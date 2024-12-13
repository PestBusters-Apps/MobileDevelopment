package com.example.pestbusters.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryDao {
    @Insert
    suspend fun insertHistory(history: HistoryEntity)

    @Query("SELECT * FROM history ORDER BY id DESC")
    fun getAllHistory(): LiveData<List<HistoryEntity>>
}
