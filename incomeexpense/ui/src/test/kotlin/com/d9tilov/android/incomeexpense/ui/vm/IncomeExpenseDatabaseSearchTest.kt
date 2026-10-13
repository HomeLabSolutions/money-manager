package com.d9tilov.android.incomeexpense.ui.vm

import androidx.paging.PagingData
import com.d9tilov.android.analytics.domain.AnalyticsSender
import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.common.android.location.LocationProvider
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.KeyPress
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class IncomeExpenseDatabaseSearchTest {
    private val dispatcher = StandardTestDispatcher()
    private val transactions = mockk<TransactionInteractor>(relaxed = true)
    private val categories = mockk<CategoryInteractor>(relaxed = true)
    private val currencies = mockk<CurrencyInteractor>(relaxed = true)
    private val billing = mockk<BillingInteractor>(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        every { currencies.getMainCurrencyFlow() } returns emptyFlow()
        every { billing.isPremium() } returns emptyFlow()
        every { categories.getGroupedCategoriesByType(any()) } returns emptyFlow()
        every { transactions.getTransactionsByType(any()) } returns flowOf(PagingData.empty())
        every { transactions.searchTransactionsByType(any(), any()) } returns flowOf(PagingData.empty())
        every { transactions.ableToSpendToday() } returns emptyFlow()
        every { transactions.getApproxSumTodayCurrentCurrency(any()) } returns emptyFlow()
        every { transactions.getApproxSumInFiscalPeriodCurrentCurrency(any()) } returns emptyFlow()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `new query cancels the old database search and clearing restores the unfiltered request`() =
        runTest(dispatcher) {
            val queries = mutableListOf<String>()
            var oldQueryCancelled = false
            every { transactions.getTransactionsByType(TransactionType.EXPENSE) } answers {
                queries.add("")
                flowOf(PagingData.empty())
            }
            every { transactions.searchTransactionsByType(TransactionType.EXPENSE, any()) } answers {
                val query = secondArg<String>()
                flow {
                    queries.add(query)
                    if (query == "old") {
                        try {
                            awaitCancellation()
                        } finally {
                            oldQueryCancelled = true
                        }
                    }
                    emit(PagingData.empty())
                }
            }
            val viewModel = createViewModel()
            runCurrent()
            backgroundScope.launch {
                viewModel.uiStateFlow.value.expenseUiState.expenseTransactions
                    .collect()
            }
            runCurrent()

            viewModel.updateSearchQuery(ScreenType.EXPENSE, " old ")
            runCurrent()
            viewModel.updateSearchQuery(ScreenType.EXPENSE, "new")
            runCurrent()
            assertTrue(oldQueryCancelled)

            viewModel.updateSearchQuery(ScreenType.EXPENSE, " new ")
            viewModel.addNumber(KeyPress.One)
            runCurrent()
            assertEquals(listOf("", "old", "new"), queries)

            viewModel.onSearchClosed(ScreenType.EXPENSE)
            runCurrent()
            assertEquals(listOf("", "old", "new", ""), queries)
            viewModel.updateSearchQuery(ScreenType.EXPENSE, " \t\u00a0 ")
            runCurrent()
            assertEquals(listOf("", "old", "new", ""), queries)
        }

    @Test
    fun `income and expense requests keep separate database queries`() =
        runTest(dispatcher) {
            val requests = mutableListOf<Pair<TransactionType, String>>()
            every { transactions.getTransactionsByType(any()) } answers {
                requests.add(firstArg<TransactionType>() to "")
                flowOf(PagingData.empty())
            }
            every { transactions.searchTransactionsByType(any(), any()) } answers {
                requests.add(firstArg<TransactionType>() to secondArg<String>())
                flowOf(PagingData.empty())
            }
            val viewModel = createViewModel()
            runCurrent()
            backgroundScope.launch {
                viewModel.uiStateFlow.value.expenseUiState.expenseTransactions
                    .collect()
            }
            backgroundScope.launch {
                viewModel.uiStateFlow.value.incomeUiState.incomeTransactions
                    .collect()
            }
            runCurrent()
            requests.clear()

            viewModel.updateSearchQuery(ScreenType.EXPENSE, "groceries")
            runCurrent()
            viewModel.updateSearchQuery(ScreenType.INCOME, "salary")
            runCurrent()
            viewModel.onSearchClosed(ScreenType.EXPENSE)
            runCurrent()

            assertEquals(
                listOf(
                    TransactionType.EXPENSE to "groceries",
                    TransactionType.INCOME to "salary",
                    TransactionType.EXPENSE to "",
                ),
                requests,
            )
            assertEquals("salary", viewModel.uiStateFlow.value.incomeUiState.searchQuery)
        }

    private fun createViewModel(): IncomeExpenseViewModel =
        IncomeExpenseViewModel(
            billingInteractor = billing,
            analyticsSender = mockk<AnalyticsSender>(relaxed = true),
            ioDispatcher = dispatcher,
            currencyInteractor = currencies,
            categoryInteractor = categories,
            transactionInteractor = transactions,
            locationProvider = mockk<LocationProvider>(relaxed = true),
        )
}
