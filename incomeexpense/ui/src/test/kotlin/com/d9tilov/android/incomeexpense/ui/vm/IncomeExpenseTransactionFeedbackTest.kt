package com.d9tilov.android.incomeexpense.ui.vm

import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.common.android.location.LocationProvider
import com.d9tilov.android.core.model.LocationData
import com.d9tilov.android.core.utils.KeyPress
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

@OptIn(ExperimentalCoroutinesApi::class)
class IncomeExpenseTransactionFeedbackTest {
    private val dispatcher = StandardTestDispatcher()
    private val transactions = mockk<TransactionInteractor>(relaxed = true)
    private val categories = mockk<CategoryInteractor>(relaxed = true)
    private val currencies = mockk<CurrencyInteractor>(relaxed = true)
    private val billing = mockk<BillingInteractor>(relaxed = true)
    private val locations = mockk<LocationProvider>(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        every { currencies.getMainCurrencyFlow() } returns emptyFlow()
        every { billing.isPremium() } returns emptyFlow()
        every { categories.getGroupedCategoriesByType(any()) } returns emptyFlow()
        every { transactions.getTransactionsByType(any()) } returns emptyFlow()
        every { transactions.ableToSpendToday() } returns emptyFlow()
        every { transactions.getApproxSumTodayCurrentCurrency(any()) } returns emptyFlow()
        every { transactions.getApproxSumInFiscalPeriodCurrentCurrency(any()) } returns emptyFlow()
        coEvery { categories.getCategoryById(CATEGORY_ID) } returns Category.EMPTY_EXPENSE.copy(id = CATEGORY_ID)
        coEvery { locations.getCurrentLocation(any()) } returns flowOf(LocationData.EMPTY)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `callback is invoked once and only after the transaction is saved`() =
        runTest(dispatcher) {
            val allowSave = CompletableDeferred<Unit>()
            coEvery { transactions.addTransaction(any()) } coAnswers { allowSave.await() }
            val viewModel = createViewModel()
            var callbackCount = 0

            viewModel.addNumber(KeyPress.One)
            viewModel.addTransaction(CATEGORY_ID) { callbackCount++ }
            runCurrent()

            coVerify(exactly = 1) { transactions.addTransaction(any()) }
            assertEquals(0, callbackCount)

            allowSave.complete(Unit)
            runCurrent()

            assertEquals(1, callbackCount)
        }

    @Test
    fun `a zero amount neither saves a transaction nor invokes its callback`() =
        runTest(dispatcher) {
            val viewModel = createViewModel()
            var callbackCount = 0

            viewModel.addTransaction(CATEGORY_ID) { callbackCount++ }
            runCurrent()

            coVerify(exactly = 0) { transactions.addTransaction(any()) }
            assertEquals(0, callbackCount)
        }

    @Test
    fun `concurrent additions invoke their own callbacks in save completion order`() =
        runTest(dispatcher) {
            val allowFirstSave = CompletableDeferred<Unit>()
            coEvery {
                transactions.addTransaction(match { it.sum.compareTo(BigDecimal.ONE) == 0 })
            } coAnswers { allowFirstSave.await() }
            val viewModel = createViewModel()
            val callbacks = mutableListOf<String>()

            viewModel.addNumber(KeyPress.One)
            viewModel.addTransaction(CATEGORY_ID) { callbacks.add("first") }
            viewModel.addNumber(KeyPress.Two)
            viewModel.addTransaction(CATEGORY_ID) { callbacks.add("second") }
            runCurrent()

            assertEquals(listOf("second"), callbacks)

            allowFirstSave.complete(Unit)
            runCurrent()

            assertEquals(listOf("second", "first"), callbacks)
            coVerify(exactly = 2) { transactions.addTransaction(any()) }
        }

    private fun createViewModel(): IncomeExpenseViewModel =
        IncomeExpenseViewModel(
            billingInteractor = billing,
            analyticsSender = mockk(relaxed = true),
            ioDispatcher = dispatcher,
            currencyInteractor = currencies,
            categoryInteractor = categories,
            transactionInteractor = transactions,
            locationProvider = locations,
        )

    private companion object {
        const val CATEGORY_ID = 123L
    }
}
