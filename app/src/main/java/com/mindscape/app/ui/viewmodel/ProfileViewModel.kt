package com.mindscape.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mindscape.app.domain.model.UserProfile
import com.mindscape.app.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            userProfileRepository.getUserProfile().collectLatest { profile ->
                _userProfile.value = profile
            }
        }
    }

    fun onToggleSmartAdaptiveGoals(enabled: Boolean) {
        viewModelScope.launch {
            userProfileRepository.setSmartAdaptiveGoalsEnabled(enabled)
            _userProfile.value = _userProfile.value.copy(smartAdaptiveGoalsEnabled = enabled)
        }
    }

    fun updateCheckInTime(newTime: String) {
        viewModelScope.launch {
            val updated = _userProfile.value.copy(checkInTime = newTime)
            userProfileRepository.updateProfile(updated)
            _userProfile.value = updated
        }
    }

    fun signOut(onComplete: () -> Unit) {
        viewModelScope.launch {
            userProfileRepository.updateProfile(
                UserProfile(
                    id = 1,
                    userName = "",
                    email = "",
                    isOnboardingCompleted = false
                )
            )
            onComplete()
        }
    }

    class Factory(
        private val userProfileRepository: UserProfileRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProfileViewModel(userProfileRepository) as T
        }
    }
}
