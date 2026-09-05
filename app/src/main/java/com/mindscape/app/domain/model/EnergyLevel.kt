package com.mindscape.app.domain.model

enum class EnergyLevel(val displayName: String) {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");

    companion object {
        fun fromString(value: String): EnergyLevel {
            return entries.find { it.displayName.equals(value, ignoreCase = true) }
                ?: MEDIUM
        }
    }
}
