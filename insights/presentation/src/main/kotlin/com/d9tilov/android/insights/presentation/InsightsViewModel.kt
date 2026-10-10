package com.d9tilov.android.insights.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.core.constants.DiConstants.DISPATCHER_IO
import com.d9tilov.android.core.utils.currentDate
import com.d9tilov.android.core.utils.toLocalDateTime
import com.d9tilov.android.insights.domain.contract.InsightsInteractor
import com.d9tilov.android.insights.domain.model.Insight
import com.d9tilov.android.insights.domain.model.InsightGenerationStatus
import com.d9tilov.android.insights.domain.model.InsightsConstants.MAX_INSIGHTS_PER_DAY
import com.d9tilov.android.insights.domain.model.exception.InsightServiceException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

data class InsightsUiState(
    val insights: List<Insight> = emptyList(),
    val isHistoryLoading: Boolean = true,
    val errorState: InsightsErrorState? = null,
    val generationStatus: InsightGenerationStatus = InsightGenerationStatus.UNDEFINED,
)

data class InsightsErrorState(
    val messageRes: Int,
    val canRetry: Boolean = false,
    val messageArg: Int? = null,
)

@HiltViewModel
class InsightsViewModel @Inject constructor(
    @param:Named(DISPATCHER_IO) private val ioDispatcher: CoroutineDispatcher,
    private val insightsInteractor: InsightsInteractor,
) : ViewModel() {
    private val _state = MutableStateFlow(InsightsUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            insightsInteractor
                .history()
                .combine(insightsInteractor.hasEnoughTransactionsForInsight()) { insights, enough ->
                    insights to enough
                }.flowOn(ioDispatcher)
                .collect { (insights, enough) ->
                    _state.update { current ->
                        current.copy(
                            insights = insights,
                            isHistoryLoading = false,
                            generationStatus = updatedStatus(insights, enough, current.generationStatus),
                        )
                    }
                }
        }
    }

    suspend fun isConsentGranted(): Boolean = insightsInteractor.isConsentGranted()

    suspend fun grantConsent() = insightsInteractor.grantConsent()

    fun dismissSnackbarError() {
        _state.update { it.copy(errorState = null) }
    }

    fun generate() {
        val state = _state.value
        if (state.generationStatus != InsightGenerationStatus.AVAILABLE) return
        _state.update { it.copy(generationStatus = InsightGenerationStatus.LOADING, errorState = null) }
        viewModelScope.launch {
            try {
                withContext(ioDispatcher) { insightsInteractor.generate() }
                setGenerationStatus(InsightGenerationStatus.DAILY_LIMIT_REACHED)
            } catch (error: CancellationException) {
                throw error
            } catch (_: InsightServiceException) {
                setGenerationStatus(InsightGenerationStatus.AVAILABLE)
                setError(R.string.insights_unavailable_message, canRetry = true)
            } catch (_: Exception) {
                setGenerationStatus(InsightGenerationStatus.AVAILABLE)
                setError(R.string.insights_unavailable_message)
            }
        }
    }

    private fun updatedStatus(
        insights: List<Insight>,
        hasEnoughTransactions: Boolean,
        currentStatus: InsightGenerationStatus,
    ): InsightGenerationStatus {
        if (currentStatus == InsightGenerationStatus.LOADING) return InsightGenerationStatus.LOADING
        val today = currentDate()
        val dailyLimitReached =
            insights.count { it.createdAtMillis.toLocalDateTime().date == today } >= MAX_INSIGHTS_PER_DAY
        return when {
            dailyLimitReached -> InsightGenerationStatus.DAILY_LIMIT_REACHED
            !hasEnoughTransactions -> InsightGenerationStatus.INSUFFICIENT_DATA
            else -> InsightGenerationStatus.AVAILABLE
        }
    }

    private fun setGenerationStatus(status: InsightGenerationStatus) {
        _state.update { it.copy(generationStatus = status) }
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
