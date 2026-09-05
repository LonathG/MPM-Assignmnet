package com.mindscape.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mindscape.app.domain.model.UserProfile
import com.mindscape.app.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OnboardingUiState(
    val step: Int = 1, // 1 or 2
    val name: String = "",
    val email: String = "",
    val primaryFocus: String = "Mindfulness",
    val starterHabits: Set<String> = setOf("Hydration", "Meditation", "Mindful Walk"),
    val checkInTime: String = "08:30 AM",
    val checkInFrequency: String = "Everyday"
)

class OnboardingViewModel(
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun onFocusSelect(focus: String) {
        _uiState.value = _uiState.value.copy(primaryFocus = focus)
    }

    fun toggleStarterHabit(habit: String) {
        val current = _uiState.value.starterHabits.toMutableSet()
        if (current.contains(habit)) current.remove(habit) else current.add(habit)
        _uiState.value = _uiState.value.copy(starterHabits = current)
    }

    fun onTimeChange(time: String) {
        _uiState.value = _uiState.value.copy(checkInTime = time)
    }

    fun onFrequencySelect(freq: String) {
        _uiState.value = _uiState.value.copy(checkInFrequency = freq)
    }

    fun goToStep2() {
        _uiState.value = _uiState.value.copy(step = 2)
    }

    fun completeOnboarding(onFinish: () -> Unit) {
        val state = _uiState.value
        val profile = UserProfile(
            userName = state.name.ifEmpty { "Lonath G" },
            email = state.email.ifEmpty { "lonath@example.com" },
            primaryFocus = state.primaryFocus,
            checkInTime = state.checkInTime,
            checkInFrequency = state.checkInFrequency
        )

        viewModelScope.launch {
            userProfileRepository.updateProfile(profile)
            onFinish()
        }
    }

    class Factory(
        private val userProfileRepository: UserProfileRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return OnboardingViewModel(userProfileRepository) as T
        }
    }
}
