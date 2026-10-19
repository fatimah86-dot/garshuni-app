package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.ai.ScriptDetector
import com.example.data.local.HistoryEntity
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    viewModel: GarshuniViewModel,
    onNavigateToResult: () -> Unit
) {
    val allHistory by viewModel.historyList.collectAsStateWithLifecycle()
    val bookmarksList by viewModel.bookmarksList.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableStateOf(0) } // 0: Semua Riwayat, 1: Bookmark
    var searchQuery by remember { mutableStateOf("") }
    var selectedScriptFilter by remember { mutableStateOf<String?>(null) } // null: Semua

    val sourceList = if (selectedTab == 0) allHistory else bookmarksList

    val filteredList = remember(sourceList, searchQuery, selectedScriptFilter) {
        sourceList.filter { item ->
            val matchScript = selectedScriptFilter == null || item.scriptType == selectedScriptFilter
            val matchSearch = searchQuery.isBlank() ||
                    item.sourceTitle.contains(searchQuery, ignoreCase = true) ||
                    item.original.contains(searchQuery) ||
                    item.harokat.contains(searchQuery) ||
                    item.translationIndo.contains(searchQuery, ignoreCase = true)
            matchScript && matchSearch
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ParchmentBg)
            .testTag("history_screen")
    ) {
        GarshuniTopBar(
            title = "Riwayat & Simpanan",
            subtitle = "Room Database Kitab Kuning"
        )

        // Subtabs: Semua vs Bookmark
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = EmeraldDeep,
            contentColor = ParchmentSurface,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = GoldAccent,
                    height = 3.dp
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Text(
                        text = "Semua Riwayat (${allHistory.size})",
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 0) GoldLight else ParchmentSurface.copy(alpha = 0.7f),
                        fontSize = 13.sp
                    )
                },
                modifier = Modifier.testTag("history_tab_all")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Text(
                        text = "Bookmark (${bookmarksList.size})",
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 1) GoldLight else ParchmentSurface.copy(alpha = 0.7f),
                        fontSize = 13.sp
                    )
                },
                modifier = Modifier.testTag("history_tab_bookmarks")
            )
        }

        // Search & Filter Row
        Column(modifier = Modifier.padding(14.dp)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("history_search_input"),
                placeholder = { Text("Cari naskah tersimpan...", color = TextSecondaryDark.copy(alpha = 0.6f)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = EmeraldDeep) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldDeep,
                    unfocusedBorderColor = ParchmentBorder,
                    focusedTextColor = TextPrimaryDark,
                    unfocusedTextColor = TextPrimaryDark
                ),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = selectedScriptFilter == null,
                    onClick = { selectedScriptFilter = null },
                    label = { Text("Semua Skrip", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = EmeraldDeep,
                        selectedLabelColor = ParchmentSurface
                    )
                )
                FilterChip(
                    selected = selectedScriptFilter == ScriptDetector.TYPE_ARAB_GUNDUL,
                    onClick = { selectedScriptFilter = ScriptDetector.TYPE_ARAB_GUNDUL },
                    label = { Text("Arab Gundul", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = EmeraldDeep,
                        selectedLabelColor = ParchmentSurface
                    )
                )
                FilterChip(
                    selected = selectedScriptFilter == ScriptDetector.TYPE_SURYANI_BIRHATIYAH,
                    onClick = { selectedScriptFilter = ScriptDetector.TYPE_SURYANI_BIRHATIYAH },
                    label = { Text("Suryani", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = EmeraldDeep,
                        selectedLabelColor = ParchmentSurface
                    )
                )
            }
        }

        // List
        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = EmeraldDeep.copy(alpha = 0.4f),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (selectedTab == 0) "Belum ada riwayat pemindaian" else "Belum ada bookmark disimpan",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryDark)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(filteredList, key = { it.id }) { item ->
                    HistoryCard(
                        history = item,
                        onClick = {
                            viewModel.loadFromHistory(item, onNavigateToResult)
                        },
                        onToggleBookmark = {
                            viewModel.toggleBookmark(item)
                        },
                        onDelete = {
                            viewModel.deleteHistory(item.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryCard(
    history: HistoryEntity,
    onClick: () -> Unit,
    onToggleBookmark: () -> Unit,
    onDelete: () -> Unit
) {
    val dateStr = remember(history.timestamp) {
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.forLanguageTag("id-ID"))
        sdf.format(Date(history.timestamp))
    }

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (history.scriptType == ScriptDetector.TYPE_SURYANI_BIRHATIYAH)
                                    GoldAccent.copy(alpha = 0.2f)
                                else EmeraldDeep.copy(alpha = 0.1f)
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (history.scriptType == ScriptDetector.TYPE_SURYANI_BIRHATIYAH) "Suryani Birhatiyah" else "Arab Gundul",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (history.scriptType == ScriptDetector.TYPE_SURYANI_BIRHATIYAH) GoldDark else EmeraldDeep,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = dateStr,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextSecondaryDark,
                            fontSize = 11.sp
                        )
                    )
                }

                Row {
                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (history.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = GoldAccent,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Hapus",
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = history.sourceTitle,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (history.harokat.isNotBlank()) history.harokat else history.original,
                fontFamily = AmiriFontFamily,
                fontSize = 18.sp,
                lineHeight = 26.sp,
                color = EmeraldDeep,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            if (history.translationIndo.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = history.translationIndo,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondaryDark,
                        fontSize = 12.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
