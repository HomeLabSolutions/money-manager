package com.d9tilov.android.insights.data.impl

import com.d9tilov.android.category.data.contract.CategorySource
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.core.utils.getStartOfDay
import com.d9tilov.android.insights.data.contract.InsightLanguageSource
import com.d9tilov.android.insights.data.contract.InsightSource
import com.d9tilov.android.insights.data.contract.InsightsAggregator
import com.d9tilov.android.insights.data.contract.InsightsRemoteSource
import com.d9tilov.android.insights.data.contract.model.InsightResponse
import com.d9tilov.android.insights.domain.model.exception.DailyInsightLimitException
import com.d9tilov.android.insights.domain.model.exception.InsufficientInsightsDataException
import com.d9tilov.android.transaction.data.contract.TransactionSource
import com.d9tilov.android.transaction.domain.model.TransactionDataModel
import com.d9tilov.android.user.domain.model.InsightLanguage
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

class InsightTransactionCountTest {
    private val local = mockk<InsightSource>()
    private val language = mockk<InsightLanguageSource>()
    private val source = mockk<TransactionSource>()
    private val categories = mockk<CategorySource>()
    private val remote = mockk<InsightsRemoteSource>()
    private val aggregator = mockk<InsightsAggregator>()
    private val repository = InsightsDataRepo(local, language, source, categories, remote, aggregator)
    private val expenses = MutableStateFlow<List<TransactionDataModel>>(emptyList())
    private val transaction =
        mockk<TransactionDataModel> {
            every { date } returns currentDateTime().date.getStartOfDay()
            every { type } returns TransactionType.EXPENSE
            every { categoryId } returns 1L
            every { currencyCode } returns "USD"
            every { sum } returns BigDecimal.ONE
        }

    @Before
    fun setUp() {
        coEvery { local.isDailyLimitReached() } returns false
        every { local.history() } returns flowOf(emptyList())
        coEvery { local.save(any()) } returns Unit
        every { language.language } returns flowOf(InsightLanguage.ENGLISH)
        every {
            source.getAllByTypeInPeriod(any(), any(), TransactionType.INCOME, true, true)
        } returns flowOf(emptyList())
        every { source.getAllByTypeInPeriod(any(), any(), TransactionType.EXPENSE, true, true) } returns expenses
        every { categories.getCategoriesByType(any()) } returns flowOf(emptyList())
        every { aggregator.aggregateTransactions(any()) } returns emptyMap()
        coEvery { remote.generate(any(), any(), any(), any(), any()) } returns InsightResponse("Generated insight")
    }

    @Test
    fun `nineteen transactions block generation and twenty on one day allow it`() =
        runTest {
            expenses.value = List(19) { transaction }
            assertTrue(runCatching { repository.generate() }.exceptionOrNull() is InsufficientInsightsDataException)
            coVerify(exactly = 0) { remote.generate(any(), any(), any(), any(), any()) }

            expenses.value = List(20) { transaction }
            assertEquals("Generated insight", repository.generate().text)
            coVerify(exactly = 1) { local.save(any()) }

            expenses.value = List(21) { transaction }
            assertEquals("Generated insight", repository.generate().text)
            coVerify(exactly = 2) { remote.generate(any(), any(), any(), any(), any()) }
        }

    @Test
    fun `future transaction cannot complete the required count`() =
        runTest {
            val future =
                mockk<TransactionDataModel> {
                    every { date } returns currentDateTime().date.plus(1, DateTimeUnit.DAY).getStartOfDay()
                }
            expenses.value = List(19) { transaction } + future
            assertTrue(runCatching { repository.generate() }.exceptionOrNull() is InsufficientInsightsDataException)
            coVerify(exactly = 0) { remote.generate(any(), any(), any(), any(), any()) }
        }

    @Test
    fun `daily limit still prevents generation when transaction count is sufficient`() =
        runTest {
            expenses.value = List(20) { transaction }
            coEvery { local.isDailyLimitReached() } returns true
            assertTrue(runCatching { repository.generate() }.exceptionOrNull() is DailyInsightLimitException)
            coVerify(exactly = 0) { remote.generate(any(), any(), any(), any(), any()) }
        }
}
