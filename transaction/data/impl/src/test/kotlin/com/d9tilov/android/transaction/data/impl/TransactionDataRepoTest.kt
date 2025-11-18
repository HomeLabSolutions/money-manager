package com.d9tilov.android.transaction.data.impl

import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.core.model.LocationData
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.transaction.data.contract.TransactionSource
import com.d9tilov.android.transaction.domain.contract.TransactionRepo
import com.d9tilov.android.transaction.domain.model.TransactionDataModel
import com.d9tilov.android.transaction.domain.model.TransactionMinMaxDateModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class TransactionDataRepoTest {
    private val transactionSource: TransactionSource = mockk(relaxed = true)
    private val repository: TransactionRepo by lazy { TransactionDataRepo(transactionSource) }

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

    private val testTransaction =
        TransactionDataModel(
            id = 1L,
            clientId = "test-client-id",
            type = TransactionType.EXPENSE,
            categoryId = 1L,
            currencyCode = "USD",
            sum = BigDecimal(100),
            usdSum = BigDecimal(100),
            date = LocalDateTime(2024, 1, 1, 0, 0),
            description = "Test",
            qrCode = "",
            inStatistics = true,
            isRegular = false,
            location = LocationData(0.0, 0.0),
            photoUri = "",
        )

    @Test
    fun `addTransaction should delegate to transactionSource insert`() =
        runTest {
            coEvery { transactionSource.insert(testTransaction) } returns Unit

            repository.addTransaction(testTransaction)

            coVerify(exactly = 1) { transactionSource.insert(testTransaction) }
        }

    @Test
    fun `getTransactionById should return transaction from source`() =
        runTest {
            coEvery { transactionSource.getById(1L) } returns flowOf(testTransaction)

            val result = repository.getTransactionById(1L).first()

            assertEquals(testTransaction.id, result.id)
            assertEquals(testTransaction.sum, result.sum)
            coVerify(exactly = 1) { transactionSource.getById(1L) }
        }

    @Test
    fun `getTransactionsByTypeInPeriod should return transactions from source`() =
        runTest {
            val from = LocalDateTime(2024, 1, 1, 0, 0)
            val to = LocalDateTime(2024, 1, 31, 23, 59)
            val transactions = listOf(testTransaction)

            coEvery {
                transactionSource.getAllByTypeInPeriod(from, to, TransactionType.EXPENSE, true, true)
            } returns flowOf(transactions)

            val result = repository.getTransactionsByTypeInPeriod(from, to, TransactionType.EXPENSE, true, true).first()

            assertEquals(1, result.size)
            assertEquals(testTransaction.id, result.first().id)
            coVerify(exactly = 1) {
                transactionSource.getAllByTypeInPeriod(from, to, TransactionType.EXPENSE, true, true)
            }
        }

    @Test
    fun `getAllByCategory should return transactions for category`() =
        runTest {
            val transactions = listOf(testTransaction)
            coEvery { transactionSource.getAllByCategory(testCategory) } returns transactions

            val result = repository.getAllByCategory(testCategory)

            assertEquals(1, result.size)
            assertEquals(testTransaction.id, result.first().id)
            coVerify(exactly = 1) { transactionSource.getAllByCategory(testCategory) }
        }

    @Test
    fun `getByCategoryInPeriod should return transactions for category in period`() =
        runTest {
            val from = LocalDateTime(2024, 1, 1, 0, 0)
            val to = LocalDateTime(2024, 1, 31, 23, 59)
            val transactions = listOf(testTransaction)

            coEvery {
                transactionSource.getByCategoryInPeriod(testCategory, from, to, true)
            } returns flowOf(transactions)

            val result = repository.getByCategoryInPeriod(testCategory, from, to, true).first()

            assertEquals(1, result.size)
            assertEquals(testTransaction.id, result.first().id)
            coVerify(exactly = 1) {
                transactionSource.getByCategoryInPeriod(testCategory, from, to, true)
            }
        }

    @Test
    fun `getTransactionMinMaxDate should return min max dates from source`() =
        runTest {
            val minMaxDate =
                TransactionMinMaxDateModel(
                    minDate = LocalDateTime(2024, 1, 1, 0, 0),
                    maxDate = LocalDateTime(2024, 12, 31, 23, 59),
                )

            coEvery { transactionSource.getMinMaxDate() } returns minMaxDate

            val result = repository.getTransactionMinMaxDate()

            assertEquals(minMaxDate.minDate, result.minDate)
            assertEquals(minMaxDate.maxDate, result.maxDate)
            coVerify(exactly = 1) { transactionSource.getMinMaxDate() }
        }

    @Test
    fun `update should delegate to transactionSource update`() =
        runTest {
            coEvery { transactionSource.update(testTransaction) } returns Unit

            repository.update(testTransaction)

            coVerify(exactly = 1) { transactionSource.update(testTransaction) }
        }

    @Test
    fun `removeTransaction should delegate to transactionSource remove`() =
        runTest {
            coEvery { transactionSource.remove(testTransaction) } returns Unit

            repository.removeTransaction(testTransaction)

            coVerify(exactly = 1) { transactionSource.remove(testTransaction) }
        }

    @Test
    fun `clearAll should delegate to transactionSource clearAll`() =
        runTest {
            coEvery { transactionSource.clearAll() } returns Unit

            repository.clearAll()

            coVerify(exactly = 1) { transactionSource.clearAll() }
        }
}
