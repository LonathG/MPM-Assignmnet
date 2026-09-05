package com.mindscape.app.domain.model

data class DayHeatmap(
    val dayLabel: String, // "S", "M", "T", "W", "T", "F", "S"
    val date: String,
    val completionRatio: Float // 0.0 to 1.0
)

data class MoodImpactData(
    val emotion: String, // "Happy", "Sad", "Calm", "Anxious"
    val completionPercentage: Int, // 0 to 100
    val colorHex: String
)

data class AnalyticsSummary(
    val overallConsistency: Int = 85,
    val weeklyObservation: String = "You tend to complete more habits when your morning energy is high.",
    val heatmapData: List<DayHeatmap> = emptyList(),
    val moodChartData: List<MoodImpactData> = emptyList()
)
