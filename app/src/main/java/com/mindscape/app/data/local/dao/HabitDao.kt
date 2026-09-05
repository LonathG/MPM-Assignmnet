package com.mindscape.app.data.local.dao

import androidx.room.*
import com.mindscape.app.data.local.entity.HabitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Query("SELECT * FROM habits ORDER BY id DESC")
    fun getAllHabits(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabitById(id: Long): HabitEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabits(habits: List<HabitEntity>)

    @Update
    suspend fun updateHabit(habit: HabitEntity)

    @Delete
    suspend fun deleteHabit(habit: HabitEntity)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteHabitById(id: Long)

    @Query("UPDATE habits SET targetValue = :targetValue, isAdapted = :isAdapted, adaptationReason = :reason WHERE id = :habitId")
    suspend fun updateHabitTarget(habitId: Long, targetValue: Int, isAdapted: Boolean, reason: String?)

    @Query("UPDATE habits SET targetValue = originalTargetValue, isAdapted = 0, adaptationReason = NULL")
    suspend fun resetAllHabitTargetsToOriginal()
}
