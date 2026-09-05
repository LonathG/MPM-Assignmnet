package com.mindscape.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mindscape.app.domain.model.Habit
import com.mindscape.app.domain.model.HabitCategory
import com.mindscape.app.domain.usecase.ManageHabitsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class HabitsUiState(
    val habits: List<Habit> = emptyList(),
    val newHabitName: String = "",
    val selectedCategory: HabitCategory = HabitCategory.MINDFULNESS,
    val targetValueText: String = "15",
    val targetUnit: String = "mins",
    val selectedFrequency: String = "Daily",
    val editingHabit: Habit? = null,
    val isLoading: Boolean = false,
    val weeklyConsistencyPercentage: Int = 82
)

class HabitViewModel(
    private val manageHabitsUseCase: ManageHabitsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HabitsUiState())
    val uiState: StateFlow<HabitsUiState> = _uiState.asStateFlow()

    init {
        loadHabits()
    }

    private fun loadHabits() {
        viewModelScope.launch {
            manageHabitsUseCase.getAllHabits().collectLatest { habitsList ->
                val completedCount = habitsList.count { it.isCompleted }
                val total = habitsList.size
                val consistency = if (total > 0) (completedCount * 100) / total else 82
                _uiState.value = _uiState.value.copy(
                    habits = habitsList,
                    weeklyConsistencyPercentage = if (consistency == 0) 82 else consistency
                )
            }
        }
    }

    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(newHabitName = name)
    }

    fun onCategorySelect(category: HabitCategory) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun onTargetValueChange(valueText: String) {
        _uiState.value = _uiState.value.copy(targetValueText = valueText)
    }

    fun onTargetUnitChange(unit: String) {
        _uiState.value = _uiState.value.copy(targetUnit = unit)
    }

    fun onFrequencySelect(frequency: String) {
        _uiState.value = _uiState.value.copy(selectedFrequency = frequency)
    }

    fun addHabit() {
        val name = _uiState.value.newHabitName.trim()
        if (name.isEmpty()) return

        val target = _uiState.value.targetValueText.toIntOrNull() ?: 10
        val habit = Habit(
            name = name,
            category = _uiState.value.selectedCategory,
            frequency = _uiState.value.selectedFrequency,
            targetValue = target,
            targetUnit = _uiState.value.targetUnit,
            originalTargetValue = target
        )

        viewModelScope.launch {
            manageHabitsUseCase.createHabit(habit)
            // Reset input form
            _uiState.value = _uiState.value.copy(
                newHabitName = "",
                targetValueText = "15"
            )
        }
    }

    fun toggleHabitCompletion(habit: Habit) {
        viewModelScope.launch {
            manageHabitsUseCase.toggleHabitCompletion(habit)
        }
    }

    fun incrementHabit(habit: Habit) {
        viewModelScope.launch {
            manageHabitsUseCase.incrementHabitValue(habit)
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            manageHabitsUseCase.deleteHabit(habit)
        }
    }

    fun startEditingHabit(habit: Habit) {
        _uiState.value = _uiState.value.copy(editingHabit = habit)
    }

    fun dismissEditModal() {
        _uiState.value = _uiState.value.copy(editingHabit = null)
    }

    fun saveEditedHabit(updatedName: String, updatedTarget: Int) {
        val current = _uiState.value.editingHabit ?: return
        val updated = current.copy(
            name = updatedName,
            targetValue = updatedTarget,
            originalTargetValue = updatedTarget
        )
        viewModelScope.launch {
            manageHabitsUseCase.updateHabit(updated)
            dismissEditModal()
        }
    }

    class Factory(
        private val manageHabitsUseCase: ManageHabitsUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HabitViewModel(manageHabitsUseCase) as T
        }
    }
}
