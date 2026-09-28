package com.d9tilov.android.insights.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.core.constants.DiConstants.DISPATCHER_IO
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
        val insight: String,
    ) : InsightsUiState

    data object Loading : InsightsUiState

    data class Error(
        val messageRes: Int,
    ) : InsightsUiState
}

@HiltViewModel
class InsightsViewModel @Inject constructor(
    @param:Named(DISPATCHER_IO) private val ioDispatcher: CoroutineDispatcher,
    private val repository: InsightsRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<InsightsUiState>(InsightsUiState.Loading)
    val state = _state.asStateFlow()

    init {
        generate()
    }

    @Suppress("TooGenericExceptionCaught")
    fun generate() {
        viewModelScope.launch {
            _state.update { InsightsUiState.Loading }
            try {
                val insight = withContext(ioDispatcher) { repository.generate(Locale.getDefault().toLanguageTag()) }
                _state.update { InsightsUiState.Data(insight) }
            } catch (error: CancellationException) {
                throw error
            } catch (_: InsufficientInsightsDataException) {
                _state.update { InsightsUiState.Error(R.string.insights_insufficient_data) }
            } catch (_: NoNewInsightException) {
                _state.update { InsightsUiState.Error(R.string.insights_no_new_insight) }
            } catch (_: Exception) {
                _state.update { InsightsUiState.Error(R.string.insights_unavailable_message) }
            }
        }
    }
}
