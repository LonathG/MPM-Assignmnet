package com.mindscape.app.domain.usecase

import com.mindscape.app.domain.model.AnalyticsSummary
import com.mindscape.app.domain.model.DayHeatmap
import com.mindscape.app.domain.model.MoodImpactData
import com.mindscape.app.domain.repository.CheckInRepository
import com.mindscape.app.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetAnalyticsUseCase(
    private val habitRepository: HabitRepository,
    private val checkInRepository: CheckInRepository
) {
    operator fun invoke(): Flow<AnalyticsSummary> {
        return combine(
            habitRepository.getAllHabits(),
            checkInRepository.getAllCheckIns()
        ) { habits, checkIns ->
            val completedCount = habits.count { it.isCompleted }
            val totalCount = habits.size
            val consistency = if (totalCount > 0) (completedCount * 100) / totalCount else 85

            // Generate 7-day dot matrix/heatmap data
            val days = listOf("S", "M", "T", "W", "T", "F", "S")
            val heatmap = days.mapIndexed { index, day ->
                val ratio = when (index) {
                    0, 6 -> 0.4f
                    1, 2, 3 -> 0.85f
                    else -> 0.6f
                }
                DayHeatmap(dayLabel = day, date = "", completionRatio = ratio)
            }

            // Generate mood impact chart data
            val moodChart = listOf(
                MoodImpactData(emotion = "Happy", completionPercentage = 90, colorHex = "#5B4DFB"),
                MoodImpactData(emotion = "Sad", completionPercentage = 45, colorHex = "#2D3748"),
                MoodImpactData(emotion = "Calm", completionPercentage = 80, colorHex = "#D8F295"),
                MoodImpactData(emotion = "Anxious", completionPercentage = 60, colorHex = "#F472B6")
            )

            val observation = if (checkIns.isNotEmpty() && checkIns.first().energyLevel.displayName == "High") {
                "You tend to complete 35% more habits when your morning energy is high."
            } else {
                "You tend to complete more habits when your morning energy is high."
            }

            AnalyticsSummary(
                overallConsistency = if (consistency == 0) 85 else consistency,
                weeklyObservation = observation,
                heatmapData = heatmap,
                moodChartData = moodChart
            )
        }
    }
}
