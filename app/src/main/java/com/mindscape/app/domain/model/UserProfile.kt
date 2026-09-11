package com.mindscape.app.domain.model

data class UserProfile(
    val id: Int = 1,
    val userName: String = "",
    val email: String = "",
    val primaryFocus: String = "Mindfulness",
    val checkInTime: String = "08:30 AM",
    val checkInFrequency: String = "Everyday",
    val smartAdaptiveGoalsEnabled: Boolean = true,
    val isOnboardingCompleted: Boolean = false,
    val streakDays: Int = 0,
    val consistencyPercentage: Int = 100,
    val completedSessions: Int = 0,
    val level: Int = 1
)
