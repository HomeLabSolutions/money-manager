package com.d9tilov.android.insights.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.core.constants.DiConstants.DISPATCHER_IO
import com.d9tilov.android.insights.domain.Insight
import com.d9tilov.android.insights.domain.InsightsRepository
import com.d9tilov.android.insights.domain.InsufficientInsightsDataException
import com.d9tilov.android.insights.domain.NoNewInsightException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Named

sealed interface InsightsUiState {
    data class Data(
        val insights: List<Insight>,
        val isGenerating: Boolean = false,
    ) : InsightsUiState

    data object Loading : InsightsUiState

    data class Error(
        val insights: List<Insight>,
        val messageRes: Int,
    ) : InsightsUiState
}

private fun InsightsUiState.historyRows(): List<Insight> =
    when (this) {
        is InsightsUiState.Data -> insights
        is InsightsUiState.Error -> insights
        InsightsUiState.Loading -> emptyList()
    }

@HiltViewModel
class InsightsViewModel @Inject constructor(
    @param:Named(DISPATCHER_IO) private val ioDispatcher: CoroutineDispatcher,
    private val repository: InsightsRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<InsightsUiState>(InsightsUiState.Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.history().collect { insights ->
                _state.update { current ->
                    when (current) {
                        is InsightsUiState.Data -> current.copy(insights = insights)
                        is InsightsUiState.Error -> current.copy(insights = insights)
                        InsightsUiState.Loading -> InsightsUiState.Data(insights)
                    }
                }
            }
        }
    }

    @Suppress("TooGenericExceptionCaught")
    fun generate() {
        val current = _state.value
        if (current is InsightsUiState.Data && current.isGenerating) return
        _state.update { InsightsUiState.Data(it.historyRows(), isGenerating = true) }
        viewModelScope.launch {
            try {
                withContext(ioDispatcher) { repository.generate(Locale.getDefault().toLanguageTag()) }
                _state.update { InsightsUiState.Data(it.historyRows()) }
            } catch (error: CancellationException) {
                throw error
            } catch (_: InsufficientInsightsDataException) {
                setError(R.string.insights_insufficient_data)
            } catch (_: NoNewInsightException) {
                setError(R.string.insights_no_new_insight)
            } catch (_: Exception) {
                setError(R.string.insights_unavailable_message)
            }
        }
    }

    private fun setError(messageRes: Int) {
        _state.update { InsightsUiState.Error(it.historyRows(), messageRes) }
    }
}
