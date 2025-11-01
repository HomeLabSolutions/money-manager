package com.d9tilov.android.statistics.ui.vm

import com.d9tilov.android.analytics.domain.AnalyticsSender
import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.domain.model.CurrencyMetaData
import com.d9tilov.android.statistics.data.model.StatisticsMenuType
import com.d9tilov.android.statistics.ui.model.StatisticsMenuCurrencyType
import com.d9tilov.android.statistics.ui.model.StatisticsMenuInStatisticsType
import com.d9tilov.android.statistics.ui.model.StatisticsMenuTransactionType
import com.d9tilov.android.statistics.ui.model.StatisticsPeriodModel
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.model.TransactionChartModel
import com.d9tilov.android.transaction.domain.model.TransactionMinMaxDateModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDateTime
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

@OptIn(ExperimentalCoroutinesApi::class)
class StatisticsViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val analyticsSender: AnalyticsSender = mockk(relaxed = true)
    private val transactionInteractor: TransactionInteractor = mockk(relaxed = true)
    private val currencyInteractor: CurrencyInteractor = mockk(relaxed = true)

    private val testCurrency =
        CurrencyMetaData(
            clientId = "test-client-id",
            code = "USD",
            symbol = "$",
        )

    private val testCategory =
        Category(
            id = 1L,
            clientId = "test-category-id",
            parent = null,
            children = emptyList(),
            type = TransactionType.EXPENSE,
            name = "Food",
            icon = 1,
            color = 1,
            usageCount = 10,
        )

    private val testTransactions =
        listOf(
            TransactionChartModel(
                clientId = "test-client-id",
                type = TransactionType.EXPENSE,
                category = testCategory,
                currencyCode = "USD",
                sum = BigDecimal("100.00"),
                percent = BigDecimal.ZERO,
            ),
        )

    private val testMinMaxDate =
        TransactionMinMaxDateModel(
            minDate = LocalDateTime(2024, 1, 1, 0, 0),
            maxDate = currentDateTime(),
        )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { currencyInteractor.getMainCurrency() } returns testCurrency
        coEvery { transactionInteractor.getTransactionMinMaxDate() } returns testMinMaxDate
        coEvery {
            transactionInteractor.getTransactionsGroupedByCategory(any(), any(), any(), any(), any(), any())
        } returns flowOf(testTransactions)
        coEvery {
            transactionInteractor.getSumInPeriod(any(), any(), any(), any(), any())
        } returns flowOf(BigDecimal("100.00"))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should set up default UI state`() =
        runTest {
            val viewModel =
                StatisticsViewModel(
                    analyticsSender,
                    testDispatcher,
                    transactionInteractor,
                    currencyInteractor,
                )
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertTrue(state.periodState.selectedPeriod is StatisticsPeriodModel.DAY)
            assertEquals(5, state.periodState.periods.size)
        }

    @Test
    fun `updatePeriod should update selected period`() =
        runTest {
            val viewModel =
                StatisticsViewModel(
                    analyticsSender,
                    testDispatcher,
                    transactionInteractor,
                    currencyInteractor,
                )
            advanceUntilIdle()

            val newPeriod = StatisticsPeriodModel.WEEK()
            viewModel.updatePeriod(newPeriod)
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertTrue(state.periodState.selectedPeriod is StatisticsPeriodModel.WEEK)
        }

    @Test
    fun `onMenuClick with CURRENCY should toggle currency type`() =
        runTest {
            val viewModel =
                StatisticsViewModel(
                    analyticsSender,
                    testDispatcher,
                    transactionInteractor,
                    currencyInteractor,
                )
            advanceUntilIdle()

            val initialCurrency = viewModel.uiState.value.statisticsMenuState.currency
            assertTrue(initialCurrency is StatisticsMenuCurrencyType.Current)

            viewModel.onMenuClick(StatisticsMenuType.CURRENCY)
            advanceUntilIdle()

            val newCurrency = viewModel.uiState.value.statisticsMenuState.currency
            assertTrue(newCurrency is StatisticsMenuCurrencyType.Default)
        }

    @Test
    fun `onMenuClick with TRANSACTION_TYPE should toggle transaction type`() =
        runTest {
            val viewModel =
                StatisticsViewModel(
                    analyticsSender,
                    testDispatcher,
                    transactionInteractor,
                    currencyInteractor,
                )
            advanceUntilIdle()

            val initialType = viewModel.uiState.value.statisticsMenuState.transactionType
            assertEquals(StatisticsMenuTransactionType.Expense, initialType)

            viewModel.onMenuClick(StatisticsMenuType.TRANSACTION_TYPE)
            advanceUntilIdle()

            val newType = viewModel.uiState.value.statisticsMenuState.transactionType
            assertEquals(StatisticsMenuTransactionType.Income, newType)
        }

    @Test
    fun `onMenuClick with STATISTICS should toggle in statistics filter`() =
        runTest {
            val viewModel =
                StatisticsViewModel(
                    analyticsSender,
                    testDispatcher,
                    transactionInteractor,
                    currencyInteractor,
                )
            advanceUntilIdle()

            val initialInStatistics = viewModel.uiState.value.statisticsMenuState.inStatistics
            assertEquals(StatisticsMenuInStatisticsType.InStatisticsType, initialInStatistics)

            viewModel.onMenuClick(StatisticsMenuType.STATISTICS)
            advanceUntilIdle()

            val newInStatistics = viewModel.uiState.value.statisticsMenuState.inStatistics
            assertEquals(StatisticsMenuInStatisticsType.All, newInStatistics)
        }

    @Test
    fun `onPeriodArrowClicked with forward should move to next period`() =
        runTest {
            val viewModel =
                StatisticsViewModel(
                    analyticsSender,
                    testDispatcher,
                    transactionInteractor,
                    currencyInteractor,
                )
            advanceUntilIdle()

            val initialPeriod = viewModel.uiState.value.periodState.selectedPeriod
            val initialFrom = initialPeriod.from.date

            viewModel.onPeriodArrowClicked(isForward = true)
            advanceUntilIdle()

            val newPeriod = viewModel.uiState.value.periodState.selectedPeriod
            val newFrom = newPeriod.from.date
            assertTrue(newFrom > initialFrom)
        }

    @Test
    fun `onPeriodArrowClicked with backward should move to previous period`() =
        runTest {
            val viewModel =
                StatisticsViewModel(
                    analyticsSender,
                    testDispatcher,
                    transactionInteractor,
                    currencyInteractor,
                )
            advanceUntilIdle()

            viewModel.updatePeriod(StatisticsPeriodModel.WEEK())
            advanceUntilIdle()

            val initialPeriod = viewModel.uiState.value.periodState.selectedPeriod
            val initialFrom = initialPeriod.from.date

            viewModel.onPeriodArrowClicked(isForward = false)
            advanceUntilIdle()

            val newPeriod = viewModel.uiState.value.periodState.selectedPeriod
            val newFrom = newPeriod.from.date
            assertTrue(newFrom < initialFrom)
        }

    @Test
    fun `CUSTOM period should not show navigation arrows`() =
        runTest {
            val viewModel =
                StatisticsViewModel(
                    analyticsSender,
                    testDispatcher,
                    transactionInteractor,
                    currencyInteractor,
                )
            advanceUntilIdle()

            viewModel.updatePeriod(StatisticsPeriodModel.CUSTOM())
            advanceUntilIdle()

            val state = viewModel.uiState.value.periodState
            assertFalse(state.showNextArrow)
            assertFalse(state.showPrevArrow)
        }

    @Test
    fun `init should load transactions and update chart state`() =
        runTest {
            val viewModel =
                StatisticsViewModel(
                    analyticsSender,
                    testDispatcher,
                    transactionInteractor,
                    currencyInteractor,
                )
            advanceUntilIdle()

            val chartState = viewModel.uiState.value.chartState
            assertEquals(1, chartState.pieData.size)
            assertEquals("Food", chartState.pieData.first().label)
            assertEquals(100.0, chartState.pieData.first().data, 0.01)
        }

    @Test
    fun `init should load sum in period and update details state`() =
        runTest {
            val viewModel =
                StatisticsViewModel(
                    analyticsSender,
                    testDispatcher,
                    transactionInteractor,
                    currencyInteractor,
                )
            advanceUntilIdle()

            val detailsState = viewModel.uiState.value.detailsTransactionListState
            assertEquals("100", detailsState.amount.value)
            assertEquals("$", detailsState.amount.currencySymbol)
        }
}
