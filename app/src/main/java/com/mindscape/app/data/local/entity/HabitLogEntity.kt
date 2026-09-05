package com.mindscape.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habit_logs")
data class HabitLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val habitId: Long,
    val date: String, // "yyyy-MM-dd"
    val completedValue: Int,
    val targetValue: Int,
    val isCompleted: Boolean,
    val isAdapted: Boolean = false,
    val moodAtLog: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
