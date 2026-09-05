package com.mindscape.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mindscape.app.domain.model.AnalyticsSummary
import com.mindscape.app.domain.usecase.GetAnalyticsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AnalyticsViewModel(
    private val getAnalyticsUseCase: GetAnalyticsUseCase
) : ViewModel() {

    private val _summary = MutableStateFlow(AnalyticsSummary())
    val summary: StateFlow<AnalyticsSummary> = _summary.asStateFlow()

    init {
        loadAnalytics()
    }

    private fun loadAnalytics() {
        viewModelScope.launch {
            getAnalyticsUseCase().collectLatest { data ->
                _summary.value = data
            }
        }
    }

    class Factory(
        private val getAnalyticsUseCase: GetAnalyticsUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AnalyticsViewModel(getAnalyticsUseCase) as T
        }
    }
}
