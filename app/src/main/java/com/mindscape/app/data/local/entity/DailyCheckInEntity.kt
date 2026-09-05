package com.mindscape.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_check_ins")
data class DailyCheckInEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String, // "yyyy-MM-dd"
    val moodScore: Int, // 1 to 5
    val moodLabel: String, // "Stormy", "Rainy", "Okay", "Sunny", "Radiant"
    val energyLevel: String, // "Low", "Medium", "High"
    val contributingFactorsJson: String, // JSON string list of factors e.g. ["Sleep", "Solitude"]
    val quickNote: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
