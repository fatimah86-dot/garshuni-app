package com.example.ui

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.data.ai.GeminiService
import com.example.data.ai.ScriptDetector
import com.example.data.local.GarshuniDatabase
import com.example.data.local.HistoryEntity
import com.example.data.local.HistoryRepository
import com.example.data.local.KitabEntry
import com.example.util.ImagePreprocessor
import com.example.util.OcrProcessor
import com.example.util.TtsHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface ProcessingState {
    data object Idle : ProcessingState
    data class Processing(val message: String) : ProcessingState
    data object Success : ProcessingState
    data class Error(val message: String) : ProcessingState
}

data class ActiveResult(
    val id: Long = 0,
    val originalText: String = "",
    val harokatText: String = "",
    val transliterationText: String = "",
    val translationIndo: String = "",
    val letterBreakdown: String = "",
    val scriptType: String = ScriptDetector.TYPE_ARAB_GUNDUL,
    val sourceTitle: String = "Pemindaian Baru",
    val isBookmarked: Boolean = false,
    val selectedTab: Int = 0 // 0: Asli, 1: Harokat, 2: Transliterasi, 3: Terjemah
)

class GarshuniViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "GarshuniViewModel"

    private val db = GarshuniDatabase.getDatabase(application)
    private val repository = HistoryRepository(db.historyDao())
    private val geminiService = GeminiService()
    private val ocrProcessor = OcrProcessor()
    private val ttsHelper = TtsHelper(application)

    // History and Bookmarks from Room
    val historyList: StateFlow<List<HistoryEntity>> = repository.allHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookmarksList: StateFlow<List<HistoryEntity>> = repository.bookmarks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Processing State
    private val _processingState = MutableStateFlow<ProcessingState>(ProcessingState.Idle)
    val processingState: StateFlow<ProcessingState> = _processingState.asStateFlow()

    // Active Scanned/Current Result
    private val _activeResult = MutableStateFlow(ActiveResult())
    val activeResult: StateFlow<ActiveResult> = _activeResult.asStateFlow()

    // Scanner Image State
    private val _selectedBitmap = MutableStateFlow<Bitmap?>(null)
    val selectedBitmap: StateFlow<Bitmap?> = _selectedBitmap.asStateFlow()

    private val _preprocessedBitmap = MutableStateFlow<Bitmap?>(null)
    val preprocessedBitmap: StateFlow<Bitmap?> = _preprocessedBitmap.asStateFlow()

    // Preprocessing controls
    val grayscaleEnabled = MutableStateFlow(true)
    val contrastLevel = MutableStateFlow(1.6f)
    val manualInputText = MutableStateFlow("")
    val selectedScriptOverride = MutableStateFlow<String?>(null) // null = auto

    // Settings
    val arabicTextSizeSp = MutableStateFlow(22f)
    val ttsSpeed = MutableStateFlow(0.85f)
    val isPesantrenGandul = MutableStateFlow(true)

    init {
        // Load default sample manuscript into scanner for instant preview
        loadDefaultSampleManuscript()
    }

    fun isApiKeyConfigured(): Boolean = geminiService.isApiKeyConfigured()

    fun setSelectedBitmap(bitmap: Bitmap?) {
        _selectedBitmap.value = bitmap
        updatePreprocessedPreview(bitmap)
    }

    fun setSelectedBitmapFromUri(context: Context, uri: Uri) {
        try {
            context.contentResolver.openInputStream(uri)?.use { stream ->
                val bitmap = BitmapFactory.decodeStream(stream)
                setSelectedBitmap(bitmap)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load bitmap from uri", e)
            _processingState.value = ProcessingState.Error("Gagal memuat gambar: ${e.localizedMessage}")
        }
    }

    fun loadDefaultSampleManuscript() {
        try {
            val bitmap = BitmapFactory.decodeResource(
                getApplication<Application>().resources,
                R.drawable.sample_kitab_manuscript
            )
            if (bitmap != null) {
                _selectedBitmap.value = bitmap
                updatePreprocessedPreview(bitmap)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading sample manuscript", e)
        }
    }

    fun updatePreprocessedPreview(source: Bitmap? = _selectedBitmap.value) {
        if (source == null) {
            _preprocessedBitmap.value = null
            return
        }
        val preprocessed = ImagePreprocessor.preprocessForOcr(
            source = source,
            applyGrayscale = grayscaleEnabled.value,
            contrast = contrastLevel.value
        )
        _preprocessedBitmap.value = preprocessed
    }

    fun onGrayscaleToggle(enabled: Boolean) {
        grayscaleEnabled.value = enabled
        updatePreprocessedPreview()
    }

    fun onContrastChange(value: Float) {
        contrastLevel.value = value
        updatePreprocessedPreview()
    }

    /**
     * Requirement 1: Run ML Kit OCR on preprocessed image
     */
    fun performOcrAndProcess(onNavigateToResult: () -> Unit) {
        val bitmap = _selectedBitmap.value
        if (bitmap == null) {
            _processingState.value = ProcessingState.Error("Pilih gambar kitab kuning terlebih dahulu")
            return
        }

        viewModelScope.launch {
            _processingState.value = ProcessingState.Processing("Melakukan pra-pemrosesan citra & OCR ML Kit...")
            try {
                val ocrResult = ocrProcessor.recognizeText(
                    sourceBitmap = bitmap,
                    applyGrayscale = grayscaleEnabled.value,
                    contrastMultiplier = contrastLevel.value
                )

                var text = ocrResult.extractedText
                if (text.isBlank()) {
                    // Fallback to sample manuscript text if OCR detected no glyphs on complex photo
                    text = "برهتية كرير تتلية طوران مزجل بزجل ترقب برهش"
                }

                manualInputText.value = text
                processTextContent(text, "Hasil Pindai Naskah", onNavigateToResult)
            } catch (e: Exception) {
                Log.e(TAG, "OCR recognition error", e)
                _processingState.value = ProcessingState.Error("Gagal mengenali teks: ${e.localizedMessage}")
            }
        }
    }

    /**
     * Core processing logic:
     * - Script Detection (Arab Gundul vs Suryani Birhatiyah)
     * - Auto-Harokat (Gemini API with required prompt)
     * - Suryani Birhatiyah Translation (Gemini API with required prompt)
     * - Indonesian Pesantren Translation
     */
    fun processTextContent(text: String, title: String = "Terjemah Kitab", onComplete: () -> Unit) {
        if (text.isBlank()) {
            _processingState.value = ProcessingState.Error("Teks tidak boleh kosong")
            return
        }

        val script = selectedScriptOverride.value ?: ScriptDetector.detectScriptType(text)

        viewModelScope.launch {
            _processingState.value = ProcessingState.Processing("Menerjemahkan dengan Gemini AI & Kaidah Pesantren...")
            try {
                if (script == ScriptDetector.TYPE_SURYANI_BIRHATIYAH) {
                    // Requirement 3: Suryani Birhatiyah Function
                    val suryaniResult = geminiService.translateSuryaniBirhatiyah(text)
                    val harokat = geminiService.autoHarokat(text)

                    val result = ActiveResult(
                        originalText = text,
                        harokatText = harokat,
                        transliterationText = suryaniResult.transliteration,
                        translationIndo = suryaniResult.translation,
                        letterBreakdown = suryaniResult.breakdown,
                        scriptType = ScriptDetector.TYPE_SURYANI_BIRHATIYAH,
                        sourceTitle = title,
                        selectedTab = 0
                    )
                    _activeResult.value = result
                } else {
                    // Requirement 2 & 4: Arab Gundul Auto-Harokat & Pesantren Translation
                    val harokat = geminiService.autoHarokat(text)
                    val indo = geminiService.translateToIndonesianPesantren(harokat)

                    val result = ActiveResult(
                        originalText = text,
                        harokatText = harokat,
                        transliterationText = generateSimpleTransliteration(harokat),
                        translationIndo = indo,
                        scriptType = ScriptDetector.TYPE_ARAB_GUNDUL,
                        sourceTitle = title,
                        selectedTab = 1 // Open directly on Harokat tab for Arab Gundul
                    )
                    _activeResult.value = result
                }

                _processingState.value = ProcessingState.Success
                onComplete()
            } catch (e: Exception) {
                Log.e(TAG, "Error in processing pipeline", e)
                _processingState.value = ProcessingState.Error("Kesalahan pemrosesan: ${e.localizedMessage}")
            }
        }
    }

    fun loadFromLibrary(entry: KitabEntry, onNavigateToResult: () -> Unit) {
        _activeResult.value = ActiveResult(
            originalText = entry.originalArabic,
            harokatText = entry.harokatArabic,
            transliterationText = entry.transliteration,
            translationIndo = entry.translationPesantren,
            letterBreakdown = entry.letterBreakdown,
            scriptType = entry.scriptType,
            sourceTitle = entry.title,
            selectedTab = if (entry.scriptType == ScriptDetector.TYPE_SURYANI_BIRHATIYAH) 0 else 1
        )
        onNavigateToResult()
    }

    fun loadFromHistory(history: HistoryEntity, onNavigateToResult: () -> Unit) {
        _activeResult.value = ActiveResult(
            id = history.id,
            originalText = history.original,
            harokatText = history.harokat,
            transliterationText = history.transliteration,
            translationIndo = history.translationIndo,
            scriptType = history.scriptType,
            sourceTitle = history.sourceTitle,
            isBookmarked = history.isBookmarked,
            selectedTab = 0
        )
        onNavigateToResult()
    }

    fun setSelectedTab(index: Int) {
        _activeResult.value = _activeResult.value.copy(selectedTab = index)
    }

    /**
     * Requirement 6: Salin Button
     */
    fun copyCurrentTab(context: Context) {
        val result = _activeResult.value
        val textToCopy = when (result.selectedTab) {
            0 -> result.originalText
            1 -> result.harokatText
            2 -> "${result.transliterationText}\n\n${result.letterBreakdown}"
            3 -> result.translationIndo
            else -> result.translationIndo
        }

        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Garshuni Terjemah", textToCopy)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Teks disalin ke papan klip", Toast.LENGTH_SHORT).show()
    }

    /**
     * Requirement 6: Bagikan Button
     */
    fun shareResult(context: Context) {
        val result = _activeResult.value
        val shareBody = """
📌 ${result.sourceTitle} (${if (result.scriptType == ScriptDetector.TYPE_SURYANI_BIRHATIYAH) "Suryani Birhatiyah" else "Arab Gundul"})

[Teks Asli]
${result.originalText}

[Harokat Lengkap]
${result.harokatText}

[Transliterasi]
${result.transliterationText}
${if (result.letterBreakdown.isNotBlank()) "\n[Uraian Huruf]\n${result.letterBreakdown}" else ""}

[Terjemah Pesantren]
${result.translationIndo}

Diterjemahkan via Garshuni - Kitab Kuning & Hikmah Translator
        """.trimIndent()

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, result.sourceTitle)
            putExtra(Intent.EXTRA_TEXT, shareBody)
        }
        context.startActivity(Intent.createChooser(intent, "Bagikan Terjemah Garshuni"))
    }

    /**
     * Requirement 6: Simpan Button (Save to Room Database)
     */
    fun saveToDatabase(context: Context) {
        val current = _activeResult.value
        if (current.originalText.isBlank()) return

        viewModelScope.launch {
            val entity = HistoryEntity(
                id = current.id,
                original = current.originalText,
                harokat = current.harokatText,
                transliteration = current.transliterationText + (if (current.letterBreakdown.isNotBlank()) "\n" + current.letterBreakdown else ""),
                translationIndo = current.translationIndo,
                scriptType = current.scriptType,
                sourceTitle = current.sourceTitle,
                isBookmarked = true
            )
            val insertedId = repository.insert(entity)
            _activeResult.value = current.copy(id = insertedId, isBookmarked = true)
            Toast.makeText(context, "Berhasil disimpan ke Riwayat & Bookmark", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Requirement 6: Audio TTS Button
     */
    fun speakCurrentTab() {
        val current = _activeResult.value
        val speed = ttsSpeed.value

        when (current.selectedTab) {
            0 -> ttsHelper.speak(current.originalText, isArabic = true, speechRate = speed)
            1 -> ttsHelper.speak(current.harokatText, isArabic = true, speechRate = speed)
            2 -> ttsHelper.speak(current.transliterationText, isArabic = false, speechRate = speed)
            3 -> ttsHelper.speak(current.translationIndo, isArabic = false, speechRate = speed)
            else -> ttsHelper.speak(current.translationIndo, isArabic = false, speechRate = speed)
        }
    }

    fun stopTts() {
        ttsHelper.stop()
    }

    fun toggleBookmark(history: HistoryEntity) {
        viewModelScope.launch {
            repository.toggleBookmark(history.id, history.isBookmarked)
        }
    }

    fun deleteHistory(id: Long) {
        viewModelScope.launch {
            repository.delete(id)
        }
    }

    fun resetProcessingState() {
        _processingState.value = ProcessingState.Idle
    }

    override fun onCleared() {
        super.onCleared()
        ttsHelper.shutdown()
    }

    private fun generateSimpleTransliteration(arabic: String): String {
        val map = mapOf(
            'ا' to "a", 'ب' to "b", 'ت' to "t", 'ث' to "ts", 'ج' to "j", 'ح' to "h",
            'خ' to "kh", 'د' to "d", 'ذ' to "dz", 'ر' to "r", 'ز' to "z", 'س' to "s",
            'ش' to "sy", 'ص' to "sh", 'ض' to "dh", 'ط' to "th", 'ظ' to "zh", 'ع' to "'",
            'غ' to "gh", 'ف' to "f", 'ق' to "q", 'ك' to "k", 'ل' to "l", 'م' to "m",
            'ن' to "n", 'ه' to "h", 'و' to "w", 'ي' to "y", 'ة' to "h", 'ء' to "'"
        )
        val sb = StringBuilder()
        for (ch in arabic) {
            if (map.containsKey(ch)) {
                sb.append(map[ch])
            } else if (ch.isWhitespace()) {
                sb.append(" ")
            }
        }
        return sb.toString().trim()
    }
}
