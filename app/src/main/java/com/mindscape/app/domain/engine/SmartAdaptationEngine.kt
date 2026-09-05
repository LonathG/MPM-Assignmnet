package com.mindscape.app.domain.engine

import com.mindscape.app.domain.model.DailyCheckIn
import com.mindscape.app.domain.model.EnergyLevel
import com.mindscape.app.domain.model.Habit
import kotlin.math.max

data class AdaptationResult(
    val adaptedHabits: List<Habit>,
    val summaryMessage: String,
    val adjustedHabitCount: Int
)

class SmartAdaptationEngine {

    fun adaptHabitsForCheckIn(
        habits: List<Habit>,
        checkIn: DailyCheckIn,
        smartAdaptiveGoalsEnabled: Boolean = true
    ): AdaptationResult {
        if (!smartAdaptiveGoalsEnabled) {
            val restored = habits.map { habit ->
                habit.copy(
                    targetValue = habit.originalTargetValue,
                    isAdapted = false,
                    adaptationReason = null
                )
            }
            return AdaptationResult(
                adaptedHabits = restored,
                summaryMessage = "Smart Adaptive Goals are disabled. Habits are set to standard targets.",
                adjustedHabitCount = 0
            )
        }

        var count = 0
        val adaptedList = habits.map { habit ->
            val (newTarget, isAdapted, reason) = calculateAdaptedTarget(habit, checkIn)
            if (isAdapted) count++
            habit.copy(
                targetValue = newTarget,
                isAdapted = isAdapted,
                adaptationReason = reason
            )
        }

        val summary = when {
            count > 0 -> "We've automatically eased your daily habits to match your current energy level."
            else -> "Your energy is high! Your habit targets remain at full capacity."
        }

        return AdaptationResult(
            adaptedHabits = adaptedList,
            summaryMessage = summary,
            adjustedHabitCount = count
        )
    }

    private fun calculateAdaptedTarget(
        habit: Habit,
        checkIn: DailyCheckIn
    ): Triple<Int, Boolean, String?> {
        val original = habit.originalTargetValue
        val mood = checkIn.moodScore
        val energy = checkIn.energyLevel

        // If mood is Stormy/Rainy (1-2) or Energy is Low
        if (mood <= 2 || energy == EnergyLevel.LOW) {
            val downscaled = max(1, (original * 0.33f).toInt())
            if (downscaled < original) {
                val reason = "Since you're feeling '${checkIn.moodLabel}', we reduced this from $original ${habit.targetUnit} to keep it manageable today."
                return Triple(downscaled, true, reason)
            }
        }
        // If mood is Okay (3) or Energy is Medium
        else if (mood == 3) {
            val downscaled = max(1, (original * 0.5f).toInt())
            if (downscaled < original) {
                val reason = "Since you're feeling 'Okay', we reduced this from $original ${habit.targetUnit} to keep it manageable today."
                return Triple(downscaled, true, reason)
            }
        }

        // Full capacity for Sunny/Radiant or High Energy
        return Triple(original, false, null)
    }
}
