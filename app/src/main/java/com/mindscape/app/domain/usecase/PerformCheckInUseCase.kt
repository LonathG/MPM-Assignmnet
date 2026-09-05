package com.mindscape.app.domain.usecase

import com.mindscape.app.domain.engine.AdaptationResult
import com.mindscape.app.domain.engine.SmartAdaptationEngine
import com.mindscape.app.domain.model.DailyCheckIn
import com.mindscape.app.domain.repository.CheckInRepository
import com.mindscape.app.domain.repository.HabitRepository
import com.mindscape.app.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.first

class PerformCheckInUseCase(
    private val checkInRepository: CheckInRepository,
    private val habitRepository: HabitRepository,
    private val userProfileRepository: UserProfileRepository,
    private val adaptationEngine: SmartAdaptationEngine = SmartAdaptationEngine()
) {

    suspend operator fun invoke(checkIn: DailyCheckIn): AdaptationResult {
        // Save check-in
        checkInRepository.saveCheckIn(checkIn)

        // Get current habits and user profile preferences
        val currentHabits = habitRepository.getAllHabits().first()
        val userProfile = userProfileRepository.getUserProfile().first()

        // Calculate adaptation
        val result = adaptationEngine.adaptHabitsForCheckIn(
            habits = currentHabits,
            checkIn = checkIn,
            smartAdaptiveGoalsEnabled = userProfile.smartAdaptiveGoalsEnabled
        )

        // Update adapted habit targets in DB
        result.adaptedHabits.forEach { habit ->
            habitRepository.updateHabitTarget(
                habitId = habit.id,
                newTarget = habit.targetValue,
                isAdapted = habit.isAdapted,
                reason = habit.adaptationReason
            )
        }

        return result
    }
}
