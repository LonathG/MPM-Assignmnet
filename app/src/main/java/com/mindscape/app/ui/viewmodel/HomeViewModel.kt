package com.mindscape.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mindscape.app.domain.model.DailyCheckIn
import com.mindscape.app.domain.model.Habit
import com.mindscape.app.domain.model.UserProfile
import com.mindscape.app.domain.usecase.ManageHabitsUseCase
import com.mindscape.app.domain.repository.CheckInRepository
import com.mindscape.app.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class CalendarDay(
    val date: LocalDate,
    val dayName: String, // "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
    val dayNumber: Int,  // 11, 12, etc.
    val isToday: Boolean = false,
    val isPast: Boolean = false,
    val isSelected: Boolean = false
)

data class HomeUiState(
    val userProfile: UserProfile = UserProfile(),
    val habits: List<Habit> = emptyList(),
    val latestCheckIn: DailyCheckIn? = null,
    val calendarDays: List<CalendarDay> = emptyList(),
    val sliderMoodValue: Float = 3f,
    val isLoading: Boolean = false
)

class HomeViewModel(
    private val manageHabitsUseCase: ManageHabitsUseCase,
    private val checkInRepository: CheckInRepository,
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        HomeUiState(calendarDays = generateCurrentCalendarDays())
    )
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun generateCurrentCalendarDays(): List<CalendarDay> {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("EEE", Locale.getDefault())

        return (-3L..3L).map { offset ->
            val date = today.plusDays(offset)
            CalendarDay(
                date = date,
                dayName = date.format(formatter),
                dayNumber = date.dayOfMonth,
                isToday = offset == 0L,
                isPast = date.isBefore(today),
                isSelected = offset == 0L // Default to today selected
            )
        }
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

    fun onSelectDay(selectedDay: CalendarDay) {
        val updated = _uiState.value.calendarDays.map { day ->
            day.copy(isSelected = day.date == selectedDay.date)
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
