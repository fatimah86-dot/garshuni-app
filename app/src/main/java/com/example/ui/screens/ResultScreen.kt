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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.ai.ScriptDetector
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
fun ResultScreen(
    viewModel: GarshuniViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val activeResult by viewModel.activeResult.collectAsStateWithLifecycle()
    val arabicTextSizeSp by viewModel.arabicTextSizeSp.collectAsStateWithLifecycle()

    val tabTitles = listOf("Asli", "Harokat", "Transliterasi", "Terjemah")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ParchmentBg)
            .testTag("result_screen")
    ) {
        GarshuniTopBar(
            title = activeResult.sourceTitle,
            subtitle = if (activeResult.scriptType == ScriptDetector.TYPE_SURYANI_BIRHATIYAH)
                "Naskah Suryani Birhatiyah"
            else
                "Naskah Arab Gundul Kitab Kuning",
            onBackClick = onBackClick,
            actions = {
                IconButton(
                    onClick = { viewModel.saveToDatabase(context) },
                    modifier = Modifier.testTag("topbar_bookmark_button")
                ) {
                    Icon(
                        imageVector = if (activeResult.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Simpan Bookmark",
                        tint = GoldAccent
                    )
                }
            }
        )

        // Tab Navigation (Asli / Harokat / Transliterasi / Terjemah)
        TabRow(
            selectedTabIndex = activeResult.selectedTab,
            containerColor = EmeraldDeep,
            contentColor = ParchmentSurface,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[activeResult.selectedTab]),
                    color = GoldAccent,
                    height = 3.dp
                )
            }
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = activeResult.selectedTab == index,
                    onClick = { viewModel.setSelectedTab(index) },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = if (activeResult.selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (activeResult.selectedTab == index) GoldLight else ParchmentSurface.copy(alpha = 0.7f),
                                fontSize = 13.sp
                            )
                        )
                    },
                    modifier = Modifier.testTag("result_tab_$index")
                )
            }
        }

        // Action Toolbar (Salin, Bagikan, Simpan, Audio TTS)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp)
                .border(1.dp, ParchmentBorder, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = ParchmentSurface),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Button 1: Salin
                OutlinedButton(
                    onClick = { viewModel.copyCurrentTab(context) },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldDeep),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("action_salin_button")
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Salin", fontSize = 12.sp)
                }

                // Button 2: Bagikan
                OutlinedButton(
                    onClick = { viewModel.shareResult(context) },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldDeep),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("action_bagikan_button")
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Bagikan", fontSize = 12.sp)
                }

                // Button 3: Simpan
                Button(
                    onClick = { viewModel.saveToDatabase(context) },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (activeResult.isBookmarked) GoldDark else EmeraldDeep
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("action_simpan_button")
                ) {
                    Icon(
                        imageVector = if (activeResult.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = ParchmentSurface
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (activeResult.isBookmarked) "Tersimpan" else "Simpan", fontSize = 12.sp, color = ParchmentSurface)
                }

                // Button 4: Audio TTS
                Button(
                    onClick = { viewModel.speakCurrentTab() },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("action_audio_tts_button")
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null, modifier = Modifier.size(16.dp), tint = TextPrimaryDark)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Audio", fontSize = 12.sp, color = TextPrimaryDark, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Content Area per Tab
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            when (activeResult.selectedTab) {
                0 -> {
                    // TAB ASLI (Original Scanned Text)
                    item {
                        ScriptCardContainer(
                            headerTitle = "Teks Asli Naskah",
                            badgeText = if (activeResult.scriptType == ScriptDetector.TYPE_SURYANI_BIRHATIYAH) "Suryani / Isim A'zhom" else "Arab Gundul Asli",
                            badgeColor = EmeraldDeep
                        ) {
                            Text(
                                text = activeResult.originalText.ifEmpty { "(Belum ada teks asli)" },
                                fontFamily = AmiriFontFamily,
                                fontSize = (arabicTextSizeSp + 2).sp,
                                lineHeight = (arabicTextSizeSp * 1.6).sp,
                                color = TextPrimaryDark,
                                textAlign = TextAlign.Start,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("result_text_asli")
                            )
                        }
                    }

                    item {
                        ScholarlyNoteCard(
                            title = "Tentang Naskah Asli",
                            note = if (activeResult.scriptType == ScriptDetector.TYPE_SURYANI_BIRHATIYAH)
                                "Naskah ini memuat asma dari tradisi Suryani Birhatiyah yang dikaji dalam kitab Syamsul Ma'arif al-Kubra oleh Syaikh Ahmad bin Ali al-Buni (wafat 622 H). Teks disajikan untuk kajian filologi dan preservasi manuskrip."
                            else
                                "Arab Gundul merupakan aksara Hijaiyah tanpa harokat/tasykil yang menjadi tradisi utama penulisan kitab kuning di pondok pesantren nusantara."
                        )
                    }
                }

                1 -> {
                    // TAB HAROKAT (Arabic with full Vowelization)
                    item {
                        ScriptCardContainer(
                            headerTitle = "Teks Arab Berharokat Lengkap",
                            badgeText = "Auto-Harokat Gemini AI",
                            badgeColor = GoldDark
                        ) {
                            Text(
                                text = activeResult.harokatText.ifEmpty { activeResult.originalText },
                                fontFamily = AmiriFontFamily,
                                fontSize = (arabicTextSizeSp + 4).sp,
                                lineHeight = (arabicTextSizeSp * 1.8).sp,
                                color = EmeraldDeep,
                                textAlign = TextAlign.Start,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("result_text_harokat")
                            )
                        }
                    }

                    item {
                        ScholarlyNoteCard(
                            title = "Tashih Nahwu & Shorof",
                            note = "Vokalisasi (Fathah, Kasrah, Dhammah, Sukun, Tanwin, dan Tasydid) diproses sesuai kaidah I'rob nahwu Jurumiyah & Imrithi untuk mempermudah santri membaca dengan tepat."
                        )
                    }
                }

                2 -> {
                    // TAB TRANSLITERASI (Latin transliteration + Letter Breakdown for Birhatiyah)
                    item {
                        ScriptCardContainer(
                            headerTitle = "Transliterasi Latin",
                            badgeText = "Standar Transliterasi Arab-Latin",
                            badgeColor = EmeraldDeep
                        ) {
                            Text(
                                text = activeResult.transliterationText.ifEmpty { "(Transliterasi belum tersedia)" },
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 17.sp,
                                    lineHeight = 26.sp,
                                    color = TextPrimaryDark
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("result_text_transliterasi")
                            )
                        }
                    }

                    if (activeResult.letterBreakdown.isNotBlank()) {
                        item {
                            ScriptCardContainer(
                                headerTitle = "Uraian Huruf & Asma (Letter Breakdown)",
                                badgeText = "Analisis Syamsul Ma'arif",
                                badgeColor = GoldAccent
                            ) {
                                Text(
                                    text = activeResult.letterBreakdown,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = TextPrimaryDark,
                                        fontSize = 14.sp,
                                        lineHeight = 22.sp
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }

                3 -> {
                    // TAB TERJEMAH (Pesantren Style Indonesian Translation)
                    item {
                        ScriptCardContainer(
                            headerTitle = "Terjemah Bahasa Indonesia Gaya Pesantren",
                            badgeText = "Gaya Makna Gandul & Lugas",
                            badgeColor = EmeraldDeep
                        ) {
                            Text(
                                text = activeResult.translationIndo.ifEmpty { "(Terjemahan belum tersedia)" },
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = TextPrimaryDark,
                                    fontSize = 16.sp,
                                    lineHeight = 26.sp
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("result_text_terjemah")
                            )
                        }
                    }

                    item {
                        ScholarlyNoteCard(
                            title = "Metode Penerjemahan Pesantren",
                            note = "Terjemahan mengadopsi struktur pemaknaan kitab kuning tradisi pesantren (mubtada'-khabar / utawi-iku), menjelaskan makna kontekstual secara akademis dan santun."
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ScriptCardContainer(
    headerTitle: String,
    badgeText: String,
    badgeColor: androidx.compose.ui.graphics.Color,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ParchmentBorder, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = ParchmentSurface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = headerTitle,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = EmeraldDeep
                    )
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(badgeColor.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = badgeColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            content()
        }
    }
}

@Composable
fun ScholarlyNoteCard(
    title: String,
    note: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ParchmentBorder.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = ParchmentVariant.copy(alpha = 0.6f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "💡 $title",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = GoldDark,
                    fontSize = 12.sp
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = note,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondaryDark,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            )
        }
    }
}
