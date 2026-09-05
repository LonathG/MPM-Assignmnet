package com.mindscape.app.data.local.dao

import androidx.room.*
import com.mindscape.app.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET smartAdaptiveGoalsEnabled = :enabled WHERE id = 1")
    suspend fun setSmartAdaptiveGoalsEnabled(enabled: Boolean)

    @Query("UPDATE user_profile SET checkInTime = :time WHERE id = 1")
    suspend fun updateCheckInTime(time: String)
}
