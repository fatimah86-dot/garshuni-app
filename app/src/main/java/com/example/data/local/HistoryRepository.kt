package com.example.data.local

import kotlinx.coroutines.flow.Flow

class HistoryRepository(private val dao: HistoryDao) {
    val allHistory: Flow<List<HistoryEntity>> = dao.getAllHistory()
    val bookmarks: Flow<List<HistoryEntity>> = dao.getBookmarks()

    fun getByScriptType(scriptType: String): Flow<List<HistoryEntity>> =
        dao.getByScriptType(scriptType)

    fun observeById(id: Long): Flow<HistoryEntity?> = dao.observeById(id)

    suspend fun getById(id: Long): HistoryEntity? = dao.getById(id)

    suspend fun insert(item: HistoryEntity): Long = dao.insertHistory(item)

    suspend fun update(item: HistoryEntity) = dao.updateHistory(item)

    suspend fun toggleBookmark(id: Long, isCurrentBookmarked: Boolean) {
        dao.setBookmark(id, !isCurrentBookmarked)
    }

    suspend fun delete(id: Long) = dao.deleteById(id)

    suspend fun clearAll() = dao.clearAll()
}
