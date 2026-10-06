package com.d9tilov.android.insights.presentation

import com.d9tilov.android.insights.domain.GeneratedInsight
import com.d9tilov.android.insights.domain.Insight
import com.d9tilov.android.insights.domain.InsightsConsentRepository
import com.d9tilov.android.insights.domain.InsightsRepository
import com.d9tilov.android.insights.domain.exception.DailyInsightLimitException
import com.d9tilov.android.insights.domain.exception.InsightServiceException
import com.d9tilov.android.insights.domain.exception.InsufficientInsightsDataException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class InsightsViewModelTest {
    private val dispatcher = UnconfinedTestDispatcher()
    private val history = MutableStateFlow(listOf(Insight(1L, 0L, "Saved insight")))
    private var historyFlow: Flow<List<Insight>> = history
    private var historyCalls = 0
    private var generate: suspend () -> GeneratedInsight = { GeneratedInsight("New insight") }
    private val repository =
        object : InsightsRepository {
            override fun history(): Flow<List<Insight>> {
                historyCalls++
                return historyFlow
            }

            override suspend fun generate(): GeneratedInsight = generate.invoke()
        }
    private val consent =
        object : InsightsConsentRepository {
            override val isGranted = flowOf(true)

            override suspend fun grant() = Unit
        }

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `generation failures preserve history and only transient failures allow retry`() =
        runTest(dispatcher) {
            val viewModel = InsightsViewModel(dispatcher, repository, consent)
            val cases =
                listOf(
                    InsufficientInsightsDataException() to
                        InsightsErrorState(R.string.insights_insufficient_data, messageArg = 14),
                    DailyInsightLimitException() to InsightsErrorState(R.string.insights_daily_limit),
                    InsightServiceException(IOException("Technical details")) to
                        InsightsErrorState(R.string.insights_unavailable_message, canRetry = true),
                    IllegalStateException("Technical details") to
                        InsightsErrorState(R.string.insights_unavailable_message),
                )

            cases.forEach { (exception, expectedError) ->
                generate = { throw exception }
                viewModel.generate()

                assertEquals(
                    InsightsUiState(insights = history.value, isHistoryLoading = false, errorState = expectedError),
                    viewModel.state.value,
                )

                viewModel.dismissSnackbarError()
                assertEquals(InsightsUiState(insights = history.value, isHistoryLoading = false), viewModel.state.value)
            }
        }

    @Test
    fun `history updates preserve the generation loader`() =
        runTest(dispatcher) {
            val result = CompletableDeferred<GeneratedInsight>()
            generate = { result.await() }
            val viewModel = InsightsViewModel(dispatcher, repository, consent)
            viewModel.generate()

            assertEquals(
                InsightsUiState(insights = history.value, isInsightLoading = true, isHistoryLoading = false),
                viewModel.state.value,
            )

            history.value = history.value + Insight(2L, 1L, "Background insight")

            assertEquals(
                InsightsUiState(insights = history.value, isInsightLoading = true, isHistoryLoading = false),
                viewModel.state.value,
            )

            result.complete(GeneratedInsight("Generated insight"))
            assertEquals(InsightsUiState(insights = history.value, isHistoryLoading = false), viewModel.state.value)
            assertEquals(1, historyCalls)
        }

    @Test
    fun `initial history loading is independent of generation`() =
        runTest(dispatcher) {
            val initialHistory = CompletableDeferred<List<Insight>>()
            val generatedInsight = CompletableDeferred<GeneratedInsight>()
            historyFlow = flow { emit(initialHistory.await()) }
            generate = { generatedInsight.await() }
            val viewModel = InsightsViewModel(dispatcher, repository, consent)

            assertEquals(InsightsUiState(), viewModel.state.value)

            viewModel.generate()
            assertEquals(InsightsUiState(isInsightLoading = true), viewModel.state.value)

            initialHistory.complete(history.value)
            assertEquals(
                InsightsUiState(insights = history.value, isInsightLoading = true, isHistoryLoading = false),
                viewModel.state.value,
            )

            generatedInsight.complete(GeneratedInsight("Generated insight"))
            assertEquals(InsightsUiState(insights = history.value, isHistoryLoading = false), viewModel.state.value)
        }

    @Test
    fun `history loading failure shows a generic error with empty data`() =
        runTest(dispatcher) {
            historyFlow = flow { throw IOException("Technical details") }
            val viewModel = InsightsViewModel(dispatcher, repository, consent)

            assertEquals(
                InsightsUiState(
                    isHistoryLoading = false,
                    errorState = InsightsErrorState(R.string.insights_unavailable_message),
                ),
                viewModel.state.value,
            )

            viewModel.dismissSnackbarError()
            assertEquals(InsightsUiState(isHistoryLoading = false), viewModel.state.value)
        }
}
