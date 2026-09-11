package com.mindscape.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
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
