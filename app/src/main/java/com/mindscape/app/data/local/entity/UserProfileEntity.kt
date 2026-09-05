package com.mindscape.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
    val id: Int = 1,
    val userName: String = "Lonath G",
    val email: String = "lonath@example.com",
    val primaryFocus: String = "Mindfulness",
    val checkInTime: String = "08:30 AM",
    val checkInFrequency: String = "Everyday",
    val smartAdaptiveGoalsEnabled: Boolean = true,
    val streakDays: Int = 14,
    val consistencyPercentage: Int = 85,
    val completedSessions: Int = 42,
    val level: Int = 3
)
