package com.mindscape.app.domain.engine

import com.mindscape.app.data.local.dao.HabitDao
import com.mindscape.app.data.local.entity.HabitEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Result data class returned after analyzing prompt and generating routine habits.
 */
data class RoutineGenerationResult(
    val intent: RoutineIntent,
    val summary: String,
    val createdHabits: List<HabitEntity>,
    val insertedCount: Int
)

/**
 * Predefined supported routine intents for offline keyword mapping.
 */
enum class RoutineIntent(val title: String, val description: String) {
    LOW_ENERGY(
        title = "Gentle Recovery Routine",
        description = "Light, restorative tasks tailored for low energy or burnout recovery."
    ),
    HIGH_FOCUS(
        title = "Deep Focus & Productivity Routine",
        description = "High-impact focus and learning habits for studying or work sprints."
    ),
    MINDFULNESS_CALM(
        title = "Stress Relief & Mindfulness Routine",
        description = "Calming exercises to relieve anxiety and build emotional resilience."
    ),
    FITNESS_ENERGY(
        title = "Active & Energy Boost Routine",
        description = "Physical movement and hydration habits to elevate physical vitality."
    ),
    EVENING_WIND_DOWN(
        title = "Restorative Sleep Routine",
        description = "Calming evening habits designed to improve sleep quality."
    ),
    BALANCED_DAILY(
        title = "Balanced Daily Wellness Routine",
        description = "A comprehensive mix of mindfulness, movement, and wellness."
    )
}

/**
 * Native Offline Smart Routine Generator (NLP Heuristics & Local DB Integration).
 *
 * Adheres strictly to the offline-first requirement:
 * 1. Analyzes user prompt using keyword-matching heuristics & token scoring.
 * 2. Maps prompt to a tailored set of habit templates.
 * 3. Persists generated [HabitEntity] items directly into Room via [HabitDao].
 */
