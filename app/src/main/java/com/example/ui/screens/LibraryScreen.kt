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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
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
import com.example.data.ai.ScriptDetector
import com.example.data.local.KitabEntry
import com.example.data.local.KitabLibraryData
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
fun LibraryScreen(
    viewModel: GarshuniViewModel,
    onNavigateToResult: () -> Unit
) {
    val categories = listOf("Semua", "Hikmah & Suryani", "Fiqih & Usul", "Tasawwuf")
    var selectedCategoryIndex by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredEntries = remember(selectedCategoryIndex, searchQuery) {
        KitabLibraryData.entries.filter { entry ->
            val matchCategory = selectedCategoryIndex == 0 || entry.category == categories[selectedCategoryIndex]
            val matchSearch = searchQuery.isBlank() ||
                    entry.title.contains(searchQuery, ignoreCase = true) ||
                    entry.bookSource.contains(searchQuery, ignoreCase = true) ||
                    entry.originalArabic.contains(searchQuery) ||
                    entry.translationPesantren.contains(searchQuery, ignoreCase = true)
            matchCategory && matchSearch
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ParchmentBg)
            .testTag("library_screen")
    ) {
        GarshuniTopBar(
            title = "Perpustakaan Kitab",
            subtitle = "Koleksi Naskah Kuning & Syamsul Ma'arif"
        )

        // Search Bar
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("library_search_field"),
                placeholder = {
                    Text(
                        text = "Cari judul kitab, fasal, atau asma...",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryDark.copy(alpha = 0.6f))
                    )
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = EmeraldDeep)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldDeep,
                    unfocusedBorderColor = ParchmentBorder,
                    focusedTextColor = TextPrimaryDark,
                    unfocusedTextColor = TextPrimaryDark
                ),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
        }

        // Category Filter Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedCategoryIndex,
            containerColor = ParchmentBg,
            contentColor = EmeraldDeep,
            edgePadding = 16.dp,
            divider = {}
        ) {
            categories.forEachIndexed { index, cat ->
                FilterChip(
                    selected = selectedCategoryIndex == index,
                    onClick = { selectedCategoryIndex = index },
                    label = {
                        Text(
                            text = cat,
                            fontSize = 12.sp,
                            fontWeight = if (selectedCategoryIndex == index) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = EmeraldDeep,
                        selectedLabelColor = ParchmentSurface,
                        containerColor = ParchmentSurface,
                        labelColor = TextPrimaryDark
                    ),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .testTag("library_cat_$index")
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Books List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 80.dp, top = 6.dp)
        ) {
            items(filteredEntries, key = { it.id }) { entry ->
                KitabLibraryCard(
                    entry = entry,
                    onOpen = {
                        viewModel.loadFromLibrary(entry, onNavigateToResult)
                    }
                )
            }
        }
    }
}

@Composable
fun KitabLibraryCard(
    entry: KitabEntry,
    onOpen: () -> Unit
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
                Column {
                    Text(
                        text = entry.category,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = GoldDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    )
                    Text(
                        text = entry.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark,
                            fontSize = 16.sp
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (entry.scriptType == ScriptDetector.TYPE_SURYANI_BIRHATIYAH)
                                GoldAccent.copy(alpha = 0.2f)
                            else EmeraldDeep.copy(alpha = 0.1f)
                        )
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = if (entry.scriptType == ScriptDetector.TYPE_SURYANI_BIRHATIYAH) "Suryani Birhatiyah" else "Arab Gundul",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (entry.scriptType == ScriptDetector.TYPE_SURYANI_BIRHATIYAH) GoldDark else EmeraldDeep,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Sumber: ${entry.bookSource}",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondaryDark,
                    fontSize = 12.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Arabic Preview in Amiri
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(ParchmentVariant.copy(alpha = 0.5f))
                    .padding(10.dp)
            ) {
                Text(
                    text = entry.harokatArabic,
                    fontFamily = AmiriFontFamily,
                    fontSize = 20.sp,
                    lineHeight = 30.sp,
                    color = EmeraldDeep,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = entry.translationPesantren,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondaryDark,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onOpen,
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldDeep),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("open_kitab_${entry.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoStories,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = GoldLight
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Kaji & Terjemahkan",
                        fontSize = 12.sp,
                        color = ParchmentSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
