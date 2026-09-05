package com.mindscape.app.`data`.local.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.mindscape.app.`data`.local.entity.UserProfileEntity
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class UserProfileDao_Impl(
  __db: RoomDatabase,
) : UserProfileDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfUserProfileEntity: EntityInsertAdapter<UserProfileEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfUserProfileEntity = object : EntityInsertAdapter<UserProfileEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `user_profile` (`id`,`userName`,`email`,`primaryFocus`,`checkInTime`,`checkInFrequency`,`smartAdaptiveGoalsEnabled`,`streakDays`,`consistencyPercentage`,`completedSessions`,`level`) VALUES (?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: UserProfileEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.userName)
        statement.bindText(3, entity.email)
        statement.bindText(4, entity.primaryFocus)
        statement.bindText(5, entity.checkInTime)
        statement.bindText(6, entity.checkInFrequency)
        val _tmp: Int = if (entity.smartAdaptiveGoalsEnabled) 1 else 0
        statement.bindLong(7, _tmp.toLong())
        statement.bindLong(8, entity.streakDays.toLong())
        statement.bindLong(9, entity.consistencyPercentage.toLong())
        statement.bindLong(10, entity.completedSessions.toLong())
        statement.bindLong(11, entity.level.toLong())
      }
    }
  }

  public override suspend fun insertOrUpdateProfile(profile: UserProfileEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfUserProfileEntity.insert(_connection, profile)
  }

  public override fun getUserProfile(): Flow<UserProfileEntity?> {
    val _sql: String = "SELECT * FROM user_profile WHERE id = 1 LIMIT 1"
    return createFlow(__db, false, arrayOf("user_profile")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfUserName: Int = getColumnIndexOrThrow(_stmt, "userName")
        val _columnIndexOfEmail: Int = getColumnIndexOrThrow(_stmt, "email")
        val _columnIndexOfPrimaryFocus: Int = getColumnIndexOrThrow(_stmt, "primaryFocus")
        val _columnIndexOfCheckInTime: Int = getColumnIndexOrThrow(_stmt, "checkInTime")
        val _columnIndexOfCheckInFrequency: Int = getColumnIndexOrThrow(_stmt, "checkInFrequency")
        val _columnIndexOfSmartAdaptiveGoalsEnabled: Int = getColumnIndexOrThrow(_stmt,
            "smartAdaptiveGoalsEnabled")
        val _columnIndexOfStreakDays: Int = getColumnIndexOrThrow(_stmt, "streakDays")
        val _columnIndexOfConsistencyPercentage: Int = getColumnIndexOrThrow(_stmt,
            "consistencyPercentage")
        val _columnIndexOfCompletedSessions: Int = getColumnIndexOrThrow(_stmt, "completedSessions")
        val _columnIndexOfLevel: Int = getColumnIndexOrThrow(_stmt, "level")
        val _result: UserProfileEntity?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpUserName: String
          _tmpUserName = _stmt.getText(_columnIndexOfUserName)
          val _tmpEmail: String
          _tmpEmail = _stmt.getText(_columnIndexOfEmail)
          val _tmpPrimaryFocus: String
          _tmpPrimaryFocus = _stmt.getText(_columnIndexOfPrimaryFocus)
          val _tmpCheckInTime: String
          _tmpCheckInTime = _stmt.getText(_columnIndexOfCheckInTime)
          val _tmpCheckInFrequency: String
          _tmpCheckInFrequency = _stmt.getText(_columnIndexOfCheckInFrequency)
          val _tmpSmartAdaptiveGoalsEnabled: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfSmartAdaptiveGoalsEnabled).toInt()
          _tmpSmartAdaptiveGoalsEnabled = _tmp != 0
          val _tmpStreakDays: Int
          _tmpStreakDays = _stmt.getLong(_columnIndexOfStreakDays).toInt()
          val _tmpConsistencyPercentage: Int
          _tmpConsistencyPercentage = _stmt.getLong(_columnIndexOfConsistencyPercentage).toInt()
          val _tmpCompletedSessions: Int
          _tmpCompletedSessions = _stmt.getLong(_columnIndexOfCompletedSessions).toInt()
          val _tmpLevel: Int
          _tmpLevel = _stmt.getLong(_columnIndexOfLevel).toInt()
          _result =
              UserProfileEntity(_tmpId,_tmpUserName,_tmpEmail,_tmpPrimaryFocus,_tmpCheckInTime,_tmpCheckInFrequency,_tmpSmartAdaptiveGoalsEnabled,_tmpStreakDays,_tmpConsistencyPercentage,_tmpCompletedSessions,_tmpLevel)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun setSmartAdaptiveGoalsEnabled(enabled: Boolean) {
    val _sql: String = "UPDATE user_profile SET smartAdaptiveGoalsEnabled = ? WHERE id = 1"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: Int = if (enabled) 1 else 0
        _stmt.bindLong(_argIndex, _tmp.toLong())
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateCheckInTime(time: String) {
    val _sql: String = "UPDATE user_profile SET checkInTime = ? WHERE id = 1"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, time)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
