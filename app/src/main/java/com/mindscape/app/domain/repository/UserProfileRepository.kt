package com.mindscape.app.domain.repository

import com.mindscape.app.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {
    fun getUserProfile(): Flow<UserProfile>
    suspend fun updateProfile(profile: UserProfile)
    suspend fun setSmartAdaptiveGoalsEnabled(enabled: Boolean)
}
