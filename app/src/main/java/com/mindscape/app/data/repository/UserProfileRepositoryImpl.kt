package com.mindscape.app.data.repository

import com.mindscape.app.data.local.dao.UserProfileDao
import com.mindscape.app.data.local.entity.UserProfileEntity
import com.mindscape.app.domain.model.UserProfile
import com.mindscape.app.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserProfileRepositoryImpl(
    private val userProfileDao: UserProfileDao
) : UserProfileRepository {

    override fun getUserProfile(): Flow<UserProfile> {
        return userProfileDao.getUserProfile().map { entity ->
            entity?.toDomainModel() ?: UserProfile()
        }
    }

    override suspend fun updateProfile(profile: UserProfile) {
        userProfileDao.insertOrUpdateProfile(profile.toEntity())
    }

    override suspend fun setSmartAdaptiveGoalsEnabled(enabled: Boolean) {
        userProfileDao.setSmartAdaptiveGoalsEnabled(enabled)
    }

    private fun UserProfileEntity.toDomainModel(): UserProfile {
        return UserProfile(
            id = id,
            userName = userName,
            email = email,
            primaryFocus = primaryFocus,
            checkInTime = checkInTime,
            checkInFrequency = checkInFrequency,
            smartAdaptiveGoalsEnabled = smartAdaptiveGoalsEnabled,
            streakDays = streakDays,
            consistencyPercentage = consistencyPercentage,
            completedSessions = completedSessions,
            level = level
        )
    }

    private fun UserProfile.toEntity(): UserProfileEntity {
        return UserProfileEntity(
            id = id,
            userName = userName,
            email = email,
            primaryFocus = primaryFocus,
            checkInTime = checkInTime,
            checkInFrequency = checkInFrequency,
            smartAdaptiveGoalsEnabled = smartAdaptiveGoalsEnabled,
            streakDays = streakDays,
            consistencyPercentage = consistencyPercentage,
            completedSessions = completedSessions,
            level = level
        )
    }
}
