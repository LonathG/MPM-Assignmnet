package com.mindscape.app.domain.repository

import com.mindscape.app.domain.model.Habit
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun getAllHabits(): Flow<List<Habit>>
    suspend fun getHabitById(id: Long): Habit?
    suspend fun insertHabit(habit: Habit): Long
    suspend fun updateHabit(habit: Habit)
    suspend fun deleteHabit(habit: Habit)
    suspend fun updateHabitTarget(habitId: Long, newTarget: Int, isAdapted: Boolean, reason: String?)
    suspend fun resetAllTargetsToOriginal()
}
