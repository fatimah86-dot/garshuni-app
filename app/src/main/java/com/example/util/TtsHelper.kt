package com.example.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class TtsHelper(context: Context) : TextToSpeech.OnInitListener {
    private val TAG = "TtsHelper"
    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isInitialized = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            // Default to Arabic or Indonesian
            val result = tts?.setLanguage(Locale.forLanguageTag("ar"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.w(TAG, "Arabic TTS not fully available, falling back")
            }
        } else {
            Log.e(TAG, "TextToSpeech init failed with status: $status")
        }
    }

    fun speak(text: String, isArabic: Boolean = true, speechRate: Float = 0.85f) {
        if (!isInitialized || tts == null) return
        val targetLocale = if (isArabic) Locale.forLanguageTag("ar") else Locale.forLanguageTag("id-ID")
        tts?.language = targetLocale
        tts?.setSpeechRate(speechRate)
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "garshuni_tts_${System.currentTimeMillis()}")
    }

    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
