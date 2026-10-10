package com.d9tilov.android.insights.presentation

import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.core.utils.toMillis
import com.d9tilov.android.insights.domain.contract.InsightsInteractor
import com.d9tilov.android.insights.domain.model.GeneratedInsight
import com.d9tilov.android.insights.domain.model.Insight
import com.d9tilov.android.insights.domain.model.InsightGenerationStatus
import com.d9tilov.android.insights.domain.model.exception.DailyInsightLimitException
import com.d9tilov.android.insights.domain.model.exception.InsightServiceException
import com.d9tilov.android.insights.domain.model.exception.InsufficientInsightsDataException
import com.d9tilov.android.user.domain.model.InsightLanguage
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
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
    private var historySubscriptions = 0
    private val enoughTransactions = MutableStateFlow(true)
    private var enoughTransactionsFlow: Flow<Boolean> = enoughTransactions
    private var generateCalls = 0
    private var generate: suspend () -> GeneratedInsight = { GeneratedInsight("New insight") }
    private val insightsInteractor =
        object : InsightsInteractor {
            override val language = flowOf(InsightLanguage.SYSTEM)

            override suspend fun setLanguage(language: InsightLanguage) = Unit

            override fun hasEnoughTransactionsForInsight(): Flow<Boolean> = enoughTransactionsFlow

            override suspend fun isConsentGranted(): Boolean = true

            override suspend fun grantConsent() = Unit

            override fun history(): Flow<List<Insight>> =
                flow {
                    historySubscriptions++
                    emitAll(historyFlow)
                }

            override suspend fun generate(): GeneratedInsight {
                generateCalls++
                return generate.invoke()
            }
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
    fun `history and generation wait for transaction check`() =
        runTest(dispatcher) {
            val enough = CompletableDeferred<Boolean>()
            enoughTransactionsFlow = flow { emit(enough.await()) }
            val viewModel = InsightsViewModel(dispatcher, insightsInteractor)
            assertEquals(InsightGenerationStatus.UNDEFINED, viewModel.state.value.generationStatus)
            assertEquals(true, viewModel.state.value.isHistoryLoading)
            assertEquals(emptyList<Insight>(), viewModel.state.value.insights)
            viewModel.generate()
            assertEquals(0, generateCalls)

            enough.complete(true)
            assertEquals(false, viewModel.state.value.isHistoryLoading)
            assertEquals(history.value, viewModel.state.value.insights)
            assertEquals(InsightGenerationStatus.AVAILABLE, viewModel.state.value.generationStatus)
            assertEquals(1, historySubscriptions)
        }

    @Test
    fun `status follows transaction availability and prioritizes daily limit`() =
        runTest(dispatcher) {
            enoughTransactions.value = false
            val viewModel = InsightsViewModel(dispatcher, insightsInteractor)
            assertEquals(InsightGenerationStatus.INSUFFICIENT_DATA, viewModel.state.value.generationStatus)
            viewModel.generate()
            assertEquals(0, generateCalls)

            enoughTransactions.value = true
            assertEquals(InsightGenerationStatus.AVAILABLE, viewModel.state.value.generationStatus)

            history.value = history.value + Insight(2L, currentDateTime().toMillis(), "Today's insight")
            assertEquals(InsightGenerationStatus.DAILY_LIMIT_REACHED, viewModel.state.value.generationStatus)
            enoughTransactions.value = false
            assertEquals(InsightGenerationStatus.DAILY_LIMIT_REACHED, viewModel.state.value.generationStatus)
            history.value = history.value.take(1)
            assertEquals(InsightGenerationStatus.INSUFFICIENT_DATA, viewModel.state.value.generationStatus)
            assertEquals(1, historySubscriptions)
        }

    @Test
    fun `history and transaction updates preserve loading and prevent duplicate generation`() =
        runTest(dispatcher) {
            val result = CompletableDeferred<GeneratedInsight>()
            generate = { result.await() }
            val viewModel = InsightsViewModel(dispatcher, insightsInteractor)
            viewModel.generate()
            enoughTransactions.value = false
            enoughTransactions.value = true
            assertEquals(InsightGenerationStatus.LOADING, viewModel.state.value.generationStatus)
            viewModel.generate()
            assertEquals(1, generateCalls)
            history.value = history.value + Insight(2L, currentDateTime().toMillis(), "Background insight")
            assertEquals(history.value, viewModel.state.value.insights)
            assertEquals(InsightGenerationStatus.LOADING, viewModel.state.value.generationStatus)
            viewModel.generate()
            assertEquals(1, generateCalls)

            result.complete(GeneratedInsight("Generated insight"))
            assertEquals(InsightGenerationStatus.DAILY_LIMIT_REACHED, viewModel.state.value.generationStatus)
            assertEquals(1, historySubscriptions)
        }

    @Test
    fun `generation failures preserve history and only service failures allow retry`() =
        runTest(dispatcher) {
            val viewModel = InsightsViewModel(dispatcher, insightsInteractor)
            listOf(
                InsightServiceException(IOException("Technical details")) to
                    InsightsErrorState(R.string.insights_unavailable_message, canRetry = true),
                IllegalStateException("Technical details") to InsightsErrorState(R.string.insights_unavailable_message),
                InsufficientInsightsDataException() to InsightsErrorState(R.string.insights_unavailable_message),
                DailyInsightLimitException() to InsightsErrorState(R.string.insights_unavailable_message),
            ).forEach { (exception, expectedError) ->
                generate = { throw exception }
                viewModel.generate()
                assertEquals(InsightGenerationStatus.AVAILABLE, viewModel.state.value.generationStatus)
                assertEquals(expectedError, viewModel.state.value.errorState)
                assertEquals(history.value, viewModel.state.value.insights)
                viewModel.dismissSnackbarError()
                assertEquals(null, viewModel.state.value.errorState)
            }
        }

    @Test
    fun `success blocks generation before saved history is observed`() =
        runTest(dispatcher) {
            val viewModel = InsightsViewModel(dispatcher, insightsInteractor)
            viewModel.generate()
            assertEquals(InsightGenerationStatus.DAILY_LIMIT_REACHED, viewModel.state.value.generationStatus)
            viewModel.generate()
            assertEquals(1, generateCalls)
        }
}
