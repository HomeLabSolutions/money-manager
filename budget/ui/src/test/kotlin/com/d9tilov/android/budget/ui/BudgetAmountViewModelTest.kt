package com.d9tilov.android.budget.ui

import com.d9tilov.android.analytics.domain.AnalyticsSender
import com.d9tilov.android.budget.domain.contract.BudgetInteractor
import com.d9tilov.android.budget.domain.model.BudgetData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
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
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

@OptIn(ExperimentalCoroutinesApi::class)
class BudgetAmountViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val analyticsSender: AnalyticsSender = mockk(relaxed = true)
    private val budgetInteractor: BudgetInteractor = mockk(relaxed = true)

    private val testBudget =
        BudgetData(
            id = 1L,
            clientId = "test-budget-id",
            currencyCode = "USD",
            sum = BigDecimal("1000.00"),
            saveSum = BigDecimal("200.00"),
            createdDate = LocalDateTime(2024, 1, 1, 0, 0),
        )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { budgetInteractor.get() } returns flowOf(testBudget)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should load budget and update UI state`() =
        runTest {
            coEvery { budgetInteractor.get() } returns flowOf(testBudget)

            val viewModel = BudgetAmountViewModel(analyticsSender, budgetInteractor)
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals("1000", state.budgetSum)
            assertEquals("200", state.amountToSave)
            assertEquals("$", state.currencySymbol)
        }

    @Test
    fun `changeBudgetAmount should update budget sum in UI state`() =
        runTest {
            val viewModel = BudgetAmountViewModel(analyticsSender, budgetInteractor)
            advanceUntilIdle()

            viewModel.changeBudgetAmount("1500.50")

            assertEquals("1500.50", viewModel.uiState.value.budgetSum)
        }

    @Test
    fun `changeAmountToSave should update amount to save in UI state`() =
        runTest {
            val viewModel = BudgetAmountViewModel(analyticsSender, budgetInteractor)
            advanceUntilIdle()

            viewModel.changeAmountToSave("300.75")

            assertEquals("300.75", viewModel.uiState.value.amountToSave)
        }

    @Test
    fun `saveBudgetAmount should update budget with new values`() =
        runTest {
            coEvery { budgetInteractor.get() } returns flowOf(testBudget)
            coEvery { budgetInteractor.update(any()) } returns Unit

            val viewModel = BudgetAmountViewModel(analyticsSender, budgetInteractor)
            advanceUntilIdle()

            viewModel.changeBudgetAmount("1500")
            viewModel.changeAmountToSave("300")
            viewModel.saveBudgetAmount()
            advanceUntilIdle()

            coVerify {
                budgetInteractor.update(
                    match { budget ->
                        budget.sum.compareTo(BigDecimal("1500")) == 0 &&
                            budget.saveSum.compareTo(BigDecimal("300")) == 0
                    },
                )
            }
        }

    @Test
    fun `saveBudgetAmount with invalid amount should save zero`() =
        runTest {
            coEvery { budgetInteractor.get() } returns flowOf(testBudget)
            coEvery { budgetInteractor.update(any()) } returns Unit

            val viewModel = BudgetAmountViewModel(analyticsSender, budgetInteractor)
            advanceUntilIdle()

            viewModel.changeBudgetAmount("invalid")
            viewModel.changeAmountToSave("also invalid")
            viewModel.saveBudgetAmount()
            advanceUntilIdle()

            coVerify {
                budgetInteractor.update(
                    match { budget ->
                        budget.sum.compareTo(BigDecimal.ZERO) == 0 &&
                            budget.saveSum.compareTo(BigDecimal.ZERO) == 0
                    },
                )
            }
        }

    @Test
    fun `saveBudgetAmount when budget is null should not call update`() =
        runTest {
            coEvery { budgetInteractor.get() } returns flowOf()

            val viewModel = BudgetAmountViewModel(analyticsSender, budgetInteractor)
            advanceUntilIdle()

            viewModel.saveBudgetAmount()
            advanceUntilIdle()

            coVerify(exactly = 0) { budgetInteractor.update(any()) }
        }
}
