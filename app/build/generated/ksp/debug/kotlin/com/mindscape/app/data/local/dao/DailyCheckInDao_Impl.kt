package com.mindscape.app.`data`.local.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.mindscape.app.`data`.local.entity.DailyCheckInEntity
import javax.`annotation`.processing.Generated
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
public class DailyCheckInDao_Impl(
  __db: RoomDatabase,
) : DailyCheckInDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfDailyCheckInEntity: EntityInsertAdapter<DailyCheckInEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfDailyCheckInEntity = object : EntityInsertAdapter<DailyCheckInEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `daily_check_ins` (`id`,`date`,`moodScore`,`moodLabel`,`energyLevel`,`contributingFactorsJson`,`quickNote`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: DailyCheckInEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.date)
        statement.bindLong(3, entity.moodScore.toLong())
        statement.bindText(4, entity.moodLabel)
        statement.bindText(5, entity.energyLevel)
        statement.bindText(6, entity.contributingFactorsJson)
        val _tmpQuickNote: String? = entity.quickNote
        if (_tmpQuickNote == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpQuickNote)
        }
        statement.bindLong(8, entity.timestamp)
      }
    }
  }

  public override suspend fun insertCheckIn(checkIn: DailyCheckInEntity): Long =
      performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfDailyCheckInEntity.insertAndReturnId(_connection, checkIn)
    _result
  }

  public override fun getAllCheckIns(): Flow<List<DailyCheckInEntity>> {
    val _sql: String = "SELECT * FROM daily_check_ins ORDER BY timestamp DESC"
    return createFlow(__db, false, arrayOf("daily_check_ins")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfMoodScore: Int = getColumnIndexOrThrow(_stmt, "moodScore")
        val _columnIndexOfMoodLabel: Int = getColumnIndexOrThrow(_stmt, "moodLabel")
        val _columnIndexOfEnergyLevel: Int = getColumnIndexOrThrow(_stmt, "energyLevel")
        val _columnIndexOfContributingFactorsJson: Int = getColumnIndexOrThrow(_stmt,
            "contributingFactorsJson")
        val _columnIndexOfQuickNote: Int = getColumnIndexOrThrow(_stmt, "quickNote")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _result: MutableList<DailyCheckInEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: DailyCheckInEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpDate: String
          _tmpDate = _stmt.getText(_columnIndexOfDate)
          val _tmpMoodScore: Int
          _tmpMoodScore = _stmt.getLong(_columnIndexOfMoodScore).toInt()
          val _tmpMoodLabel: String
          _tmpMoodLabel = _stmt.getText(_columnIndexOfMoodLabel)
          val _tmpEnergyLevel: String
          _tmpEnergyLevel = _stmt.getText(_columnIndexOfEnergyLevel)
          val _tmpContributingFactorsJson: String
          _tmpContributingFactorsJson = _stmt.getText(_columnIndexOfContributingFactorsJson)
          val _tmpQuickNote: String?
          if (_stmt.isNull(_columnIndexOfQuickNote)) {
            _tmpQuickNote = null
          } else {
            _tmpQuickNote = _stmt.getText(_columnIndexOfQuickNote)
          }
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          _item =
              DailyCheckInEntity(_tmpId,_tmpDate,_tmpMoodScore,_tmpMoodLabel,_tmpEnergyLevel,_tmpContributingFactorsJson,_tmpQuickNote,_tmpTimestamp)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getCheckInByDate(date: String): DailyCheckInEntity? {
    val _sql: String = "SELECT * FROM daily_check_ins WHERE date = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, date)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfMoodScore: Int = getColumnIndexOrThrow(_stmt, "moodScore")
        val _columnIndexOfMoodLabel: Int = getColumnIndexOrThrow(_stmt, "moodLabel")
        val _columnIndexOfEnergyLevel: Int = getColumnIndexOrThrow(_stmt, "energyLevel")
        val _columnIndexOfContributingFactorsJson: Int = getColumnIndexOrThrow(_stmt,
            "contributingFactorsJson")
        val _columnIndexOfQuickNote: Int = getColumnIndexOrThrow(_stmt, "quickNote")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _result: DailyCheckInEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpDate: String
          _tmpDate = _stmt.getText(_columnIndexOfDate)
          val _tmpMoodScore: Int
          _tmpMoodScore = _stmt.getLong(_columnIndexOfMoodScore).toInt()
          val _tmpMoodLabel: String
          _tmpMoodLabel = _stmt.getText(_columnIndexOfMoodLabel)
          val _tmpEnergyLevel: String
          _tmpEnergyLevel = _stmt.getText(_columnIndexOfEnergyLevel)
          val _tmpContributingFactorsJson: String
          _tmpContributingFactorsJson = _stmt.getText(_columnIndexOfContributingFactorsJson)
          val _tmpQuickNote: String?
          if (_stmt.isNull(_columnIndexOfQuickNote)) {
            _tmpQuickNote = null
          } else {
            _tmpQuickNote = _stmt.getText(_columnIndexOfQuickNote)
          }
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          _result =
              DailyCheckInEntity(_tmpId,_tmpDate,_tmpMoodScore,_tmpMoodLabel,_tmpEnergyLevel,_tmpContributingFactorsJson,_tmpQuickNote,_tmpTimestamp)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getLatestCheckIn(): DailyCheckInEntity? {
    val _sql: String = "SELECT * FROM daily_check_ins ORDER BY timestamp DESC LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfMoodScore: Int = getColumnIndexOrThrow(_stmt, "moodScore")
        val _columnIndexOfMoodLabel: Int = getColumnIndexOrThrow(_stmt, "moodLabel")
        val _columnIndexOfEnergyLevel: Int = getColumnIndexOrThrow(_stmt, "energyLevel")
        val _columnIndexOfContributingFactorsJson: Int = getColumnIndexOrThrow(_stmt,
            "contributingFactorsJson")
        val _columnIndexOfQuickNote: Int = getColumnIndexOrThrow(_stmt, "quickNote")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _result: DailyCheckInEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpDate: String
          _tmpDate = _stmt.getText(_columnIndexOfDate)
          val _tmpMoodScore: Int
          _tmpMoodScore = _stmt.getLong(_columnIndexOfMoodScore).toInt()
          val _tmpMoodLabel: String
          _tmpMoodLabel = _stmt.getText(_columnIndexOfMoodLabel)
          val _tmpEnergyLevel: String
          _tmpEnergyLevel = _stmt.getText(_columnIndexOfEnergyLevel)
          val _tmpContributingFactorsJson: String
          _tmpContributingFactorsJson = _stmt.getText(_columnIndexOfContributingFactorsJson)
          val _tmpQuickNote: String?
          if (_stmt.isNull(_columnIndexOfQuickNote)) {
            _tmpQuickNote = null
          } else {
            _tmpQuickNote = _stmt.getText(_columnIndexOfQuickNote)
          }
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          _result =
              DailyCheckInEntity(_tmpId,_tmpDate,_tmpMoodScore,_tmpMoodLabel,_tmpEnergyLevel,_tmpContributingFactorsJson,_tmpQuickNote,_tmpTimestamp)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getRecentCheckIns(limit: Int): Flow<List<DailyCheckInEntity>> {
    val _sql: String = "SELECT * FROM daily_check_ins ORDER BY timestamp DESC LIMIT ?"
    return createFlow(__db, false, arrayOf("daily_check_ins")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, limit.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfMoodScore: Int = getColumnIndexOrThrow(_stmt, "moodScore")
        val _columnIndexOfMoodLabel: Int = getColumnIndexOrThrow(_stmt, "moodLabel")
        val _columnIndexOfEnergyLevel: Int = getColumnIndexOrThrow(_stmt, "energyLevel")
        val _columnIndexOfContributingFactorsJson: Int = getColumnIndexOrThrow(_stmt,
            "contributingFactorsJson")
        val _columnIndexOfQuickNote: Int = getColumnIndexOrThrow(_stmt, "quickNote")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _result: MutableList<DailyCheckInEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: DailyCheckInEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpDate: String
          _tmpDate = _stmt.getText(_columnIndexOfDate)
          val _tmpMoodScore: Int
          _tmpMoodScore = _stmt.getLong(_columnIndexOfMoodScore).toInt()
          val _tmpMoodLabel: String
          _tmpMoodLabel = _stmt.getText(_columnIndexOfMoodLabel)
          val _tmpEnergyLevel: String
          _tmpEnergyLevel = _stmt.getText(_columnIndexOfEnergyLevel)
          val _tmpContributingFactorsJson: String
          _tmpContributingFactorsJson = _stmt.getText(_columnIndexOfContributingFactorsJson)
          val _tmpQuickNote: String?
          if (_stmt.isNull(_columnIndexOfQuickNote)) {
            _tmpQuickNote = null
          } else {
            _tmpQuickNote = _stmt.getText(_columnIndexOfQuickNote)
          }
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          _item =
              DailyCheckInEntity(_tmpId,_tmpDate,_tmpMoodScore,_tmpMoodLabel,_tmpEnergyLevel,_tmpContributingFactorsJson,_tmpQuickNote,_tmpTimestamp)
          _result.add(_item)
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
