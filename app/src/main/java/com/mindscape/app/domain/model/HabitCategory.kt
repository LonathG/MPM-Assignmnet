package com.mindscape.app.domain.model

enum class HabitCategory(val displayName: String) {
    MINDFULNESS("Mindfulness"),
    MOVEMENT("Movement"),
    WELLNESS("Wellness"),
    LEARNING("Learning");

    companion object {
        fun fromString(value: String): HabitCategory {
            return entries.find { it.displayName.equals(value, ignoreCase = true) }
                ?: MINDFULNESS
        }
    }
}
