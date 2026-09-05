package com.mindscape.app.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mindscape.app.data.local.dao.DailyCheckInDao
import com.mindscape.app.data.local.entity.DailyCheckInEntity
import com.mindscape.app.domain.model.DailyCheckIn
import com.mindscape.app.domain.model.EnergyLevel
import com.mindscape.app.domain.repository.CheckInRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CheckInRepositoryImpl(
    private val checkInDao: DailyCheckInDao,
    private val gson: Gson = Gson()
) : CheckInRepository {

    override fun getAllCheckIns(): Flow<List<DailyCheckIn>> {
        return checkInDao.getAllCheckIns().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getCheckInByDate(date: String): DailyCheckIn? {
        return checkInDao.getCheckInByDate(date)?.toDomainModel()
    }

    override suspend fun getLatestCheckIn(): DailyCheckIn? {
        return checkInDao.getLatestCheckIn()?.toDomainModel()
    }

    override suspend fun saveCheckIn(checkIn: DailyCheckIn): Long {
        return checkInDao.insertCheckIn(checkIn.toEntity())
    }

    private fun DailyCheckInEntity.toDomainModel(): DailyCheckIn {
        val type = object : TypeToken<List<String>>() {}.type
        val factors: List<String> = try {
            gson.fromJson(contributingFactorsJson, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }

        return DailyCheckIn(
            id = id,
            date = date,
            moodScore = moodScore,
            moodLabel = moodLabel,
            energyLevel = EnergyLevel.fromString(energyLevel),
            contributingFactors = factors,
            quickNote = quickNote,
            timestamp = timestamp
        )
    }

    private fun DailyCheckIn.toEntity(): DailyCheckInEntity {
        val factorsJson = gson.toJson(contributingFactors)
        return DailyCheckInEntity(
            id = id,
            date = date,
            moodScore = moodScore,
            moodLabel = moodLabel,
            energyLevel = energyLevel.displayName,
            contributingFactorsJson = factorsJson,
            quickNote = quickNote,
            timestamp = timestamp
        )
    }
}
