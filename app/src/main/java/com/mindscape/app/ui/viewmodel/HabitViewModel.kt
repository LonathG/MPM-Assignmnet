package com.mindscape.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mindscape.app.domain.engine.RoutineGenerationResult
import com.mindscape.app.domain.engine.RoutineIntent
import com.mindscape.app.domain.engine.SmartRoutineEngine
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
    val weeklyConsistencyPercentage: Int = 82,
    // Smart Routine Generator State
    val isSmartRoutineDialogOpen: Boolean = false,
    val smartRoutinePrompt: String = "",
    val detectedIntent: RoutineIntent? = null,
    val isGeneratingRoutine: Boolean = false,
    val lastGeneratedResult: RoutineGenerationResult? = null,
    val smartRoutineError: String? = null
)

class HabitViewModel(
    private val manageHabitsUseCase: ManageHabitsUseCase,
    private val smartRoutineEngine: SmartRoutineEngine
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

    // --- Smart Routine Generator Controls ---

    fun openSmartRoutineDialog() {
        _uiState.value = _uiState.value.copy(
            isSmartRoutineDialogOpen = true,
            smartRoutineError = null,
            lastGeneratedResult = null
        )
    }

    fun dismissSmartRoutineDialog() {
        _uiState.value = _uiState.value.copy(
            isSmartRoutineDialogOpen = false,
            smartRoutineError = null
        )
    }

    fun onSmartRoutinePromptChange(prompt: String) {
        val detected = if (prompt.isNotBlank()) smartRoutineEngine.detectIntent(prompt) else null
        _uiState.value = _uiState.value.copy(
            smartRoutinePrompt = prompt,
            detectedIntent = detected,
            smartRoutineError = null
        )
    }

    fun generateSmartRoutine(promptOverride: String? = null) {
        val prompt = (promptOverride ?: _uiState.value.smartRoutinePrompt).trim()
        if (prompt.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                smartRoutineError = "Please describe what routine you'd like (e.g., 'Routine for low energy')."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isGeneratingRoutine = true,
                smartRoutineError = null
            )
            try {
                // Calls engine to analyze heuristics & insert into Room DB
                val result = smartRoutineEngine.generateAndSaveRoutine(prompt)
                _uiState.value = _uiState.value.copy(
                    isGeneratingRoutine = false,
                    lastGeneratedResult = result,
                    smartRoutinePrompt = "",
                    detectedIntent = result.intent
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isGeneratingRoutine = false,
                    smartRoutineError = "Error creating routine: ${e.localizedMessage ?: "Unknown error"}"
                )
            }
        }
    }

    fun clearLastGeneratedResult() {
        _uiState.value = _uiState.value.copy(lastGeneratedResult = null)
    }

    class Factory(
        private val manageHabitsUseCase: ManageHabitsUseCase,
        private val smartRoutineEngine: SmartRoutineEngine
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HabitViewModel(manageHabitsUseCase, smartRoutineEngine) as T
        }
    }
}
