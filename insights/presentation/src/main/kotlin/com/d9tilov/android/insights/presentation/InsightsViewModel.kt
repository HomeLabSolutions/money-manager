package com.d9tilov.android.insights.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.core.constants.DiConstants.DISPATCHER_IO
import com.d9tilov.android.insights.domain.Insight
import com.d9tilov.android.insights.domain.InsightsConsentRepository
import com.d9tilov.android.insights.domain.InsightsConstants.MIN_INSIGHT_HISTORY_DAYS
import com.d9tilov.android.insights.domain.InsightsRepository
import com.d9tilov.android.insights.domain.exception.DailyInsightLimitException
import com.d9tilov.android.insights.domain.exception.InsightServiceException
import com.d9tilov.android.insights.domain.exception.InsufficientInsightsDataException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

data class InsightsUiState(
    val insights: List<Insight> = emptyList(),
    val isInsightLoading: Boolean = false,
    val isHistoryLoading: Boolean = true,
    val errorState: InsightsErrorState? = null,
)

data class InsightsErrorState(
    val messageRes: Int,
    val canRetry: Boolean = false,
    val messageArg: Int? = null,
)

@HiltViewModel
class InsightsViewModel @Inject constructor(
    @param:Named(DISPATCHER_IO) private val ioDispatcher: CoroutineDispatcher,
    private val repository: InsightsRepository,
    private val consentRepository: InsightsConsentRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(InsightsUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository
                .history()
                .catch {
                    _state.update { it.copy(isHistoryLoading = false) }
                    setError(R.string.insights_unavailable_message)
                }.collect { insights -> _state.update { it.copy(insights = insights, isHistoryLoading = false) } }
        }
    }

    suspend fun isConsentGranted(): Boolean = consentRepository.isGranted.first()

    suspend fun grantConsent() = consentRepository.grant()

    fun dismissSnackbarError() {
        _state.update { it.copy(errorState = null) }
    }

    fun generate() {
        if (_state.value.isInsightLoading) return
        _state.update { it.copy(isInsightLoading = true, errorState = null) }
        viewModelScope.launch {
            try {
                withContext(ioDispatcher) { repository.generate() }
            } catch (error: CancellationException) {
                throw error
            } catch (_: InsufficientInsightsDataException) {
                setError(R.string.insights_insufficient_data, messageArg = MIN_INSIGHT_HISTORY_DAYS)
            } catch (_: DailyInsightLimitException) {
                setError(R.string.insights_daily_limit)
            } catch (_: InsightServiceException) {
                setError(R.string.insights_unavailable_message, canRetry = true)
            } catch (_: Exception) {
                setError(R.string.insights_unavailable_message)
            } finally {
                _state.update { it.copy(isInsightLoading = false) }
            }
        }
    }

    private fun setError(
        messageRes: Int,
        canRetry: Boolean = false,
        messageArg: Int? = null,
    ) {
        _state.update {
            it.copy(errorState = InsightsErrorState(messageRes, canRetry, messageArg))
        }
    }
}
