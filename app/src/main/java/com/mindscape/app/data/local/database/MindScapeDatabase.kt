package com.mindscape.app.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mindscape.app.data.local.dao.DailyCheckInDao
import com.mindscape.app.data.local.dao.HabitDao
import com.mindscape.app.data.local.dao.HabitLogDao
import com.mindscape.app.data.local.dao.UserProfileDao
import com.mindscape.app.data.local.entity.DailyCheckInEntity
import com.mindscape.app.data.local.entity.HabitEntity
import com.mindscape.app.data.local.entity.HabitLogEntity
import com.mindscape.app.data.local.entity.UserProfileEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        HabitEntity::class,
        DailyCheckInEntity::class,
        HabitLogEntity::class,
        UserProfileEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class MindScapeDatabase : RoomDatabase() {

    abstract fun habitDao(): HabitDao
    abstract fun dailyCheckInDao(): DailyCheckInDao
    abstract fun habitLogDao(): HabitLogDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: MindScapeDatabase? = null

        fun getDatabase(context: Context): MindScapeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MindScapeDatabase::class.java,
                    "mindscape_database"
                )
                    .addCallback(DatabaseCallback(context))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val context: Context
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database)
                    }
                }
            }

            private suspend fun populateInitialData(database: MindScapeDatabase) {
                // Evidence-Based Initial Starter Habits (Clinical & Behavioral Neuroscience Grounded)
                val initialHabits = listOf(
                    HabitEntity(
                        id = 1,
                        name = "Daily Mind-Body Hydration",
                        category = "Wellness",
                        frequency = "Daily",
                        targetValue = 8,
                        targetUnit = "glasses",
                        currentValue = 8,
                        isCompleted = true,
                        adaptationReason = "Armstrong et al. (2012): Hydration sustains cognitive stamina and positive mood.",
                        isStarter = true
                    ),
                    HabitEntity(
                        id = 2,
                        name = "Morning Sunlight & 20-min Walk",
                        category = "Movement",
                        frequency = "Daily",
                        targetValue = 20,
                        targetUnit = "mins",
                        currentValue = 0,
                        isCompleted = false,
                        originalTargetValue = 20,
                        adaptationReason = "Stanford Neuroscience (2021): Early light sets circadian rhythm and enhances daytime focus.",
                        isStarter = true
                    ),
                    HabitEntity(
                        id = 3,
                        name = "Box Breathing & Vagal Reset",
                        category = "Mindfulness",
                        frequency = "Daily",
                        targetValue = 5,
                        targetUnit = "mins",
                        currentValue = 0,
                        isCompleted = false,
                        originalTargetValue = 5,
                        adaptationReason = "Balban et al. (2023): Cyclic box breathing rapidly engages the parasympathetic calming response.",
                        isStarter = true
                    ),
                    HabitEntity(
                        id = 4,
                        name = "25-Min Deep Focus Sprint",
                        category = "Learning",
                        frequency = "Daily",
                        targetValue = 25,
                        targetUnit = "mins",
                        currentValue = 0,
                        isCompleted = false,
                        originalTargetValue = 25,
                        adaptationReason = "Deliberate Practice Protocol (Ericsson, 1993): Structured sprints optimize working memory without burnout.",
                        isStarter = true
                    )
                )
                database.habitDao().insertHabits(initialHabits)

                // Initial Profile (Onboarding not yet completed for new users)
                database.userProfileDao().insertOrUpdateProfile(
                    UserProfileEntity(
                        id = 1,
                        userName = "",
                        email = "",
                        primaryFocus = "Mindfulness",
                        checkInTime = "08:30 AM",
                        checkInFrequency = "Everyday",
                        smartAdaptiveGoalsEnabled = true,
                        isOnboardingCompleted = false,
                        streakDays = 0,
                        consistencyPercentage = 100,
                        completedSessions = 0,
                        level = 1
                    )
                )
            }
        }
    }
}
