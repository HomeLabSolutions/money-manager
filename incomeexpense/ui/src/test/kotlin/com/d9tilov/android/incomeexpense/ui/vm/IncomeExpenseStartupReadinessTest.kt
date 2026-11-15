package com.d9tilov.android.incomeexpense.ui.vm

import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.KeyPress
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
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
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
            assertEquals(IncomeExpenseScreenState.Loading, viewModel.uiStateFlow.value)

            currencyUpdates.emit(CurrencyMetaData.EMPTY)
            runCurrent()
            assertEquals(IncomeExpenseScreenState.Loading, viewModel.uiStateFlow.value)

            categoryUpdates.emit(emptyList())
            val ready = viewModel.uiStateFlow.filterIsInstance<IncomeExpenseScreenState.Ready>().first()
            assertTrue(
                ready.uiState.expenseUiState.expenseCategoryList
                    .isEmpty(),
            )
        }

    @Test
    fun `categories loaded before currency keep the screen loading`() =
        runTest(dispatcher) {
            val viewModel = createViewModel()
            categoryUpdates.emit(emptyList())
            runCurrent()
            assertEquals(IncomeExpenseScreenState.Loading, viewModel.uiStateFlow.value)

            currencyUpdates.emit(CurrencyMetaData.EMPTY.copy(code = "EUR"))
            val ready = viewModel.uiStateFlow.filterIsInstance<IncomeExpenseScreenState.Ready>().first()
            assertEquals("EUR", ready.uiState.price.currencyCode)
        }

    @Test
    fun `ready content preserves edits when categories and currency change`() =
        runTest(dispatcher) {
            val viewModel = createViewModel()
            currencyUpdates.emit(CurrencyMetaData.EMPTY)
            categoryUpdates.emit(emptyList())
            val readyStates = viewModel.uiStateFlow.filterIsInstance<IncomeExpenseScreenState.Ready>()
            readyStates.first()

            viewModel.addNumber(KeyPress.One)
            viewModel.updateCurrencyCode("EUR")
            readyStates.first { it.uiState.price.currencyCode == "EUR" }

            val updatedCategories = listOf(Category.EMPTY_EXPENSE.copy(name = "Updated"))
            categoryUpdates.emit(updatedCategories)
            val categoryUpdate =
                readyStates.first { it.uiState.expenseUiState.expenseCategoryList == updatedCategories }.uiState
            assertEquals("1", categoryUpdate.price.value)
            assertEquals("EUR", categoryUpdate.price.currencyCode)

            currencyUpdates.emit(CurrencyMetaData.EMPTY.copy(code = "GBP"))
            val currencyUpdate = readyStates.first { it.uiState.price.currencyCode == "GBP" }.uiState
            assertEquals("1", currencyUpdate.price.value)
            assertEquals(updatedCategories, currencyUpdate.expenseUiState.expenseCategoryList)
        }

    @Test
    fun `edits made during loading are present in the first ready state`() =
        runTest(dispatcher) {
            val viewModel = createViewModel()
            viewModel.addNumber(KeyPress.One)
            viewModel.updateMode(EditMode.LIST)
            currencyUpdates.emit(CurrencyMetaData.EMPTY)
            categoryUpdates.emit(emptyList())

            val ready =
                viewModel.uiStateFlow
                    .filterIsInstance<IncomeExpenseScreenState.Ready>()
                    .first()
                    .uiState
            assertEquals("1", ready.price.value)
            assertEquals(EditMode.LIST, ready.mode)
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
