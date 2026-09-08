package com.d9tilov.android.transaction.regular.ui.vm

import androidx.lifecycle.SavedStateHandle
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.core.constants.NavigationConstants
import com.d9tilov.android.core.model.ExecutionPeriod
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.currentDate
import com.d9tilov.android.core.utils.getStartOfDay
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.transaction.regular.domain.contract.RegularTransactionInteractor
import com.d9tilov.android.transaction.regular.domain.model.RegularTransaction
import com.d9tilov.android.transaction.regular.ui.navigator.REGULAR_TRANSACTION_ID_ARGS
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class RegularTransactionCreationViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val regularTransactionInteractor: RegularTransactionInteractor = mockk(relaxed = true)
    private val categoryInteractor: CategoryInteractor = mockk(relaxed = true)
    private val currencyInteractor: CurrencyInteractor = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `saving an unchanged schedule preserves its last execution date`() =
        runTest(testDispatcher) {
            val lastExecutionDate = LocalDateTime(2026, 8, 14, 0, 0)
            val transaction = transaction(ExecutionPeriod.EveryWeek(DaysInWeek.FRIDAY.ordinal, lastExecutionDate))
            coEvery { regularTransactionInteractor.getById(TRANSACTION_ID) } returns transaction
            val viewModel = createViewModel()
            advanceUntilIdle()

            viewModel.updateDescription("Updated description")
            viewModel.saveOrUpdate()
            advanceUntilIdle()

            coVerify(exactly = 1) {
                regularTransactionInteractor.insert(
                    match {
                        it.description == "Updated description" &&
                            it.executionPeriod ==
                            ExecutionPeriod.EveryWeek(DaysInWeek.FRIDAY.ordinal, lastExecutionDate)
                    },
                )
            }
        }

    @Test
    fun `changing the schedule updates the transaction and resets its execution watermark`() =
        runTest(testDispatcher) {
            val lastExecutionDate = LocalDateTime(2026, 8, 14, 0, 0)
            val transaction = transaction(ExecutionPeriod.EveryWeek(DaysInWeek.FRIDAY.ordinal, lastExecutionDate))
            coEvery { regularTransactionInteractor.getById(TRANSACTION_ID) } returns transaction
            val viewModel = createViewModel()
            advanceUntilIdle()

            viewModel.updateWeekDay(DaysInWeek.MONDAY)
            val editedPeriod = viewModel.uiState.value.transaction.executionPeriod
            assertEquals(
                ExecutionPeriod.EveryWeek(DaysInWeek.MONDAY.ordinal, currentDate().getStartOfDay()),
                editedPeriod,
            )

            viewModel.saveOrUpdate()
            advanceUntilIdle()

            coVerify(exactly = 1) {
                regularTransactionInteractor.insert(
                    match {
                        it.executionPeriod ==
                            ExecutionPeriod.EveryWeek(
                                DaysInWeek.MONDAY.ordinal,
                                currentDate().getStartOfDay(),
                            )
                    },
                )
            }
        }

    @Test
    fun `changing period type keeps the schedule in the transaction`() =
        runTest(testDispatcher) {
            val lastExecutionDate = LocalDateTime(2026, 8, 14, 0, 0)
            val transaction = transaction(ExecutionPeriod.EveryMonth(14, lastExecutionDate))
            coEvery { regularTransactionInteractor.getById(TRANSACTION_ID) } returns transaction
            val viewModel = createViewModel()
            advanceUntilIdle()

            viewModel.updateCurPeriodItem(PeriodMenuItem.DAY)

            assertEquals(
                ExecutionPeriod.EveryDay(currentDate().getStartOfDay()),
                viewModel.uiState.value.transaction.executionPeriod,
            )
        }

    private fun createViewModel() =
        RegularTransactionCreationViewModel(
            savedStateHandle =
                SavedStateHandle(
                    mapOf(
                        NavigationConstants.TRANSACTION_TYPE_ARG to TransactionType.EXPENSE.value,
                        REGULAR_TRANSACTION_ID_ARGS to TRANSACTION_ID,
                    ),
                ),
            regularTransactionInteractor = regularTransactionInteractor,
            categoryInteractor = categoryInteractor,
            currencyInteractor = currencyInteractor,
        )

    private fun transaction(executionPeriod: ExecutionPeriod) =
        RegularTransaction(
            id = TRANSACTION_ID,
            clientId = "client-id",
            currencyCode = "GBP",
            type = TransactionType.EXPENSE,
            sum = BigDecimal("25.50"),
            category = Category.EMPTY_EXPENSE,
            createdDate = LocalDateTime(2026, 1, 1, 0, 0),
            executionPeriod = executionPeriod,
            description = "Description",
            pushEnabled = true,
            autoAdd = true,
        )

    private companion object {
        const val TRANSACTION_ID = 42L
    }
}
