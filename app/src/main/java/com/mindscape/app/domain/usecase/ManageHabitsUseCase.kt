package com.mindscape.app.domain.usecase

import com.mindscape.app.domain.model.Habit
import com.mindscape.app.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow

class ManageHabitsUseCase(
    private val habitRepository: HabitRepository
) {
    fun getAllHabits(): Flow<List<Habit>> = habitRepository.getAllHabits()

    suspend fun createHabit(habit: Habit): Long = habitRepository.insertHabit(habit)

    suspend fun updateHabit(habit: Habit) = habitRepository.updateHabit(habit)

    suspend fun deleteHabit(habit: Habit) = habitRepository.deleteHabit(habit)

    suspend fun toggleHabitCompletion(habit: Habit) {
        val updated = habit.copy(
            isCompleted = !habit.isCompleted,
            currentValue = if (!habit.isCompleted) habit.targetValue else 0
        )
        habitRepository.updateHabit(updated)
    }

    suspend fun incrementHabitValue(habit: Habit) {
        val newCurrent = habit.currentValue + 1
        val isDone = newCurrent >= habit.targetValue
        val updated = habit.copy(
            currentValue = newCurrent,
            isCompleted = isDone
        )
        habitRepository.updateHabit(updated)
    }
}
