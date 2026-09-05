package com.mindscape.app.domain.repository

import com.mindscape.app.domain.model.DailyCheckIn
import kotlinx.coroutines.flow.Flow

interface CheckInRepository {
    fun getAllCheckIns(): Flow<List<DailyCheckIn>>
    suspend fun getCheckInByDate(date: String): DailyCheckIn?
    suspend fun getLatestCheckIn(): DailyCheckIn?
    suspend fun saveCheckIn(checkIn: DailyCheckIn): Long
}
