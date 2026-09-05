package com.mindscape.app.data.repository

import com.mindscape.app.data.local.dao.HabitDao
import com.mindscape.app.data.local.entity.HabitEntity
import com.mindscape.app.domain.model.Habit
import com.mindscape.app.domain.model.HabitCategory
import com.mindscape.app.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HabitRepositoryImpl(
    private val habitDao: HabitDao
) : HabitRepository {

    override fun getAllHabits(): Flow<List<Habit>> {
        return habitDao.getAllHabits().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getHabitById(id: Long): Habit? {
        return habitDao.getHabitById(id)?.toDomainModel()
    }

    override suspend fun insertHabit(habit: Habit): Long {
        return habitDao.insertHabit(habit.toEntity())
    }

    override suspend fun updateHabit(habit: Habit) {
        habitDao.updateHabit(habit.toEntity())
    }

    override suspend fun deleteHabit(habit: Habit) {
        habitDao.deleteHabit(habit.toEntity())
    }

    override suspend fun updateHabitTarget(
        habitId: Long,
        newTarget: Int,
        isAdapted: Boolean,
        reason: String?
    ) {
        habitDao.updateHabitTarget(habitId, newTarget, isAdapted, reason)
    }

    override suspend fun resetAllTargetsToOriginal() {
        habitDao.resetAllHabitTargetsToOriginal()
    }

    private fun HabitEntity.toDomainModel(): Habit {
        return Habit(
            id = id,
            name = name,
            category = HabitCategory.fromString(category),
            frequency = frequency,
            targetValue = targetValue,
            targetUnit = targetUnit,
            currentValue = currentValue,
            isCompleted = isCompleted,
            isAdapted = isAdapted,
            originalTargetValue = originalTargetValue,
            adaptationReason = adaptationReason,
            isStarter = isStarter
        )
    }

    private fun Habit.toEntity(): HabitEntity {
        return HabitEntity(
            id = id,
            name = name,
            category = category.displayName,
            frequency = frequency,
            targetValue = targetValue,
            targetUnit = targetUnit,
            currentValue = currentValue,
            isCompleted = isCompleted,
            isAdapted = isAdapted,
            originalTargetValue = originalTargetValue,
            adaptationReason = adaptationReason,
            isStarter = isStarter
        )
    }
}
