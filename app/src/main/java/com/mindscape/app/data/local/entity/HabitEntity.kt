package com.mindscape.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: String, // "Mindfulness", "Movement", "Wellness", "Learning"
    val frequency: String, // "Daily", "Weekdays Only", "Custom"
    val targetValue: Int, // e.g. 30 (mins) or 8 (glasses)
    val targetUnit: String, // "mins", "pages", "glasses"
    val currentValue: Int = 0,
    val isCompleted: Boolean = false,
    val isAdapted: Boolean = false,
    val originalTargetValue: Int = targetValue,
    val adaptationReason: String? = null,
    val isStarter: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
