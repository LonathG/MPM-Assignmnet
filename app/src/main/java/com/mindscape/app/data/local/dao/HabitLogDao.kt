package com.mindscape.app.data.local.dao

import androidx.room.*
import com.mindscape.app.data.local.entity.HabitLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitLogDao {

    @Query("SELECT * FROM habit_logs WHERE date = :date")
    fun getLogsForDate(date: String): Flow<List<HabitLogEntity>>

    @Query("SELECT * FROM habit_logs WHERE date BETWEEN :startDate AND :endDate")
    fun getLogsBetweenDates(startDate: String, endDate: String): Flow<List<HabitLogEntity>>

    @Query("SELECT * FROM habit_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<HabitLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateLog(log: HabitLogEntity): Long

    @Query("SELECT COUNT(*) FROM habit_logs WHERE isCompleted = 1")
    suspend fun getTotalCompletedCount(): Int

    @Query("SELECT COUNT(*) FROM habit_logs")
    suspend fun getTotalLoggedCount(): Int
}
