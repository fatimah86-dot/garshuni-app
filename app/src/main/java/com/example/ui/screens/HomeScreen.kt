package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.ai.ScriptDetector
import com.example.data.local.HistoryEntity
import com.example.data.local.KitabEntry
import com.example.data.local.KitabLibraryData
import com.example.ui.GarshuniViewModel
import com.example.ui.ProcessingState
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.EmeraldDeep
import com.example.ui.theme.EmeraldLight
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
fun HomeScreen(
    viewModel: GarshuniViewModel,
    onNavigateToScanner: () -> Unit,
    onNavigateToResult: () -> Unit,
    onNavigateToLibrary: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    val historyList by viewModel.historyList.collectAsStateWithLifecycle()
    val processingState by viewModel.processingState.collectAsStateWithLifecycle()
    var quickTextInput by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ParchmentBg)
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Hero Parchment Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.kitab_parchment_banner),
                    contentDescription = "Parchment Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Gradient Overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    EmeraldDeep.copy(alpha = 0.85f),
                                    EmeraldDeep.copy(alpha = 0.65f),
                                    ParchmentBg
                                )
                            )
                        )
                )

                // Header Content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(GoldAccent)
                                .border(1.dp, GoldLight, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "غ",
                                fontFamily = AmiriFontFamily,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldDeep
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Garshuni",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontFamily = AmiriFontFamily,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ParchmentSurface
                                )
                            )
                            Text(
                                text = "Kitab Kuning & Hikmah Translator",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = GoldLight,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                        fontFamily = AmiriFontFamily,
                        fontSize = 20.sp,
                        color = GoldLight,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "OCR Arab Gundul • Harokat Otomatis • Suryani Birhatiyah",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ParchmentSurface.copy(alpha = 0.9f),
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }

        // Quick Action Cards
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ElevatedCard(
                        onClick = {
                            viewModel.selectedScriptOverride.value = ScriptDetector.TYPE_ARAB_GUNDUL
                            onNavigateToScanner()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("action_scan_arab_gundul"),
                        colors = CardDefaults.elevatedCardColors(containerColor = EmeraldDeep),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(GoldAccent.copy(alpha = 0.25f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = "Pindai Arab Gundul",
                                    tint = GoldAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Pindai Naskah",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = ParchmentSurface,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            )
                            Text(
                                text = "Arab Gundul & Harokat",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = GoldLight,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    ElevatedCard(
                        onClick = {
                            viewModel.selectedScriptOverride.value = ScriptDetector.TYPE_SURYANI_BIRHATIYAH
                            viewModel.loadDefaultSampleManuscript()
                            onNavigateToScanner()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("action_suryani_birhatiyah"),
                        colors = CardDefaults.elevatedCardColors(containerColor = ParchmentSurface),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(EmeraldDeep.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Suryani Birhatiyah",
                                    tint = EmeraldDeep,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Suryani Birhatiyah",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = TextPrimaryDark,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            )
                            Text(
                                text = "Syamsul Ma'arif al-Kubra",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = GoldDark,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        // Quick Text Input Box
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .border(1.dp, ParchmentBorder, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = ParchmentSurface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Ketik / Tempel Teks Kitab",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = EmeraldDeep
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = quickTextInput,
                        onValueChange = { quickTextInput = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("quick_text_input"),
                        placeholder = {
                            Text(
                                text = "Contoh: برهتية كرير atau فصل اركان الاسلام خمسة...",
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
                        maxLines = 3,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(fontFamily = AmiriFontFamily)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                if (quickTextInput.isNotBlank()) {
                                    viewModel.processTextContent(
                                        text = quickTextInput,
                                        title = "Terjemah Cepat",
                                        onComplete = onNavigateToResult
                                    )
                                }
                            },
                            enabled = quickTextInput.isNotBlank() && processingState !is ProcessingState.Processing,
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldDeep),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("quick_translate_button")
                        ) {
                            if (processingState is ProcessingState.Processing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = GoldLight,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                            Icon(
                                imageVector = Icons.Default.Translate,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = GoldLight
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Terjemahkan",
                                color = ParchmentSurface,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Featured Manuscripts Carousel
        item {
            Column(modifier = Modifier.padding(top = 12.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Khazanah Naskah Pilihan",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = EmeraldDeep
                        )
                    )
                    Text(
                        text = "Lihat Semua",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = GoldDark,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .clickable { onNavigateToLibrary() }
                            .padding(4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(KitabLibraryData.entries.take(4)) { entry ->
                        KitabFeaturedCard(
                            entry = entry,
                            onClick = {
                                viewModel.loadFromLibrary(entry, onNavigateToResult)
                            }
                        )
                    }
                }
            }
        }

        // Recent Scans from Room Database
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Riwayat Terakhir",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = EmeraldDeep
                        )
                    )
                    if (historyList.isNotEmpty()) {
                        Text(
                            text = "Buka Riwayat",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = GoldDark,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .clickable { onNavigateToHistory() }
                                .padding(4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (historyList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(ParchmentVariant.copy(alpha = 0.6f))
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                tint = EmeraldDeep.copy(alpha = 0.5f),
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Belum ada riwayat pemindaian",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = TextSecondaryDark,
                                    fontSize = 13.sp
                                )
                            )
                            Text(
                                text = "Pindai naskah kitab kuning atau teks hikmah sekarang",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondaryDark.copy(alpha = 0.7f),
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        historyList.take(3).forEach { history ->
                            HistoryItemRow(
                                history = history,
                                onClick = {
                                    viewModel.loadFromHistory(history, onNavigateToResult)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KitabFeaturedCard(
    entry: KitabEntry,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .width(260.dp)
            .border(1.dp, ParchmentBorder, RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = ParchmentSurface),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = entry.category,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = GoldDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            if (entry.scriptType == ScriptDetector.TYPE_SURYANI_BIRHATIYAH)
                                GoldAccent.copy(alpha = 0.2f)
                            else EmeraldDeep.copy(alpha = 0.1f)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (entry.scriptType == ScriptDetector.TYPE_SURYANI_BIRHATIYAH) "Suryani" else "Arab Gundul",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (entry.scriptType == ScriptDetector.TYPE_SURYANI_BIRHATIYAH) GoldDark else EmeraldDeep,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = entry.title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = entry.harokatArabic,
                fontFamily = AmiriFontFamily,
                fontSize = 18.sp,
                color = EmeraldDeep,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 26.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = entry.translationPesantren,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondaryDark,
                    fontSize = 11.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun HistoryItemRow(
    history: HistoryEntity,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ParchmentBorder.copy(alpha = 0.7f), RoundedCornerShape(10.dp)),
        colors = CardDefaults.cardColors(containerColor = ParchmentSurface),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = history.sourceTitle,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark,
                            fontSize = 13.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (history.scriptType == ScriptDetector.TYPE_SURYANI_BIRHATIYAH) "Suryani" else "Arab Gundul",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (history.scriptType == ScriptDetector.TYPE_SURYANI_BIRHATIYAH) GoldDark else EmeraldDeep,
                            fontSize = 10.sp
                        )
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (history.harokat.isNotBlank()) history.harokat else history.original,
                    fontFamily = AmiriFontFamily,
                    fontSize = 15.sp,
                    color = EmeraldDeep,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
