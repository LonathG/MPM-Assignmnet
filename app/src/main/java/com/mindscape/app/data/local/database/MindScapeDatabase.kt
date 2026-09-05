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
    version = 1,
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
                // Initial Starter Habits
                val initialHabits = listOf(
                    HabitEntity(
                        id = 1,
                        name = "Drink 8 glasses of water",
                        category = "Wellness",
                        frequency = "Daily",
                        targetValue = 8,
                        targetUnit = "glasses",
                        currentValue = 8,
                        isCompleted = true,
                        isStarter = true
                    ),
                    HabitEntity(
                        id = 2,
                        name = "Take a 30 min mindful walk",
                        category = "Movement",
                        frequency = "Daily",
                        targetValue = 30,
                        targetUnit = "mins",
                        currentValue = 0,
                        isCompleted = false,
                        originalTargetValue = 30,
                        isStarter = true
                    ),
                    HabitEntity(
                        id = 3,
                        name = "Read 10 pages",
                        category = "Learning",
                        frequency = "Daily",
                        targetValue = 10,
                        targetUnit = "pages",
                        currentValue = 0,
                        isCompleted = false,
                        isStarter = true
                    ),
                    HabitEntity(
                        id = 4,
                        name = "Morning Meditation",
                        category = "Mindfulness",
                        frequency = "Daily",
                        targetValue = 15,
                        targetUnit = "mins",
                        currentValue = 9,
                        isCompleted = false,
                        isStarter = true
                    )
                )
                database.habitDao().insertHabits(initialHabits)

                // Initial Profile
                database.userProfileDao().insertOrUpdateProfile(
                    UserProfileEntity(
                        id = 1,
                        userName = "Lonath G",
                        email = "lonath@example.com",
                        primaryFocus = "Mindfulness",
                        checkInTime = "08:30 AM",
                        checkInFrequency = "Everyday",
                        smartAdaptiveGoalsEnabled = true,
                        streakDays = 14,
                        consistencyPercentage = 85,
                        completedSessions = 42,
                        level = 3
                    )
                )
            }
        }
    }
}
