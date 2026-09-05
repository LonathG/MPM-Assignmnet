package com.mindscape.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mindscape.app.domain.model.DailyCheckIn
import com.mindscape.app.domain.model.Habit
import com.mindscape.app.domain.model.UserProfile
import com.mindscape.app.domain.usecase.ManageHabitsUseCase
import com.mindscape.app.domain.usecase.PerformCheckInUseCase
import com.mindscape.app.domain.usecase.GetAnalyticsUseCase
import com.mindscape.app.domain.repository.CheckInRepository
import com.mindscape.app.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class CalendarDay(
    val dayName: String, // "Tue", "Wed", "Thu", "Fri"
    val dayNumber: Int,  // 8, 9, 10, 11
    val isSelected: Boolean = false
)

data class HomeUiState(
    val userProfile: UserProfile = UserProfile(),
    val habits: List<Habit> = emptyList(),
    val latestCheckIn: DailyCheckIn? = null,
    val calendarDays: List<CalendarDay> = listOf(
        CalendarDay("Tue", 8),
        CalendarDay("Wed", 9),
        CalendarDay("Thu", 10, isSelected = true),
        CalendarDay("Fri", 11),
        CalendarDay("Sat", 12)
    ),
    val sliderMoodValue: Float = 3f,
    val isLoading: Boolean = false
)

class HomeViewModel(
    private val manageHabitsUseCase: ManageHabitsUseCase,
    private val checkInRepository: CheckInRepository,
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            manageHabitsUseCase.getAllHabits().collectLatest { habitsList ->
                _uiState.value = _uiState.value.copy(habits = habitsList)
            }
        }

        viewModelScope.launch {
            userProfileRepository.getUserProfile().collectLatest { profile ->
                _uiState.value = _uiState.value.copy(userProfile = profile)
            }
        }

        viewModelScope.launch {
            checkInRepository.getAllCheckIns().collectLatest { checkIns ->
                _uiState.value = _uiState.value.copy(latestCheckIn = checkIns.firstOrNull())
            }
        }
    }

    fun onSelectDay(day: CalendarDay) {
        val updated = _uiState.value.calendarDays.map {
            it.copy(isSelected = it.dayNumber == day.dayNumber)
        }
        _uiState.value = _uiState.value.copy(calendarDays = updated)
    }

    fun toggleHabitCompletion(habit: Habit) {
        viewModelScope.launch {
            manageHabitsUseCase.toggleHabitCompletion(habit)
        }
    }

    class Factory(
        private val manageHabitsUseCase: ManageHabitsUseCase,
        private val checkInRepository: CheckInRepository,
        private val userProfileRepository: UserProfileRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(manageHabitsUseCase, checkInRepository, userProfileRepository) as T
        }
    }
}
