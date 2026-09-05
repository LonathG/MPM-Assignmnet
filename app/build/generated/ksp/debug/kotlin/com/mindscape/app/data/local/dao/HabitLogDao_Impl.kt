package com.mindscape.app.`data`.local.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.mindscape.app.`data`.local.entity.HabitLogEntity
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class HabitLogDao_Impl(
  __db: RoomDatabase,
) : HabitLogDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfHabitLogEntity: EntityInsertAdapter<HabitLogEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfHabitLogEntity = object : EntityInsertAdapter<HabitLogEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `habit_logs` (`id`,`habitId`,`date`,`completedValue`,`targetValue`,`isCompleted`,`isAdapted`,`moodAtLog`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: HabitLogEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.habitId)
        statement.bindText(3, entity.date)
        statement.bindLong(4, entity.completedValue.toLong())
        statement.bindLong(5, entity.targetValue.toLong())
        val _tmp: Int = if (entity.isCompleted) 1 else 0
        statement.bindLong(6, _tmp.toLong())
        val _tmp_1: Int = if (entity.isAdapted) 1 else 0
        statement.bindLong(7, _tmp_1.toLong())
        val _tmpMoodAtLog: String? = entity.moodAtLog
        if (_tmpMoodAtLog == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpMoodAtLog)
        }
        statement.bindLong(9, entity.timestamp)
      }
    }
  }

  public override suspend fun insertOrUpdateLog(log: HabitLogEntity): Long = performSuspending(__db,
      false, true) { _connection ->
    val _result: Long = __insertAdapterOfHabitLogEntity.insertAndReturnId(_connection, log)
    _result
  }

  public override fun getLogsForDate(date: String): Flow<List<HabitLogEntity>> {
    val _sql: String = "SELECT * FROM habit_logs WHERE date = ?"
    return createFlow(__db, false, arrayOf("habit_logs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, date)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfHabitId: Int = getColumnIndexOrThrow(_stmt, "habitId")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfCompletedValue: Int = getColumnIndexOrThrow(_stmt, "completedValue")
        val _columnIndexOfTargetValue: Int = getColumnIndexOrThrow(_stmt, "targetValue")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfIsAdapted: Int = getColumnIndexOrThrow(_stmt, "isAdapted")
        val _columnIndexOfMoodAtLog: Int = getColumnIndexOrThrow(_stmt, "moodAtLog")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _result: MutableList<HabitLogEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: HabitLogEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpHabitId: Long
          _tmpHabitId = _stmt.getLong(_columnIndexOfHabitId)
          val _tmpDate: String
          _tmpDate = _stmt.getText(_columnIndexOfDate)
          val _tmpCompletedValue: Int
          _tmpCompletedValue = _stmt.getLong(_columnIndexOfCompletedValue).toInt()
          val _tmpTargetValue: Int
          _tmpTargetValue = _stmt.getLong(_columnIndexOfTargetValue).toInt()
          val _tmpIsCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp != 0
          val _tmpIsAdapted: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsAdapted).toInt()
          _tmpIsAdapted = _tmp_1 != 0
          val _tmpMoodAtLog: String?
          if (_stmt.isNull(_columnIndexOfMoodAtLog)) {
            _tmpMoodAtLog = null
          } else {
            _tmpMoodAtLog = _stmt.getText(_columnIndexOfMoodAtLog)
          }
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          _item =
              HabitLogEntity(_tmpId,_tmpHabitId,_tmpDate,_tmpCompletedValue,_tmpTargetValue,_tmpIsCompleted,_tmpIsAdapted,_tmpMoodAtLog,_tmpTimestamp)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getLogsBetweenDates(startDate: String, endDate: String):
      Flow<List<HabitLogEntity>> {
    val _sql: String = "SELECT * FROM habit_logs WHERE date BETWEEN ? AND ?"
    return createFlow(__db, false, arrayOf("habit_logs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, startDate)
        _argIndex = 2
        _stmt.bindText(_argIndex, endDate)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfHabitId: Int = getColumnIndexOrThrow(_stmt, "habitId")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfCompletedValue: Int = getColumnIndexOrThrow(_stmt, "completedValue")
        val _columnIndexOfTargetValue: Int = getColumnIndexOrThrow(_stmt, "targetValue")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfIsAdapted: Int = getColumnIndexOrThrow(_stmt, "isAdapted")
        val _columnIndexOfMoodAtLog: Int = getColumnIndexOrThrow(_stmt, "moodAtLog")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _result: MutableList<HabitLogEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: HabitLogEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpHabitId: Long
          _tmpHabitId = _stmt.getLong(_columnIndexOfHabitId)
          val _tmpDate: String
          _tmpDate = _stmt.getText(_columnIndexOfDate)
          val _tmpCompletedValue: Int
          _tmpCompletedValue = _stmt.getLong(_columnIndexOfCompletedValue).toInt()
          val _tmpTargetValue: Int
          _tmpTargetValue = _stmt.getLong(_columnIndexOfTargetValue).toInt()
          val _tmpIsCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp != 0
          val _tmpIsAdapted: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsAdapted).toInt()
          _tmpIsAdapted = _tmp_1 != 0
          val _tmpMoodAtLog: String?
          if (_stmt.isNull(_columnIndexOfMoodAtLog)) {
            _tmpMoodAtLog = null
          } else {
            _tmpMoodAtLog = _stmt.getText(_columnIndexOfMoodAtLog)
          }
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          _item =
              HabitLogEntity(_tmpId,_tmpHabitId,_tmpDate,_tmpCompletedValue,_tmpTargetValue,_tmpIsCompleted,_tmpIsAdapted,_tmpMoodAtLog,_tmpTimestamp)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAllLogs(): Flow<List<HabitLogEntity>> {
    val _sql: String = "SELECT * FROM habit_logs ORDER BY timestamp DESC"
    return createFlow(__db, false, arrayOf("habit_logs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfHabitId: Int = getColumnIndexOrThrow(_stmt, "habitId")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfCompletedValue: Int = getColumnIndexOrThrow(_stmt, "completedValue")
        val _columnIndexOfTargetValue: Int = getColumnIndexOrThrow(_stmt, "targetValue")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfIsAdapted: Int = getColumnIndexOrThrow(_stmt, "isAdapted")
        val _columnIndexOfMoodAtLog: Int = getColumnIndexOrThrow(_stmt, "moodAtLog")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _result: MutableList<HabitLogEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: HabitLogEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpHabitId: Long
          _tmpHabitId = _stmt.getLong(_columnIndexOfHabitId)
          val _tmpDate: String
          _tmpDate = _stmt.getText(_columnIndexOfDate)
          val _tmpCompletedValue: Int
          _tmpCompletedValue = _stmt.getLong(_columnIndexOfCompletedValue).toInt()
          val _tmpTargetValue: Int
          _tmpTargetValue = _stmt.getLong(_columnIndexOfTargetValue).toInt()
          val _tmpIsCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp != 0
          val _tmpIsAdapted: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsAdapted).toInt()
          _tmpIsAdapted = _tmp_1 != 0
          val _tmpMoodAtLog: String?
          if (_stmt.isNull(_columnIndexOfMoodAtLog)) {
            _tmpMoodAtLog = null
          } else {
            _tmpMoodAtLog = _stmt.getText(_columnIndexOfMoodAtLog)
          }
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          _item =
              HabitLogEntity(_tmpId,_tmpHabitId,_tmpDate,_tmpCompletedValue,_tmpTargetValue,_tmpIsCompleted,_tmpIsAdapted,_tmpMoodAtLog,_tmpTimestamp)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getTotalCompletedCount(): Int {
    val _sql: String = "SELECT COUNT(*) FROM habit_logs WHERE isCompleted = 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getTotalLoggedCount(): Int {
    val _sql: String = "SELECT COUNT(*) FROM habit_logs"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
