package com.mindscape.app.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.mindscape.app.data.local.database.MindScapeDatabase
import com.mindscape.app.data.local.entity.DailyCheckInEntity
import com.mindscape.app.domain.engine.HabitAdaptationEngine
import com.mindscape.app.domain.engine.SmartAdaptationEngine
import com.mindscape.app.domain.model.DailyCheckIn
import com.mindscape.app.domain.model.EnergyLevel
import com.mindscape.app.domain.model.Habit
import com.mindscape.app.domain.model.HabitCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Offline BroadcastReceiver for handling interactive tap events from the Rich Notification (FR-02 & FR-05).
 * Directly persists selected Mood and Physical Energy to the local Room Database without opening the app Activity.
 */
class CheckInNotificationReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "CheckInNotifReceiver"

        const val ACTION_SELECT_MOOD = "com.mindscape.app.ACTION_SELECT_MOOD"
        const val ACTION_SELECT_ENERGY = "com.mindscape.app.ACTION_SELECT_ENERGY"
        const val ACTION_LOG_CHECKIN = "com.mindscape.app.ACTION_LOG_CHECKIN"
        const val ACTION_DISMISS = "com.mindscape.app.ACTION_DISMISS_NOTIFICATION"
        const val ACTION_MIC_TAP = "com.mindscape.app.ACTION_MIC_TAP"

        const val EXTRA_MOOD_SCORE = "extra_mood_score"
        const val EXTRA_ENERGY_LEVEL = "extra_energy_level"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        val currentMood = intent.getIntExtra(EXTRA_MOOD_SCORE, 4)
        val currentEnergy = intent.getIntExtra(EXTRA_ENERGY_LEVEL, 4)

        when (action) {
            ACTION_SELECT_MOOD -> {
                // Instantly re-render notification with the selected mood state
                CheckInNotificationManager.showCheckInNotification(
                    context = context,
                    selectedMood = currentMood,
                    selectedEnergy = currentEnergy
                )
            }

            ACTION_SELECT_ENERGY -> {
                // Instantly re-render notification with the selected energy level
                CheckInNotificationManager.showCheckInNotification(
                    context = context,
                    selectedMood = currentMood,
                    selectedEnergy = currentEnergy
                )
            }

            ACTION_DISMISS -> {
                CheckInNotificationManager.cancelNotification(context)
            }

            ACTION_MIC_TAP -> {
                Toast.makeText(context, "Voice Check-In: Speak your mood note", Toast.LENGTH_SHORT).show()
            }

            ACTION_LOG_CHECKIN -> {
                // Execute asynchronous local database persistence (FR-05)
                val pendingResult = goAsync()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        saveCheckInLocally(context, currentMood, currentEnergy)
                        // Dismiss notification upon successful logging
                        CheckInNotificationManager.cancelNotification(context)
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to log check-in offline from notification", e)
                    } finally {
                        pendingResult.finish()
                    }
                }
            }
        }
    }

    /**
     * Persists the check-in entity to Room Database and executes on-device habit adaptation.
     */
    private suspend fun saveCheckInLocally(context: Context, moodScore: Int, energyLevelInt: Int) {
        val database = MindScapeDatabase.getDatabase(context)
        val todayStr = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

        val moodLabel = CheckInNotificationManager.MOOD_LABELS[moodScore] ?: "Good"
        val energyDomainEnum = when (energyLevelInt) {
            1, 2 -> EnergyLevel.LOW
            3 -> EnergyLevel.MEDIUM
            else -> EnergyLevel.HIGH
        }

        val checkInEntity = DailyCheckInEntity(
            date = todayStr,
            moodScore = moodScore,
            moodLabel = moodLabel,
            energyLevel = energyDomainEnum.displayName,
            contributingFactorsJson = "[\"Notification Quick-Log\"]",
            quickNote = "Logged directly via Rich Notification (${CheckInNotificationManager.ENERGY_LABELS[energyLevelInt]})",
            timestamp = System.currentTimeMillis()
        )

        // 1. Insert into Room Database locally (FR-05)
        database.dailyCheckInDao().insertCheckIn(checkInEntity)
        Log.d(TAG, "Successfully saved DailyCheckIn to Room DB: $moodLabel (Energy: ${energyDomainEnum.displayName})")

        // 2. Perform offline Smart Habit Adaptation if user enabled adaptive goals
        try {
            val profile = database.userProfileDao().getUserProfile().firstOrNull()
            val adaptiveGoalsEnabled = profile?.smartAdaptiveGoalsEnabled ?: true

            if (adaptiveGoalsEnabled) {
                val habits = database.habitDao().getAllHabits().firstOrNull() ?: emptyList()
                if (habits.isNotEmpty()) {
                    val domainHabits = habits.map { entity ->
                        Habit(
                            id = entity.id,
                            name = entity.name,
                            category = HabitCategory.fromString(entity.category),
                            frequency = entity.frequency,
                            targetValue = entity.targetValue,
                            targetUnit = entity.targetUnit,
                            currentValue = entity.currentValue,
                            isCompleted = entity.isCompleted,
                            isAdapted = entity.isAdapted,
                            originalTargetValue = entity.originalTargetValue,
                            adaptationReason = entity.adaptationReason,
                            isStarter = entity.isStarter
                        )
                    }

                    val domainCheckIn = DailyCheckIn(
                        id = 0,
                        date = todayStr,
                        moodScore = moodScore,
                        moodLabel = moodLabel,
                        energyLevel = energyDomainEnum,
                        contributingFactors = listOf("Notification Quick-Log"),
                        quickNote = checkInEntity.quickNote,
                        timestamp = checkInEntity.timestamp
                    )

                    val mlEngine = HabitAdaptationEngine(context)
                    val adaptationEngine = SmartAdaptationEngine(mlEngine)
                    val adaptationResult = adaptationEngine.adaptHabitsForCheckIn(
                        habits = domainHabits,
                        checkIn = domainCheckIn,
                        smartAdaptiveGoalsEnabled = true
                    )

                    // Update adapted targets in Room
                    adaptationResult.adaptedHabits.forEach { adaptedHabit ->
                        database.habitDao().updateHabitTarget(
                            habitId = adaptedHabit.id,
                            targetValue = adaptedHabit.targetValue,
                            isAdapted = adaptedHabit.isAdapted,
                            reason = adaptedHabit.adaptationReason
                        )
                    }
                    Log.d(TAG, "Habits successfully adapted in background: ${adaptationResult.adjustedHabitCount} habits adjusted.")
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Non-critical: Habit adaptation after notification check-in skipped", e)
        }
    }
}
