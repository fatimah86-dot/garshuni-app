package com.example.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.ai.ScriptDetector
import com.example.ui.GarshuniViewModel
import com.example.ui.ProcessingState
import com.example.ui.components.GarshuniTopBar
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.EmeraldDeep
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.ParchmentBg
import com.example.ui.theme.ParchmentBorder
import com.example.ui.theme.ParchmentSurface
import com.example.ui.theme.ParchmentVariant
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark

@Composable
fun ScannerScreen(
    viewModel: GarshuniViewModel,
    onNavigateToResult: () -> Unit,
    onBackClick: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val selectedBitmap by viewModel.selectedBitmap.collectAsStateWithLifecycle()
    val preprocessedBitmap by viewModel.preprocessedBitmap.collectAsStateWithLifecycle()
    val grayscaleEnabled by viewModel.grayscaleEnabled.collectAsStateWithLifecycle()
    val contrastLevel by viewModel.contrastLevel.collectAsStateWithLifecycle()
    val manualInputText by viewModel.manualInputText.collectAsStateWithLifecycle()
    val processingState by viewModel.processingState.collectAsStateWithLifecycle()
    val selectedScriptOverride by viewModel.selectedScriptOverride.collectAsStateWithLifecycle()

    var showPreprocessingPanel by remember { mutableStateOf(true) }
    var showPreprocessedPreview by remember { mutableStateOf(true) }

    // Android Zero-permission Photo Picker
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.setSelectedBitmapFromUri(context, uri)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ParchmentBg)
            .testTag("scanner_screen")
    ) {
        GarshuniTopBar(
            title = "Pemindai Naskah",
            subtitle = "OCR ML Kit & Pra-Pemrosesan Citra",
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Processing Error Banner if any
            if (processingState is ProcessingState.Error) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = (processingState as ProcessingState.Error).message,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // Image Preview Card (Original vs Preprocessed)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, ParchmentBorder, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = ParchmentSurface),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (showPreprocessedPreview) "Citra Pra-Pemrosesan (OCR)" else "Citra Asli",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldDeep
                                )
                            )

                            Row {
                                FilterChip(
                                    selected = !showPreprocessedPreview,
                                    onClick = { showPreprocessedPreview = false },
                                    label = { Text("Asli", fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = EmeraldDeep,
                                        selectedLabelColor = ParchmentSurface
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                FilterChip(
                                    selected = showPreprocessedPreview,
                                    onClick = { showPreprocessedPreview = true },
                                    label = { Text("Hasil Filter", fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = EmeraldDeep,
                                        selectedLabelColor = ParchmentSurface
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        val displayBitmap: Bitmap? = if (showPreprocessedPreview) preprocessedBitmap ?: selectedBitmap else selectedBitmap

                        if (displayBitmap != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(230.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(ParchmentVariant)
                                    .border(1.dp, ParchmentBorder, RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    bitmap = displayBitmap.asImageBitmap(),
                                    contentDescription = "Naskah yang dipindai",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(ParchmentVariant.copy(alpha = 0.5f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Belum ada citra naskah dipilih",
                                    color = TextSecondaryDark,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Image source buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("gallery_picker_button"),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldDeep)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Collections,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Pilih Foto", fontSize = 12.sp)
                            }

                            Button(
                                onClick = { viewModel.loadDefaultSampleManuscript() },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("sample_manuscript_button"),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = TextPrimaryDark
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Naskah Syamsul", color = TextPrimaryDark, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Image Preprocessing Controls (Requirement 1: grayscale, contrast)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, ParchmentBorder, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = ParchmentSurface),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showPreprocessingPanel = !showPreprocessingPanel },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Tune,
                                    contentDescription = null,
                                    tint = EmeraldDeep,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Pengaturan Pra-Pemrosesan Citra",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldDeep
                                    )
                                )
                            }
                            Text(
                                text = if (showPreprocessingPanel) "Tutup" else "Buka",
                                style = MaterialTheme.typography.labelSmall.copy(color = GoldDark)
                            )
                        }

                        if (showPreprocessingPanel) {
                            Spacer(modifier = Modifier.height(12.dp))

                            // Grayscale Switch
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Filter Grayscale (Skala Abu-Abu)",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextPrimaryDark
                                        )
                                    )
                                    Text(
                                        text = "Hilangkan noda kuning parchment untuk menonjolkan tinta",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextSecondaryDark,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                                Switch(
                                    checked = grayscaleEnabled,
                                    onCheckedChange = { viewModel.onGrayscaleToggle(it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = ParchmentSurface,
                                        checkedTrackColor = EmeraldDeep
                                    ),
                                    modifier = Modifier.testTag("grayscale_switch")
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Contrast Adjustment Slider
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Peningkat Kontras Naskah",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextPrimaryDark
                                        )
                                    )
                                    Text(
                                        text = "${String.format("%.1f", contrastLevel)}x",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = EmeraldDeep
                                        )
                                    )
                                }
                                Slider(
                                    value = contrastLevel,
                                    onValueChange = { viewModel.onContrastChange(it) },
                                    valueRange = 1.0f..2.5f,
                                    colors = SliderDefaults.colors(
                                        thumbColor = EmeraldDeep,
                                        activeTrackColor = EmeraldDeep,
                                        inactiveTrackColor = ParchmentBorder
                                    ),
                                    modifier = Modifier.testTag("contrast_slider")
                                )
                            }
                        }
                    }
                }
            }

            // Script Selection Mode
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, ParchmentBorder, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = ParchmentSurface),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Mode Naskah & Skrip",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = EmeraldDeep
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = selectedScriptOverride == null,
                                onClick = { viewModel.selectedScriptOverride.value = null },
                                label = { Text("Otomatis", fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = EmeraldDeep,
                                    selectedLabelColor = ParchmentSurface
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            FilterChip(
                                selected = selectedScriptOverride == ScriptDetector.TYPE_ARAB_GUNDUL,
                                onClick = { viewModel.selectedScriptOverride.value = ScriptDetector.TYPE_ARAB_GUNDUL },
                                label = { Text("Arab Gundul", fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = EmeraldDeep,
                                    selectedLabelColor = ParchmentSurface
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            FilterChip(
                                selected = selectedScriptOverride == ScriptDetector.TYPE_SURYANI_BIRHATIYAH,
                                onClick = { viewModel.selectedScriptOverride.value = ScriptDetector.TYPE_SURYANI_BIRHATIYAH },
                                label = { Text("Suryani", fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = EmeraldDeep,
                                    selectedLabelColor = ParchmentSurface
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Manual Text Input / Edit Detected Text
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, ParchmentBorder, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = ParchmentSurface),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Teks Terbaca / Masukan Manual",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = EmeraldDeep
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = manualInputText,
                            onValueChange = { viewModel.manualInputText.value = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("scanner_manual_text_field"),
                            placeholder = {
                                Text(
                                    text = "Hasil OCR akan muncul di sini atau Anda dapat mengetik naskah Arab Gundul / Suryani...",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = TextSecondaryDark.copy(alpha = 0.6f),
                                        fontFamily = AmiriFontFamily
                                    )
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldDeep,
                                unfocusedBorderColor = ParchmentBorder,
                                focusedTextColor = TextPrimaryDark,
                                unfocusedTextColor = TextPrimaryDark
                            ),
                            minLines = 3,
                            maxLines = 6,
                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                fontFamily = AmiriFontFamily,
                                fontSize = 18.sp,
                                lineHeight = 26.sp
                            )
                        )
                    }
                }
            }

            // Execute Button
            item {
                Button(
                    onClick = {
                        if (manualInputText.isNotBlank()) {
                            viewModel.processTextContent(
                                text = manualInputText,
                                title = "Naskah Pemindaian",
                                onComplete = onNavigateToResult
                            )
                        } else {
                            viewModel.performOcrAndProcess(onNavigateToResult)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("start_scan_and_translate_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldDeep),
                    shape = RoundedCornerShape(12.dp),
                    enabled = processingState !is ProcessingState.Processing
                ) {
                    if (processingState is ProcessingState.Processing) {
                        CircularProgressIndicator(
                            color = GoldLight,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = (processingState as ProcessingState.Processing).message,
                            color = ParchmentSurface,
                            fontSize = 13.sp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.DocumentScanner,
                            contentDescription = null,
                            tint = GoldLight
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (manualInputText.isNotBlank()) "Terjemahkan Naskah Sekarang" else "Pindai OCR & Terjemahkan",
                            color = ParchmentSurface,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}
