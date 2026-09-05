package com.mindscape.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mindscape.app.domain.engine.AdaptationResult
import com.mindscape.app.domain.model.DailyCheckIn
import com.mindscape.app.domain.model.EnergyLevel
import com.mindscape.app.domain.usecase.PerformCheckInUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class CheckInUiState(
    val sliderValue: Float = 3f, // 1.0 to 5.0
    val moodScore: Int = 3,
    val moodLabel: String = "Okay",
    val energyLevel: EnergyLevel = EnergyLevel.MEDIUM,
    val selectedFactors: Set<String> = setOf("Sleep", "Solitude"),
    val quickNote: String = "",
    val isCheckInComplete: Boolean = false,
    val adaptationResult: AdaptationResult? = null,
    val dateFormatted: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
)

class CheckInViewModel(
    private val performCheckInUseCase: PerformCheckInUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckInUiState())
    val uiState: StateFlow<CheckInUiState> = _uiState.asStateFlow()

    fun onSliderValueChange(value: Float) {
        val score = value.toInt().coerceIn(1, 5)
        val label = when (score) {
            1 -> "Stormy"
            2 -> "Rainy"
            3 -> "Okay"
            4 -> "Sunny"
            5 -> "Radiant"
            else -> "Okay"
        }
        val energy = when (score) {
            1, 2 -> EnergyLevel.LOW
            3 -> EnergyLevel.MEDIUM
            else -> EnergyLevel.HIGH
        }
        _uiState.value = _uiState.value.copy(
            sliderValue = value,
            moodScore = score,
            moodLabel = label,
            energyLevel = energy
        )
    }

    fun toggleFactor(factor: String) {
        val current = _uiState.value.selectedFactors.toMutableSet()
        if (current.contains(factor)) {
            current.remove(factor)
        } else {
            current.add(factor)
        }
        _uiState.value = _uiState.value.copy(selectedFactors = current)
    }

    fun onQuickNoteChange(note: String) {
        _uiState.value = _uiState.value.copy(quickNote = note)
    }

    fun submitCheckIn() {
        val state = _uiState.value
        val checkIn = DailyCheckIn(
            date = state.dateFormatted,
            moodScore = state.moodScore,
            moodLabel = state.moodLabel,
            energyLevel = state.energyLevel,
            contributingFactors = state.selectedFactors.toList(),
            quickNote = state.quickNote.ifEmpty { null }
        )

        viewModelScope.launch {
            val result = performCheckInUseCase(checkIn)
            _uiState.value = _uiState.value.copy(
                isCheckInComplete = true,
                adaptationResult = result
            )
        }
    }

    fun dismissCompletionModal() {
        _uiState.value = _uiState.value.copy(isCheckInComplete = false)
    }

    class Factory(
        private val performCheckInUseCase: PerformCheckInUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CheckInViewModel(performCheckInUseCase) as T
        }
    }
}
