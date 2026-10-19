package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val original: String,
    val harokat: String = "",
    val transliteration: String = "",
    val translationIndo: String = "",
    val scriptType: String = "ARAB_GUNDUL", // "ARAB_GUNDUL" or "SURYANI_BIRHATIYAH"
    val timestamp: Long = System.currentTimeMillis(),
    val isBookmarked: Boolean = false,
    val sourceTitle: String = "Kitab Kuning",
    val notes: String = ""
)
