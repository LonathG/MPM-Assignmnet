package com.mindscape.app.domain.model

data class DailyCheckIn(
    val id: Long = 0,
    val date: String,
    val moodScore: Int, // 1=Stormy, 2=Rainy, 3=Okay, 4=Sunny, 5=Radiant
    val moodLabel: String,
    val energyLevel: EnergyLevel,
    val contributingFactors: List<String>,
    val quickNote: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
