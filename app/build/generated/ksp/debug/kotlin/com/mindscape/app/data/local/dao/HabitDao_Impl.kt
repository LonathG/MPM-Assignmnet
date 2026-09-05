package com.mindscape.app.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.mindscape.app.`data`.local.entity.HabitEntity
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class HabitDao_Impl(
  __db: RoomDatabase,
) : HabitDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfHabitEntity: EntityInsertAdapter<HabitEntity>

  private val __deleteAdapterOfHabitEntity: EntityDeleteOrUpdateAdapter<HabitEntity>

  private val __updateAdapterOfHabitEntity: EntityDeleteOrUpdateAdapter<HabitEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfHabitEntity = object : EntityInsertAdapter<HabitEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `habits` (`id`,`name`,`category`,`frequency`,`targetValue`,`targetUnit`,`currentValue`,`isCompleted`,`isAdapted`,`originalTargetValue`,`adaptationReason`,`isStarter`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: HabitEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.category)
        statement.bindText(4, entity.frequency)
        statement.bindLong(5, entity.targetValue.toLong())
        statement.bindText(6, entity.targetUnit)
        statement.bindLong(7, entity.currentValue.toLong())
        val _tmp: Int = if (entity.isCompleted) 1 else 0
        statement.bindLong(8, _tmp.toLong())
        val _tmp_1: Int = if (entity.isAdapted) 1 else 0
        statement.bindLong(9, _tmp_1.toLong())
        statement.bindLong(10, entity.originalTargetValue.toLong())
        val _tmpAdaptationReason: String? = entity.adaptationReason
        if (_tmpAdaptationReason == null) {
          statement.bindNull(11)
        } else {
          statement.bindText(11, _tmpAdaptationReason)
        }
        val _tmp_2: Int = if (entity.isStarter) 1 else 0
        statement.bindLong(12, _tmp_2.toLong())
        statement.bindLong(13, entity.createdAt)
      }
    }
    this.__deleteAdapterOfHabitEntity = object : EntityDeleteOrUpdateAdapter<HabitEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `habits` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: HabitEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfHabitEntity = object : EntityDeleteOrUpdateAdapter<HabitEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `habits` SET `id` = ?,`name` = ?,`category` = ?,`frequency` = ?,`targetValue` = ?,`targetUnit` = ?,`currentValue` = ?,`isCompleted` = ?,`isAdapted` = ?,`originalTargetValue` = ?,`adaptationReason` = ?,`isStarter` = ?,`createdAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: HabitEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.category)
        statement.bindText(4, entity.frequency)
        statement.bindLong(5, entity.targetValue.toLong())
        statement.bindText(6, entity.targetUnit)
        statement.bindLong(7, entity.currentValue.toLong())
        val _tmp: Int = if (entity.isCompleted) 1 else 0
        statement.bindLong(8, _tmp.toLong())
        val _tmp_1: Int = if (entity.isAdapted) 1 else 0
        statement.bindLong(9, _tmp_1.toLong())
        statement.bindLong(10, entity.originalTargetValue.toLong())
        val _tmpAdaptationReason: String? = entity.adaptationReason
        if (_tmpAdaptationReason == null) {
          statement.bindNull(11)
        } else {
          statement.bindText(11, _tmpAdaptationReason)
        }
        val _tmp_2: Int = if (entity.isStarter) 1 else 0
        statement.bindLong(12, _tmp_2.toLong())
        statement.bindLong(13, entity.createdAt)
        statement.bindLong(14, entity.id)
      }
    }
  }

  public override suspend fun insertHabit(habit: HabitEntity): Long = performSuspending(__db, false,
      true) { _connection ->
    val _result: Long = __insertAdapterOfHabitEntity.insertAndReturnId(_connection, habit)
    _result
  }

  public override suspend fun insertHabits(habits: List<HabitEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfHabitEntity.insert(_connection, habits)
  }

  public override suspend fun deleteHabit(habit: HabitEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __deleteAdapterOfHabitEntity.handle(_connection, habit)
  }

  public override suspend fun updateHabit(habit: HabitEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __updateAdapterOfHabitEntity.handle(_connection, habit)
  }

  public override fun getAllHabits(): Flow<List<HabitEntity>> {
    val _sql: String = "SELECT * FROM habits ORDER BY id DESC"
    return createFlow(__db, false, arrayOf("habits")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfFrequency: Int = getColumnIndexOrThrow(_stmt, "frequency")
        val _columnIndexOfTargetValue: Int = getColumnIndexOrThrow(_stmt, "targetValue")
        val _columnIndexOfTargetUnit: Int = getColumnIndexOrThrow(_stmt, "targetUnit")
        val _columnIndexOfCurrentValue: Int = getColumnIndexOrThrow(_stmt, "currentValue")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfIsAdapted: Int = getColumnIndexOrThrow(_stmt, "isAdapted")
        val _columnIndexOfOriginalTargetValue: Int = getColumnIndexOrThrow(_stmt,
            "originalTargetValue")
        val _columnIndexOfAdaptationReason: Int = getColumnIndexOrThrow(_stmt, "adaptationReason")
        val _columnIndexOfIsStarter: Int = getColumnIndexOrThrow(_stmt, "isStarter")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<HabitEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: HabitEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpFrequency: String
          _tmpFrequency = _stmt.getText(_columnIndexOfFrequency)
          val _tmpTargetValue: Int
          _tmpTargetValue = _stmt.getLong(_columnIndexOfTargetValue).toInt()
          val _tmpTargetUnit: String
          _tmpTargetUnit = _stmt.getText(_columnIndexOfTargetUnit)
          val _tmpCurrentValue: Int
          _tmpCurrentValue = _stmt.getLong(_columnIndexOfCurrentValue).toInt()
          val _tmpIsCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp != 0
          val _tmpIsAdapted: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsAdapted).toInt()
          _tmpIsAdapted = _tmp_1 != 0
          val _tmpOriginalTargetValue: Int
          _tmpOriginalTargetValue = _stmt.getLong(_columnIndexOfOriginalTargetValue).toInt()
          val _tmpAdaptationReason: String?
          if (_stmt.isNull(_columnIndexOfAdaptationReason)) {
            _tmpAdaptationReason = null
          } else {
            _tmpAdaptationReason = _stmt.getText(_columnIndexOfAdaptationReason)
          }
          val _tmpIsStarter: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsStarter).toInt()
          _tmpIsStarter = _tmp_2 != 0
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item =
              HabitEntity(_tmpId,_tmpName,_tmpCategory,_tmpFrequency,_tmpTargetValue,_tmpTargetUnit,_tmpCurrentValue,_tmpIsCompleted,_tmpIsAdapted,_tmpOriginalTargetValue,_tmpAdaptationReason,_tmpIsStarter,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getHabitById(id: Long): HabitEntity? {
    val _sql: String = "SELECT * FROM habits WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfFrequency: Int = getColumnIndexOrThrow(_stmt, "frequency")
        val _columnIndexOfTargetValue: Int = getColumnIndexOrThrow(_stmt, "targetValue")
        val _columnIndexOfTargetUnit: Int = getColumnIndexOrThrow(_stmt, "targetUnit")
        val _columnIndexOfCurrentValue: Int = getColumnIndexOrThrow(_stmt, "currentValue")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfIsAdapted: Int = getColumnIndexOrThrow(_stmt, "isAdapted")
        val _columnIndexOfOriginalTargetValue: Int = getColumnIndexOrThrow(_stmt,
            "originalTargetValue")
        val _columnIndexOfAdaptationReason: Int = getColumnIndexOrThrow(_stmt, "adaptationReason")
        val _columnIndexOfIsStarter: Int = getColumnIndexOrThrow(_stmt, "isStarter")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: HabitEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpFrequency: String
          _tmpFrequency = _stmt.getText(_columnIndexOfFrequency)
          val _tmpTargetValue: Int
          _tmpTargetValue = _stmt.getLong(_columnIndexOfTargetValue).toInt()
          val _tmpTargetUnit: String
          _tmpTargetUnit = _stmt.getText(_columnIndexOfTargetUnit)
          val _tmpCurrentValue: Int
          _tmpCurrentValue = _stmt.getLong(_columnIndexOfCurrentValue).toInt()
          val _tmpIsCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp != 0
          val _tmpIsAdapted: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsAdapted).toInt()
          _tmpIsAdapted = _tmp_1 != 0
          val _tmpOriginalTargetValue: Int
          _tmpOriginalTargetValue = _stmt.getLong(_columnIndexOfOriginalTargetValue).toInt()
          val _tmpAdaptationReason: String?
          if (_stmt.isNull(_columnIndexOfAdaptationReason)) {
            _tmpAdaptationReason = null
          } else {
            _tmpAdaptationReason = _stmt.getText(_columnIndexOfAdaptationReason)
          }
          val _tmpIsStarter: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsStarter).toInt()
          _tmpIsStarter = _tmp_2 != 0
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _result =
              HabitEntity(_tmpId,_tmpName,_tmpCategory,_tmpFrequency,_tmpTargetValue,_tmpTargetUnit,_tmpCurrentValue,_tmpIsCompleted,_tmpIsAdapted,_tmpOriginalTargetValue,_tmpAdaptationReason,_tmpIsStarter,_tmpCreatedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteHabitById(id: Long) {
    val _sql: String = "DELETE FROM habits WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateHabitTarget(
    habitId: Long,
    targetValue: Int,
    isAdapted: Boolean,
    reason: String?,
  ) {
    val _sql: String =
        "UPDATE habits SET targetValue = ?, isAdapted = ?, adaptationReason = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, targetValue.toLong())
        _argIndex = 2
        val _tmp: Int = if (isAdapted) 1 else 0
        _stmt.bindLong(_argIndex, _tmp.toLong())
        _argIndex = 3
        if (reason == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, reason)
        }
        _argIndex = 4
        _stmt.bindLong(_argIndex, habitId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun resetAllHabitTargetsToOriginal() {
    val _sql: String =
        "UPDATE habits SET targetValue = originalTargetValue, isAdapted = 0, adaptationReason = NULL"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
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