class SmartRoutineEngine(
    private val habitDao: HabitDao
) {

    // Keyword dictionary mapped to specific routine intents
    private val keywordMap: Map<RoutineIntent, Set<String>> = mapOf(
        RoutineIntent.LOW_ENERGY to setOf(
            "low energy", "tired", "exhausted", "burnout", "fatigue", "lazy",
            "drained", "gentle", "easy", "recovery", "sick", "low battery", "sluggish"
        ),
        RoutineIntent.HIGH_FOCUS to setOf(
            "focus", "study", "work", "productive", "productivity", "exam",
            "deep work", "coding", "tasks", "deadline", "concentration", "discipline"
        ),
        RoutineIntent.MINDFULNESS_CALM to setOf(
            "stress", "anxious", "anxiety", "calm", "relax", "mindful", "peace",
            "meditate", "overwhelmed", "mental health", "breathe", "inner peace"
        ),
        RoutineIntent.FITNESS_ENERGY to setOf(
            "fitness", "workout", "gym", "exercise", "run", "running", "active",
            "energy boost", "muscle", "strength", "cardio", "stamina", "training"
        ),
        RoutineIntent.EVENING_WIND_DOWN to setOf(
            "sleep", "night", "bedtime", "insomnia", "evening", "wind down",
            "relaxing night", "unwind", "rest"
        ),
        RoutineIntent.BALANCED_DAILY to setOf(
            "balance", "balanced", "daily", "general", "healthy", "lifestyle",
            "routine", "starter", "normal", "all rounder"
        )
    )

    /**
     * Extracts the user's intent by analyzing keywords in the natural language text.
     *
     * @param prompt User's natural language input (e.g., "Create a daily routine for low energy")
     * @return Best matching [RoutineIntent] based on token occurrence and scoring.
     */
    fun detectIntent(prompt: String): RoutineIntent {
        val sanitized = prompt.trim().lowercase()
        if (sanitized.isEmpty()) return RoutineIntent.BALANCED_DAILY

        var highestScore = 0
        var bestIntent = RoutineIntent.BALANCED_DAILY

        for ((intent, keywords) in keywordMap) {
            var score = 0
            for (keyword in keywords) {
                if (keyword.contains(" ")) {
                    // Multi-word exact phrase match gets higher weight
                    if (sanitized.contains(keyword)) {
                        score += 3
                    }
                } else {
                    // Single-word match
                    val regex = Regex("\\b${Regex.escape(keyword)}\\b")
                    if (regex.containsMatchIn(sanitized)) {
                        score += 2
                    } else if (sanitized.contains(keyword)) {
                        score += 1
                    }
                }
            }

            if (score > highestScore) {
                highestScore = score
                bestIntent = intent
            }
        }

        return bestIntent
    }

    /**
     * Resolves a list of pre-configured [HabitEntity] templates for a given intent.
     */
    fun getPredefinedHabitsForIntent(intent: RoutineIntent): List<HabitEntity> {
        val timestamp = System.currentTimeMillis()
        return when (intent) {
            RoutineIntent.LOW_ENERGY -> listOf(
                HabitEntity(
                    id = 0,
                    name = "5-Min Box Breathing Reset",
                    category = "Mindfulness",
                    frequency = "Daily",
                    targetValue = 5,
                    targetUnit = "mins",
                    originalTargetValue = 5,
                    adaptationReason = "Balban et al. (2023): Cyclic 4-4-4-4 breathing rapidly reduces sympathetic stress & fatigue.",
                    createdAt = timestamp
                ),
                HabitEntity(
                    id = 0,
                    name = "Electrolyte & Warm Hydration",
                    category = "Wellness",
                    frequency = "Daily",
                    targetValue = 4,
                    targetUnit = "glasses",
                    originalTargetValue = 4,
                    adaptationReason = "Armstrong et al. (2012): Mild dehydration amplifies perceived exhaustion and brain fog.",
                    createdAt = timestamp
                ),
                HabitEntity(
                    id = 0,
                    name = "10-Min Restorative Gentle Stretch",
                    category = "Movement",
                    frequency = "Daily",
                    targetValue = 10,
                    targetUnit = "mins",
                    originalTargetValue = 10,
                    adaptationReason = "Behavioral Activation (CBT): Low-intensity micro-movement prevents depressive inactivity loops.",
                    createdAt = timestamp
                )
            )

            RoutineIntent.HIGH_FOCUS -> listOf(
                HabitEntity(
                    id = 0,
                    name = "25-Min Pomodoro Focus Block",
                    category = "Learning",
                    frequency = "Daily",
                    targetValue = 25,
                    targetUnit = "mins",
                    originalTargetValue = 25,
                    adaptationReason = "Ericsson (1993): Ultradian focus sprints maintain optimal working memory capacity.",
                    createdAt = timestamp
                ),
                HabitEntity(
                    id = 0,
                    name = "Daily Top 3 MIT Priority Plan",
                    category = "Learning",
                    frequency = "Daily",
                    targetValue = 1,
                    targetUnit = "plan",
                    originalTargetValue = 1,
                    adaptationReason = "Cognitive Load Theory (Sweller, 1988): Pre-committing to 3 tasks prevents decision fatigue.",
                    createdAt = timestamp
                ),
                HabitEntity(
                    id = 0,
                    name = "Active Reading & Synthesis",
                    category = "Learning",
                    frequency = "Daily",
                    targetValue = 15,
                    targetUnit = "pages",
                    originalTargetValue = 15,
                    adaptationReason = "Dunlosky et al. (2013): Active spaced learning boosts long-term conceptual retention.",
                    createdAt = timestamp
                )
            )

            RoutineIntent.MINDFULNESS_CALM -> listOf(
                HabitEntity(
                    id = 0,
                    name = "3-Item Gratitude Reflection",
                    category = "Mindfulness",
                    frequency = "Daily",
                    targetValue = 1,
                    targetUnit = "entry",
                    originalTargetValue = 1,
                    adaptationReason = "Emmons & McCullough (2003): Structured gratitude measurably lowers cortisol and boosts resilience.",
                    createdAt = timestamp
                ),
                HabitEntity(
                    id = 0,
                    name = "Mindful Sensory Nature Walk",
                    category = "Movement",
                    frequency = "Daily",
                    targetValue = 20,
                    targetUnit = "mins",
                    originalTargetValue = 20,
                    adaptationReason = "Bratman et al. (PNAS, 2015): 20 mins in natural surroundings decreases neural rumination.",
                    createdAt = timestamp
                ),
                HabitEntity(
                    id = 0,
                    name = "Evening Digital Wind-Down",
                    category = "Mindfulness",
                    frequency = "Daily",
                    targetValue = 15,
                    targetUnit = "mins",
                    originalTargetValue = 15,
                    adaptationReason = "Kabat-Zinn (1990): Mindful body scans relieve somatic anxiety before sleep.",
                    createdAt = timestamp
                )
            )

            RoutineIntent.FITNESS_ENERGY -> listOf(
                HabitEntity(
                    id = 0,
                    name = "Moderate Aerobic / HIIT Session",
                    category = "Movement",
                    frequency = "Daily",
                    targetValue = 30,
                    targetUnit = "mins",
                    originalTargetValue = 30,
                    adaptationReason = "WHO Physical Activity Guidelines (2020): 30 mins moderate movement stimulates BDNF neuroplasticity.",
                    createdAt = timestamp
                ),
                HabitEntity(
                    id = 0,
                    name = "Daily Hydration Target",
                    category = "Wellness",
                    frequency = "Daily",
                    targetValue = 8,
                    targetUnit = "glasses",
                    originalTargetValue = 8,
                    adaptationReason = "Popkin et al. (2010): Optimal fluid intake supports metabolic clearance and physical endurance.",
                    createdAt = timestamp
                ),
                HabitEntity(
                    id = 0,
                    name = "Post-Workout Dynamic Mobility",
                    category = "Movement",
                    frequency = "Daily",
                    targetValue = 15,
                    targetUnit = "mins",
                    originalTargetValue = 15,
                    adaptationReason = "Page (2012): Post-exercise mobility reduces delayed onset muscle soreness (DOMS).",
                    createdAt = timestamp
                )
            )

            RoutineIntent.EVENING_WIND_DOWN -> listOf(
                HabitEntity(
                    id = 0,
                    name = "Screen-Free Wind Down",
                    category = "Wellness",
                    frequency = "Daily",
                    targetValue = 30,
                    targetUnit = "mins",
                    originalTargetValue = 30,
                    adaptationReason = "Chang et al. (PNAS, 2015): 30+ min blue-light screen elimination restores nocturnal melatonin rhythm.",
                    createdAt = timestamp
                ),
                HabitEntity(
                    id = 0,
                    name = "Warm Herbal Tea & Reading",
                    category = "Mindfulness",
                    frequency = "Daily",
                    targetValue = 15,
                    targetUnit = "mins",
                    originalTargetValue = 15,
                    adaptationReason = "Stanford Sleep Medicine: Calming bedtime rituals lower basal core body temperature for REM sleep.",
                    createdAt = timestamp
                )
            )

            RoutineIntent.BALANCED_DAILY -> listOf(
                HabitEntity(
                    id = 0,
                    name = "Morning Sunlight & 20-min Walk",
                    category = "Movement",
                    frequency = "Daily",
                    targetValue = 20,
                    targetUnit = "mins",
                    originalTargetValue = 20,
                    adaptationReason = "Huberman (2021): Early daylight viewing triggers cortisol awakening response & elevates daytime mood.",
                    createdAt = timestamp
                ),
                HabitEntity(
                    id = 0,
                    name = "Daily 8-Glass Hydration Protocol",
                    category = "Wellness",
                    frequency = "Daily",
                    targetValue = 8,
                    targetUnit = "glasses",
                    originalTargetValue = 8,
                    adaptationReason = "Armstrong et al. (2012): Sustained hydration prevents afternoon cognitive fatigue.",
                    createdAt = timestamp
                ),
                HabitEntity(
                    id = 0,
                    name = "Box Breathing Reset",
                    category = "Mindfulness",
                    frequency = "Daily",
                    targetValue = 5,
                    targetUnit = "mins",
                    originalTargetValue = 5,
                    adaptationReason = "Balban et al. (2023): Fast parasympathetic vagal stimulation.",
                    createdAt = timestamp
                ),
                HabitEntity(
                    id = 0,
                    name = "Focused Learning & Reflection",
                    category = "Learning",
                    frequency = "Daily",
                    targetValue = 15,
                    targetUnit = "mins",
                    originalTargetValue = 15,
                    adaptationReason = "Ericsson (1993): Daily continuous micro-learning reinforces neural pathways.",
                    createdAt = timestamp
                )
            )
        }
    }

    /**
     * Executes the complete offline smart routine pipeline:
     * 1. Detects the intent from natural language input.
     * 2. Generates predefined habit entities.
     * 3. Inserts all habits directly into Room DB on IO dispatcher.
     *
     * @param prompt Natural language string provided by the user.
     * @return [RoutineGenerationResult] containing intent details and inserted habits.
     */
    suspend fun generateAndSaveRoutine(prompt: String): RoutineGenerationResult = withContext(Dispatchers.IO) {
        val detectedIntent = detectIntent(prompt)
        val habitEntities = getPredefinedHabitsForIntent(detectedIntent)

        // Insert into Room Database locally
        habitDao.insertHabits(habitEntities)

        val summary = "Generated ${habitEntities.size} habits for '${detectedIntent.title}': ${detectedIntent.description}"

        RoutineGenerationResult(
            intent = detectedIntent,
            summary = summary,
            createdHabits = habitEntities,
            insertedCount = habitEntities.size
        )
    }
}
