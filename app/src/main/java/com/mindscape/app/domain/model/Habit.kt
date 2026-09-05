package com.mindscape.app.domain.model

data class Habit(
    val id: Long = 0,
    val name: String,
    val category: HabitCategory,
    val frequency: String = "Daily",
    val targetValue: Int,
    val targetUnit: String,
    val currentValue: Int = 0,
    val isCompleted: Boolean = false,
    val isAdapted: Boolean = false,
    val originalTargetValue: Int = targetValue,
    val adaptationReason: String? = null,
    val isStarter: Boolean = false
)
