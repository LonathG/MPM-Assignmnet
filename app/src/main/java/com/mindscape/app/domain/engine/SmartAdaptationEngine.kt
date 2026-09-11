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

class SmartAdaptationEngine(
    private val mlEngine: HabitAdaptationEngine? = null
) {

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

        // If ML engine is available, use ML model prediction
        if (mlEngine != null) {
            val predictedClass = mlEngine.predict(energy, mood, original)
            return when (predictedClass) {
                HabitAdaptationEngine.CLASS_LIGHT -> {
                    val downscaled = max(1, (original * 0.33f).toInt())
                    val reason = "Smart Adaptation: Reduced to $downscaled ${habit.targetUnit} based on your ${checkIn.moodLabel} mood and ${energy.displayName} energy."
                    Triple(downscaled, downscaled < original, reason)
                }
                HabitAdaptationEngine.CLASS_MODERATE -> {
                    val downscaled = max(1, (original * 0.5f).toInt())
                    val reason = "Smart Adaptation: Adjusted to $downscaled ${habit.targetUnit} to match your daily capacity."
                    Triple(downscaled, downscaled < original, reason)
                }
                else -> {
                    Triple(original, false, null)
                }
            }
        }

        // Rule-based fallback if ML engine is not provided
        if (mood <= 2 || energy == EnergyLevel.LOW) {
            val downscaled = max(1, (original * 0.33f).toInt())
            if (downscaled < original) {
                val reason = "Since you're feeling '${checkIn.moodLabel}', we reduced this from $original ${habit.targetUnit} to keep it manageable today."
                return Triple(downscaled, true, reason)
            }
        } else if (mood == 3) {
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
