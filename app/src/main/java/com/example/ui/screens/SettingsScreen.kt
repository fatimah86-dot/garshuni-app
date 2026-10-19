package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.GarshuniViewModel
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
fun SettingsScreen(
    viewModel: GarshuniViewModel
) {
    val isApiConfigured = viewModel.isApiKeyConfigured()
    val arabicSize by viewModel.arabicTextSizeSp.collectAsStateWithLifecycle()
    val ttsSpeed by viewModel.ttsSpeed.collectAsStateWithLifecycle()
    val isPesantrenGandul by viewModel.isPesantrenGandul.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ParchmentBg)
            .testTag("settings_screen")
    ) {
        GarshuniTopBar(
            title = "Pengaturan",
            subtitle = "Konfigurasi Garshuni & AI Studio"
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // AI Status Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, ParchmentBorder, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = ParchmentSurface),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (isApiConfigured) EmeraldDeep else GoldDark),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isApiConfigured) Icons.Default.CheckCircle else Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = ParchmentSurface,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (isApiConfigured) "Gemini API Terhubung" else "Mode Korpus Pesantren & AI Standar",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimaryDark,
                                        fontSize = 15.sp
                                    )
                                )
                                Text(
                                    text = if (isApiConfigured)
                                        "Kunci API aktif via Secrets Panel AI Studio"
                                    else
                                        "Menggunakan pustaka kitab kuning & korpus cerdas bawaan",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondaryDark,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(ParchmentVariant)
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "💡 Untuk mengaktifkan Gemini API kustom, atur GEMINI_API_KEY di Secrets Panel AI Studio.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondaryDark,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            }

            // Typography & Arabic Font Size
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, ParchmentBorder, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = ParchmentSurface),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.FormatSize, contentDescription = null, tint = EmeraldDeep)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Ukuran Tulisan Arab (Font Amiri)",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldDeep
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Preview Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(ParchmentVariant.copy(alpha = 0.6f))
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                                fontFamily = AmiriFontFamily,
                                fontSize = arabicSize.sp,
                                color = EmeraldDeep
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("18 sp", style = MaterialTheme.typography.labelSmall)
                            Text("${arabicSize.toInt()} sp", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = EmeraldDeep))
                            Text("32 sp", style = MaterialTheme.typography.labelSmall)
                        }

                        Slider(
                            value = arabicSize,
                            onValueChange = { viewModel.arabicTextSizeSp.value = it },
                            valueRange = 18f..32f,
                            colors = SliderDefaults.colors(
                                thumbColor = EmeraldDeep,
                                activeTrackColor = EmeraldDeep,
                                inactiveTrackColor = ParchmentBorder
                            ),
                            modifier = Modifier.testTag("font_size_slider")
                        )
                    }
                }
            }

            // Audio TTS Settings
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, ParchmentBorder, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = ParchmentSurface),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.RecordVoiceOver, contentDescription = null, tint = EmeraldDeep)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Kecepatan Pelafalan Audio TTS",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldDeep
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Lambat (0.6x)", style = MaterialTheme.typography.labelSmall)
                            Text("${String.format("%.2f", ttsSpeed)}x", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = EmeraldDeep))
                            Text("Cepat (1.2x)", style = MaterialTheme.typography.labelSmall)
                        }

                        Slider(
                            value = ttsSpeed,
                            onValueChange = { viewModel.ttsSpeed.value = it },
                            valueRange = 0.6f..1.2f,
                            colors = SliderDefaults.colors(
                                thumbColor = EmeraldDeep,
                                activeTrackColor = EmeraldDeep,
                                inactiveTrackColor = ParchmentBorder
                            ),
                            modifier = Modifier.testTag("tts_speed_slider")
                        )
                    }
                }
            }

            // Pesantren Translation Style
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, ParchmentBorder, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = ParchmentSurface),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Gaya Terjemah Pesantren Tradisional",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimaryDark
                                    )
                                )
                                Text(
                                    text = "Mengutamakan notasi makna gandul (utawi-iku, mubtada'-khabar)",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondaryDark,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                            Switch(
                                checked = isPesantrenGandul,
                                onCheckedChange = { viewModel.isPesantrenGandul.value = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = ParchmentSurface,
                                    checkedTrackColor = EmeraldDeep
                                ),
                                modifier = Modifier.testTag("gandul_style_switch")
                            )
                        }
                    }
                }
            }

            // About Garshuni & Syamsul Ma'arif Preservation
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, ParchmentBorder, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = ParchmentVariant.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = GoldDark)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Preservasi Filologi & Manuskrip",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = GoldDark
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Garshuni dirancang untuk pelestarian akademis khazanah naskah Islam klasik, memadukan OCR cerdas untuk Arab Gundul tanpa harokat dari kitab kuning serta aksara Suryani Birhatiyah dari kitab Syamsul Ma'arif al-Kubra.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondaryDark,
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Versi 1.0.0 • Native Android (Jetpack Compose & Room DB)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextSecondaryDark.copy(alpha = 0.7f),
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
