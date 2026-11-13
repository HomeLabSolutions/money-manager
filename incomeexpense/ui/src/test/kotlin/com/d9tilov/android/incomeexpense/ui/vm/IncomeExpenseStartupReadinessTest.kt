package com.d9tilov.android.incomeexpense.ui.vm

import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.domain.model.CurrencyMetaData
import com.d9tilov.android.transaction.domain.contract.ObserveSpendingSummaryUseCase
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class IncomeExpenseStartupReadinessTest {
    private val dispatcher = StandardTestDispatcher()
    private val currencyUpdates = MutableSharedFlow<CurrencyMetaData>(replay = 1)
    private val categoryUpdates = MutableSharedFlow<List<Category>>(replay = 1)

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `screen readiness waits for currency and categories and accepts an empty category list`() =
        runTest(dispatcher) {
            val viewModel = createViewModel()
            runCurrent()
            assertFalse(viewModel.uiStateFlow.value.isContentReady)

            currencyUpdates.emit(CurrencyMetaData.EMPTY)
            val currencyLoaded = viewModel.uiStateFlow.first { it.isMainCurrencyLoaded }
            assertFalse(currencyLoaded.isContentReady)

            categoryUpdates.emit(emptyList())
            val ready = viewModel.uiStateFlow.first { it.isContentReady }
            assertTrue(ready.expenseUiState.expenseCategoryList.isEmpty())
        }

    private fun createViewModel(): IncomeExpenseViewModel {
        val currencies =
            mockk<CurrencyInteractor> {
                every { getMainCurrencyFlow() } returns currencyUpdates
            }
        val categories =
            mockk<CategoryInteractor> {
                every { getGroupedCategoriesByType(any()) } returns emptyFlow()
                every { getGroupedCategoriesByType(TransactionType.EXPENSE) } returns categoryUpdates
            }
        val billing =
            mockk<BillingInteractor> {
                every { isPremium() } returns emptyFlow()
            }
        val summaries = mockk<ObserveSpendingSummaryUseCase>()
        every { summaries() } returns emptyFlow()
        val transactions =
            mockk<TransactionInteractor>(relaxed = true) {
                every { getApproxSumInFiscalPeriodCurrentCurrency(any()) } returns emptyFlow()
            }
        return IncomeExpenseViewModel(
            billingInteractor = billing,
            analyticsSender = mockk(relaxed = true),
            ioDispatcher = dispatcher,
            currencyInteractor = currencies,
            categoryInteractor = categories,
            transactionInteractor = transactions,
            observeSpendingSummary = summaries,
            locationProvider = mockk(relaxed = true),
        )
    }
}
