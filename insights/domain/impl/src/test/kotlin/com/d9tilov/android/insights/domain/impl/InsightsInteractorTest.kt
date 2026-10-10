package com.d9tilov.android.insights.domain.impl

import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.core.utils.getEndOfDay
import com.d9tilov.android.core.utils.getStartOfDay
import com.d9tilov.android.insights.domain.contract.InsightLanguageRepo
import com.d9tilov.android.insights.domain.contract.InsightsConsentRepo
import com.d9tilov.android.insights.domain.contract.InsightsRepo
import com.d9tilov.android.insights.domain.model.InsightsConstants.INSIGHT_WINDOW_DAYS
import com.d9tilov.android.transaction.domain.contract.TransactionRepo
import com.d9tilov.android.transaction.domain.model.TransactionDataModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class InsightsInteractorTest {
    private val transactionRepo = mockk<TransactionRepo>()
    private val insightsRepo = mockk<InsightsRepo>()
    private val consentRepo = mockk<InsightsConsentRepo>()
    private val languageRepo = mockk<InsightLanguageRepo>()
    private val interactor =
        InsightsInteractorImpl(transactionRepo, insightsRepo, consentRepo, languageRepo)
    private val from = currentDateTime().date.getStartOfDay()
    private val periodStart = from.date.minus(INSIGHT_WINDOW_DAYS - 1, DateTimeUnit.DAY).getStartOfDay()
    private val to = from.date.getEndOfDay()
    private val income = MutableStateFlow<List<TransactionDataModel>>(emptyList())
    private val expense = MutableStateFlow<List<TransactionDataModel>>(emptyList())
    private val transaction = mockk<TransactionDataModel> { every { date } returns from }

    @Before
    fun setUp() {
        every { insightsRepo.insightPeriod() } returns Pair(periodStart, currentDateTime())
        every {
            transactionRepo.getTransactionsByTypeInPeriod(periodStart, to, TransactionType.INCOME, true, true)
        } returns income
        every {
            transactionRepo.getTransactionsByTypeInPeriod(periodStart, to, TransactionType.EXPENSE, true, true)
        } returns expense
    }

    @Test
    fun `minimum count accepts twenty transactions on one day and observes both types`() =
        runTest {
            val results = mutableListOf<Boolean>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                interactor.hasEnoughTransactionsForInsight().collect { results.add(it) }
            }
            runCurrent()
            assertEquals(false, results.last())
            income.value = List(10) { transaction }
            expense.value = List(9) { transaction }
            runCurrent()
            assertEquals(false, results.last())
            expense.value = List(10) { transaction }
            runCurrent()
            assertEquals(true, results.last())
            expense.value = List(11) { transaction }
            runCurrent()
            assertEquals(true, results.last())
            income.value = emptyList()
            runCurrent()
            assertEquals(false, results.last())

            verify {
                transactionRepo.getTransactionsByTypeInPeriod(periodStart, to, TransactionType.INCOME, true, true)
            }
            verify {
                transactionRepo.getTransactionsByTypeInPeriod(periodStart, to, TransactionType.EXPENSE, true, true)
            }
        }

    @Test
    fun `future transactions do not count toward minimum`() =
        runTest {
            val future =
                mockk<TransactionDataModel> {
                    every { date } returns from.date.plus(1, DateTimeUnit.DAY).getStartOfDay()
                }
            income.value = List(19) { transaction }
            expense.value = listOf(future)
            assertEquals(false, interactor.hasEnoughTransactionsForInsight().first())
            expense.value = listOf(transaction)
            assertEquals(true, interactor.hasEnoughTransactionsForInsight().first())
        }

    @Test
    fun `minimum count can be met by either income or expense alone`() =
        runTest {
            income.value = List(20) { transaction }
            assertEquals(true, interactor.hasEnoughTransactionsForInsight().first())
            income.value = emptyList()
            expense.value = List(20) { transaction }
            assertEquals(true, interactor.hasEnoughTransactionsForInsight().first())
        }
}
