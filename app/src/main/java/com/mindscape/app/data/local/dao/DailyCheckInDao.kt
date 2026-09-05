package com.mindscape.app.data.local.dao

import androidx.room.*
import com.mindscape.app.data.local.entity.DailyCheckInEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyCheckInDao {

    @Query("SELECT * FROM daily_check_ins ORDER BY timestamp DESC")
    fun getAllCheckIns(): Flow<List<DailyCheckInEntity>>

    @Query("SELECT * FROM daily_check_ins WHERE date = :date LIMIT 1")
    suspend fun getCheckInByDate(date: String): DailyCheckInEntity?

    @Query("SELECT * FROM daily_check_ins ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestCheckIn(): DailyCheckInEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheckIn(checkIn: DailyCheckInEntity): Long

    @Query("SELECT * FROM daily_check_ins ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentCheckIns(limit: Int): Flow<List<DailyCheckInEntity>>
}
