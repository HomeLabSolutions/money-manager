package com.d9tilov.android.transaction.regular.domain.impl

import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.core.model.ExecutionPeriod
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.domain.model.CurrencyMetaData
import com.d9tilov.android.transaction.regular.domain.contract.RegularTransactionInteractor
import com.d9tilov.android.transaction.regular.domain.contract.RegularTransactionRepo
import com.d9tilov.android.transaction.regular.domain.model.RegularTransaction
import com.d9tilov.android.transaction.regular.domain.model.RegularTransactionData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

class RegularTransactionInteractorImplTest {
    private val currencyInteractor: CurrencyInteractor = mockk(relaxed = true)
    private val regularTransactionRepo: RegularTransactionRepo = mockk(relaxed = true)
    private val categoryInteractor: CategoryInteractor = mockk(relaxed = true)
    private val interactor: RegularTransactionInteractor by lazy {
        RegularTransactionInteractorImpl(
            currencyInteractor = currencyInteractor,
            regularTransactionRepo = regularTransactionRepo,
            categoryInteractor = categoryInteractor,
        )
    }

    private val testCurrencyMetaData =
        CurrencyMetaData(
            clientId = "test-currency-client-id",
            code = "USD",
            symbol = "$",
        )

    private val testCategory =
        Category(
            id = 1L,
            clientId = "test-category-client-id",
            name = "Test Category",
            type = TransactionType.EXPENSE,
            icon = 1,
            color = 1,
            usageCount = 0,
            children = emptyList(),
            parent = null,
        )

    private val testRegularTransaction =
        RegularTransaction(
            id = 1L,
            clientId = "test-regular-transaction-client-id",
            currencyCode = "USD",
            type = TransactionType.EXPENSE,
            sum = BigDecimal(100),
            category = testCategory,
            createdDate = LocalDateTime(2024, 1, 1, 0, 0),
            executionPeriod = ExecutionPeriod.EveryMonth(1, LocalDateTime(2024, 1, 1, 0, 0)),
            description = "Test regular transaction",
            pushEnabled = false,
            autoAdd = false,
        )

    private val testRegularTransactionData =
        RegularTransactionData(
            id = 1L,
            type = TransactionType.EXPENSE,
            categoryId = 1L,
            currencyCode = "USD",
            sum = BigDecimal(100),
            description = "Test regular transaction",
            executionPeriod = ExecutionPeriod.EveryMonth(1, LocalDateTime(2024, 1, 1, 0, 0)),
        )

    @Before
    fun setup() {
        every { currencyInteractor.getMainCurrencyFlow() } returns flowOf(testCurrencyMetaData)
        coEvery { categoryInteractor.getCategoryById(1L) } returns testCategory
        coEvery { categoryInteractor.getGroupedCategoriesByType(any()) } returns flowOf(listOf(testCategory))
    }

    @Test
    fun `createDefault should return empty regular transaction with main currency`() =
        runTest {
            val result = interactor.createDefault().first()

            assertEquals("USD", result.currencyCode)
            assertEquals(BigDecimal.ZERO, result.sum)
        }

    @Test
    fun `insert should delegate to repo insert`() =
        runTest {
            coEvery { regularTransactionRepo.insert(any()) } returns Unit

            interactor.insert(testRegularTransaction)

            coVerify(exactly = 1) { regularTransactionRepo.insert(any()) }
        }

    @Test
    fun `getAll should return regular transactions with categories`() =
        runTest {
            coEvery {
                regularTransactionRepo.getAll(TransactionType.EXPENSE)
            } returns flowOf(listOf(testRegularTransactionData))

            val result = interactor.getAll(TransactionType.EXPENSE).first()

            assertEquals(1, result.size)
            assertEquals(testCategory.id, result.first().category.id)
            assertEquals(BigDecimal(100), result.first().sum)
        }

    @Test
    fun `getAll should handle empty list`() =
        runTest {
            coEvery { regularTransactionRepo.getAll(TransactionType.EXPENSE) } returns flowOf(emptyList())

            val result = interactor.getAll(TransactionType.EXPENSE).first()

            assertEquals(0, result.size)
        }

    @Test
    fun `getById should return regular transaction with category`() =
        runTest {
            coEvery { regularTransactionRepo.getById(1L) } returns testRegularTransactionData

            val result = interactor.getById(1L)

            assertNotNull(result)
            assertEquals(1L, result.id)
            assertEquals(testCategory.id, result.category.id)
            assertEquals(BigDecimal(100), result.sum)
        }

    @Test
    fun `update should delegate to repo update`() =
        runTest {
            coEvery { regularTransactionRepo.update(any()) } returns Unit

            interactor.update(testRegularTransaction)

            coVerify(exactly = 1) { regularTransactionRepo.update(any()) }
        }

    @Test
    fun `delete should delegate to repo delete`() =
        runTest {
            coEvery { regularTransactionRepo.delete(any()) } returns Unit

            interactor.delete(testRegularTransaction)

            coVerify(exactly = 1) { regularTransactionRepo.delete(any()) }
        }

    @Test
    fun `removeAllByCategory should delete all transactions for category`() =
        runTest {
            coEvery {
                regularTransactionRepo.getAllByCategory(testCategory)
            } returns listOf(testRegularTransactionData)
            coEvery { regularTransactionRepo.delete(any()) } returns Unit

            interactor.removeAllByCategory(testCategory)

            coVerify(atLeast = 1) { regularTransactionRepo.delete(any()) }
        }
}
