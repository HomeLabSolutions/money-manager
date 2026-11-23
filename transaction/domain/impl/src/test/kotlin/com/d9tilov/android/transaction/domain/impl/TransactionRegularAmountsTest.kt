package com.d9tilov.android.transaction.domain.impl

import com.d9tilov.android.budget.domain.contract.BudgetInteractor
import com.d9tilov.android.budget.domain.model.BudgetData
import com.d9tilov.android.core.model.Decimal
import com.d9tilov.android.core.model.ExecutionPeriod
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.currentDate
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.core.utils.getStartOfDay
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.domain.model.CurrencyMetaData
import com.d9tilov.android.currency.domain.model.CurrencyRate
import com.d9tilov.android.transaction.domain.contract.TransactionRepo
import com.d9tilov.android.transaction.domain.model.TransactionDataModel
import com.d9tilov.android.transaction.domain.model.TransactionSpendingTodayModel
import com.d9tilov.android.transaction.regular.domain.contract.RegularTransactionInteractor
import com.d9tilov.android.transaction.regular.domain.model.RegularTransaction
import com.d9tilov.android.user.domain.contract.UserInteractor
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TransactionRegularAmountsTest {
    private val transactionRepo: TransactionRepo = mockk()
    private val regularTransactionInteractor: RegularTransactionInteractor = mockk()
    private val userInteractor: UserInteractor = mockk()
    private val currencyInteractor: CurrencyInteractor = mockk()
    private val budgetInteractor: BudgetInteractor = mockk()
    private val interactor =
        TransactionInteractorImpl(
            transactionRepo = transactionRepo,
            regularTransactionInteractor = regularTransactionInteractor,
            categoryInteractor = mockk(),
            userInteractor = userInteractor,
            currencyInteractor = currencyInteractor,
            budgetInteractor = budgetInteractor,
        )
    private val annualTransaction =
        RegularTransaction.EMPTY.copy(
            type = TransactionType.INCOME,
            currencyCode = "USD",
            sum = Decimal("3650"),
            createdDate = LocalDate(2023, 1, 1).getStartOfDay(),
            executionPeriod = ExecutionPeriod.EveryYear(month = 12, dayOfMonth = 1),
        )

    @Before
    fun setUp() {
        mockkStatic("com.d9tilov.android.core.utils.DateTimeUtilKt")
        coEvery { currencyInteractor.getMainCurrency() } returns CurrencyMetaData("client-id", "USD", "$")
        every { budgetInteractor.get() } returns flowOf(BudgetData.EMPTY)
        every { regularTransactionInteractor.getAll(TransactionType.EXPENSE) } returns flowOf(emptyList())
        every {
            transactionRepo.getTransactionsByTypeInPeriod(any(), any(), any(), true, false)
        } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        unmockkStatic("com.d9tilov.android.core.utils.DateTimeUtilKt")
    }

    @Test
    fun `annual allowance accrues even when payment falls outside fiscal month`() =
        runTest {
            assertAllowance("10", annualTransaction, LocalDate(2023, 7, 15), fiscalDay = 15)
        }

    @Test
    fun `annual allowance is spread across 366 days in leap year`() =
        runTest {
            val transaction = annualTransaction.copy(sum = Decimal("3660"))
            assertAllowance("10", transaction, LocalDate(2024, 2, 1))
            assertAllowance("10", transaction, LocalDate(2024, 2, 29))
        }

    @Test
    fun `annual daily allowance stays consistent across all fiscal months`() =
        runTest {
            for (month in 1..12) {
                assertAllowance("10", annualTransaction, LocalDate(2023, month, 1))
                assertAllowance(
                    "10",
                    annualTransaction.copy(sum = Decimal("3660")),
                    LocalDate(2024, month, 1),
                )
            }
        }

    @Test
    fun `fiscal period spanning years uses each years daily share`() =
        runTest {
            val transaction = annualTransaction.copy(sum = Decimal("133590"))
            assertAllowance("365.50", transaction, LocalDate(2023, 12, 31), fiscalDay = 2)
        }

    @Test
    fun `annual allowance excludes days before creation`() =
        runTest {
            val transaction = annualTransaction.copy(createdDate = LocalDate(2023, 7, 20).getStartOfDay())
            assertAllowance("3.87", transaction, LocalDate(2023, 7, 1))
            assertAllowance("0", transaction, LocalDate(2023, 6, 1))
        }

    @Test
    fun `daily allowance deducts a converted expense rounded at a half-cent tie`() =
        runTest {
            assertDailyExpenses(listOf("EUR" to "0.07"), mapOf("EUR" to 14))
        }

    @Test
    fun `daily allowance rounds the sum of two half-cent expenses once`() =
        runTest {
            assertDailyExpenses(listOf("EUR" to "0.07", "EUR" to "0.07"), mapOf("EUR" to 14))
        }

    @Test
    fun `daily allowance rounds an exact half-cent total across currencies up`() =
        runTest {
            assertDailyExpenses(
                listOf("EUR" to "0.01", "GBP" to "0.01", "CAD" to "0.02"),
                mapOf("EUR" to 3, "GBP" to 12, "CAD" to 24),
            )
        }

    @Test
    fun `weekly and monthly allowances still count whole payments`() =
        runTest {
            val transaction = annualTransaction.copy(sum = Decimal("28"))
            val weekly = transaction.copy(executionPeriod = ExecutionPeriod.EveryWeek(dayOfWeek = 0))
            val monthly = transaction.copy(executionPeriod = ExecutionPeriod.EveryMonth(dayOfMonth = 15))
            assertAllowance("4", weekly, LocalDate(2023, 2, 1))
            assertAllowance("1", monthly, LocalDate(2023, 2, 1))
        }

    private suspend fun assertDailyExpenses(
        amounts: List<Pair<String, String>>,
        rates: Map<String, Int>,
    ) {
        val today = LocalDate(2024, 2, 15)
        every { currentDateTime() } returns today.getStartOfDay()
        coEvery { userInteractor.getFiscalDay() } returns 1
        every { regularTransactionInteractor.getAll(TransactionType.INCOME) } returns flowOf(emptyList())
        for ((code, rate) in rates + ("USD" to 1)) {
            val currency = CurrencyRate.EMPTY.copy(code = code, value = Decimal(rate))
            coEvery { currencyInteractor.getCurrencyByCode(code) } returns currency
        }
        val expenses =
            amounts.map { (code, amount) ->
                mockk<TransactionDataModel> {
                    every { currencyCode } returns code
                    every { sum } returns Decimal(amount)
                }
            }
        every {
            transactionRepo.getTransactionsByTypeInPeriod(any(), any(), TransactionType.EXPENSE, true, false)
        } answers {
            val from = firstArg<LocalDateTime>()
            flowOf(if (from.date == today) expenses else emptyList())
        }

        val result = interactor.ableToSpendToday().first()

        assertEquals(TransactionSpendingTodayModel.OVERSPENDING(Decimal("-0.01")), result)
    }

    private suspend fun assertAllowance(
        expected: String,
        transaction: RegularTransaction,
        today: LocalDate,
        fiscalDay: Int = 1,
    ) {
        every { currentDateTime() } returns today.getStartOfDay()
        every { currentDate() } returns today
        coEvery { userInteractor.getFiscalDay() } returns fiscalDay
        every { regularTransactionInteractor.getAll(TransactionType.INCOME) } returns flowOf(listOf(transaction))

        val result = interactor.ableToSpendToday().first() as TransactionSpendingTodayModel.NORMAL

        assertEquals(0, Decimal(expected).compareTo(result.trSum))
    }
}
