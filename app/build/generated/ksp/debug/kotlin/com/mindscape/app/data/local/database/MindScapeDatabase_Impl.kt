package com.mindscape.app.`data`.local.database

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.mindscape.app.`data`.local.dao.DailyCheckInDao
import com.mindscape.app.`data`.local.dao.DailyCheckInDao_Impl
import com.mindscape.app.`data`.local.dao.HabitDao
import com.mindscape.app.`data`.local.dao.HabitDao_Impl
import com.mindscape.app.`data`.local.dao.HabitLogDao
import com.mindscape.app.`data`.local.dao.HabitLogDao_Impl
import com.mindscape.app.`data`.local.dao.UserProfileDao
import com.mindscape.app.`data`.local.dao.UserProfileDao_Impl
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class MindScapeDatabase_Impl : MindScapeDatabase() {
  private val _habitDao: Lazy<HabitDao> = lazy {
    HabitDao_Impl(this)
  }

  private val _dailyCheckInDao: Lazy<DailyCheckInDao> = lazy {
    DailyCheckInDao_Impl(this)
  }

  private val _habitLogDao: Lazy<HabitLogDao> = lazy {
    HabitLogDao_Impl(this)
  }

  private val _userProfileDao: Lazy<UserProfileDao> = lazy {
    UserProfileDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1,
        "b6574d51202f20906c39b2d9ae388630", "eebcc88a922f1f0a83e44e2d667c2ae8") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `habits` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `category` TEXT NOT NULL, `frequency` TEXT NOT NULL, `targetValue` INTEGER NOT NULL, `targetUnit` TEXT NOT NULL, `currentValue` INTEGER NOT NULL, `isCompleted` INTEGER NOT NULL, `isAdapted` INTEGER NOT NULL, `originalTargetValue` INTEGER NOT NULL, `adaptationReason` TEXT, `isStarter` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `daily_check_ins` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `date` TEXT NOT NULL, `moodScore` INTEGER NOT NULL, `moodLabel` TEXT NOT NULL, `energyLevel` TEXT NOT NULL, `contributingFactorsJson` TEXT NOT NULL, `quickNote` TEXT, `timestamp` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `habit_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `habitId` INTEGER NOT NULL, `date` TEXT NOT NULL, `completedValue` INTEGER NOT NULL, `targetValue` INTEGER NOT NULL, `isCompleted` INTEGER NOT NULL, `isAdapted` INTEGER NOT NULL, `moodAtLog` TEXT, `timestamp` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `user_profile` (`id` INTEGER NOT NULL, `userName` TEXT NOT NULL, `email` TEXT NOT NULL, `primaryFocus` TEXT NOT NULL, `checkInTime` TEXT NOT NULL, `checkInFrequency` TEXT NOT NULL, `smartAdaptiveGoalsEnabled` INTEGER NOT NULL, `streakDays` INTEGER NOT NULL, `consistencyPercentage` INTEGER NOT NULL, `completedSessions` INTEGER NOT NULL, `level` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'b6574d51202f20906c39b2d9ae388630')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `habits`")
        connection.execSQL("DROP TABLE IF EXISTS `daily_check_ins`")
        connection.execSQL("DROP TABLE IF EXISTS `habit_logs`")
        connection.execSQL("DROP TABLE IF EXISTS `user_profile`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsHabits: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsHabits.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHabits.put("name", TableInfo.Column("name", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHabits.put("category", TableInfo.Column("category", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHabits.put("frequency", TableInfo.Column("frequency", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHabits.put("targetValue", TableInfo.Column("targetValue", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHabits.put("targetUnit", TableInfo.Column("targetUnit", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHabits.put("currentValue", TableInfo.Column("currentValue", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsHabits.put("isCompleted", TableInfo.Column("isCompleted", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHabits.put("isAdapted", TableInfo.Column("isAdapted", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHabits.put("originalTargetValue", TableInfo.Column("originalTargetValue", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsHabits.put("adaptationReason", TableInfo.Column("adaptationReason", "TEXT", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsHabits.put("isStarter", TableInfo.Column("isStarter", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHabits.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysHabits: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesHabits: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoHabits: TableInfo = TableInfo("habits", _columnsHabits, _foreignKeysHabits,
            _indicesHabits)
        val _existingHabits: TableInfo = read(connection, "habits")
        if (!_infoHabits.equals(_existingHabits)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |habits(com.mindscape.app.data.local.entity.HabitEntity).
              | Expected:
              |""".trimMargin() + _infoHabits + """
              |
              | Found:
              |""".trimMargin() + _existingHabits)
        }
        val _columnsDailyCheckIns: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsDailyCheckIns.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDailyCheckIns.put("date", TableInfo.Column("date", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDailyCheckIns.put("moodScore", TableInfo.Column("moodScore", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDailyCheckIns.put("moodLabel", TableInfo.Column("moodLabel", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDailyCheckIns.put("energyLevel", TableInfo.Column("energyLevel", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDailyCheckIns.put("contributingFactorsJson",
            TableInfo.Column("contributingFactorsJson", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDailyCheckIns.put("quickNote", TableInfo.Column("quickNote", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDailyCheckIns.put("timestamp", TableInfo.Column("timestamp", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysDailyCheckIns: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesDailyCheckIns: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoDailyCheckIns: TableInfo = TableInfo("daily_check_ins", _columnsDailyCheckIns,
            _foreignKeysDailyCheckIns, _indicesDailyCheckIns)
        val _existingDailyCheckIns: TableInfo = read(connection, "daily_check_ins")
        if (!_infoDailyCheckIns.equals(_existingDailyCheckIns)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |daily_check_ins(com.mindscape.app.data.local.entity.DailyCheckInEntity).
              | Expected:
              |""".trimMargin() + _infoDailyCheckIns + """
              |
              | Found:
              |""".trimMargin() + _existingDailyCheckIns)
        }
        val _columnsHabitLogs: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsHabitLogs.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHabitLogs.put("habitId", TableInfo.Column("habitId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHabitLogs.put("date", TableInfo.Column("date", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHabitLogs.put("completedValue", TableInfo.Column("completedValue", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsHabitLogs.put("targetValue", TableInfo.Column("targetValue", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsHabitLogs.put("isCompleted", TableInfo.Column("isCompleted", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsHabitLogs.put("isAdapted", TableInfo.Column("isAdapted", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHabitLogs.put("moodAtLog", TableInfo.Column("moodAtLog", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHabitLogs.put("timestamp", TableInfo.Column("timestamp", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysHabitLogs: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesHabitLogs: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoHabitLogs: TableInfo = TableInfo("habit_logs", _columnsHabitLogs,
            _foreignKeysHabitLogs, _indicesHabitLogs)
        val _existingHabitLogs: TableInfo = read(connection, "habit_logs")
        if (!_infoHabitLogs.equals(_existingHabitLogs)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |habit_logs(com.mindscape.app.data.local.entity.HabitLogEntity).
              | Expected:
              |""".trimMargin() + _infoHabitLogs + """
              |
              | Found:
              |""".trimMargin() + _existingHabitLogs)
        }
        val _columnsUserProfile: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsUserProfile.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("userName", TableInfo.Column("userName", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("email", TableInfo.Column("email", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("primaryFocus", TableInfo.Column("primaryFocus", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("checkInTime", TableInfo.Column("checkInTime", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("checkInFrequency", TableInfo.Column("checkInFrequency", "TEXT",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("smartAdaptiveGoalsEnabled",
            TableInfo.Column("smartAdaptiveGoalsEnabled", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("streakDays", TableInfo.Column("streakDays", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("consistencyPercentage", TableInfo.Column("consistencyPercentage",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("completedSessions", TableInfo.Column("completedSessions",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("level", TableInfo.Column("level", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysUserProfile: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesUserProfile: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoUserProfile: TableInfo = TableInfo("user_profile", _columnsUserProfile,
            _foreignKeysUserProfile, _indicesUserProfile)
        val _existingUserProfile: TableInfo = read(connection, "user_profile")
        if (!_infoUserProfile.equals(_existingUserProfile)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |user_profile(com.mindscape.app.data.local.entity.UserProfileEntity).
              | Expected:
              |""".trimMargin() + _infoUserProfile + """
              |
              | Found:
              |""".trimMargin() + _existingUserProfile)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "habits", "daily_check_ins",
        "habit_logs", "user_profile")
  }

  public override fun clearAllTables() {
    super.performClear(false, "habits", "daily_check_ins", "habit_logs", "user_profile")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(HabitDao::class, HabitDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(DailyCheckInDao::class, DailyCheckInDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(HabitLogDao::class, HabitLogDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(UserProfileDao::class, UserProfileDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun habitDao(): HabitDao = _habitDao.value

  public override fun dailyCheckInDao(): DailyCheckInDao = _dailyCheckInDao.value

  public override fun habitLogDao(): HabitLogDao = _habitLogDao.value

  public override fun userProfileDao(): UserProfileDao = _userProfileDao.value
}
