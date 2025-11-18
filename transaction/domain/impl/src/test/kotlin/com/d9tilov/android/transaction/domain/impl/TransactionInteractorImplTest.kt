package com.d9tilov.android.transaction.domain.impl

import com.d9tilov.android.budget.domain.contract.BudgetInteractor
import com.d9tilov.android.budget.domain.model.BudgetData
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.core.model.LocationData
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.domain.model.Currency
import com.d9tilov.android.currency.domain.model.CurrencyMetaData
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.contract.TransactionRepo
import com.d9tilov.android.transaction.domain.model.Transaction
import com.d9tilov.android.transaction.domain.model.TransactionDataModel
import com.d9tilov.android.transaction.domain.model.TransactionMinMaxDateModel
import com.d9tilov.android.transaction.regular.domain.contract.RegularTransactionInteractor
import com.d9tilov.android.user.domain.contract.UserInteractor
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

class TransactionInteractorImplTest {
    private val transactionRepo: TransactionRepo = mockk(relaxed = true)
    private val regularTransactionInteractor: RegularTransactionInteractor = mockk(relaxed = true)
    private val categoryInteractor: CategoryInteractor = mockk(relaxed = true)
    private val userInteractor: UserInteractor = mockk(relaxed = true)
    private val currencyInteractor: CurrencyInteractor = mockk(relaxed = true)
    private val budgetInteractor: BudgetInteractor = mockk(relaxed = true)
    private val interactor: TransactionInteractor by lazy {
        TransactionInteractorImpl(
            transactionRepo = transactionRepo,
            regularTransactionInteractor = regularTransactionInteractor,
            categoryInteractor = categoryInteractor,
            userInteractor = userInteractor,
            currencyInteractor = currencyInteractor,
            budgetInteractor = budgetInteractor,
        )
    }

    private val testCurrencyMetaData =
        CurrencyMetaData(
            clientId = "test-currency-client-id",
            code = "USD",
            symbol = "$",
        )

    private val testCurrency =
        Currency(
            code = "USD",
            symbol = "$",
            value = BigDecimal.ONE,
            lastUpdateTime = 0L,
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

    private val testBudget =
        BudgetData(
            id = 1L,
            clientId = "test-client-id",
            sum = BigDecimal(1000),
            saveSum = BigDecimal(100),
            currencyCode = "USD",
            createdDate = LocalDateTime(2024, 1, 1, 0, 0),
        )

    @Before
    fun setup() {
        coEvery { currencyInteractor.getMainCurrency() } returns testCurrencyMetaData
        every { currencyInteractor.getMainCurrencyFlow() } returns flowOf(testCurrencyMetaData)
        coEvery { currencyInteractor.toUsd(any(), any()) } returns BigDecimal.TEN
        coEvery { currencyInteractor.toTargetCurrency(any(), any(), any()) } returns BigDecimal.TEN
        coEvery { currencyInteractor.getCurrencyByCode(any()) } returns testCurrency
        coEvery { categoryInteractor.getCategoryById(any()) } returns testCategory
        coEvery { budgetInteractor.get() } returns flowOf(testBudget)
    }

    @Test
    fun `addTransaction should add transaction and update category usage count`() =
        runTest {
            val transaction =
                Transaction.EMPTY.copy(
                    type = TransactionType.EXPENSE,
                    category = testCategory,
                    sum = BigDecimal(100),
                    currencyCode = "USD",
                )

            coEvery { categoryInteractor.getCategoryById(1L) } returns testCategory
            coEvery { categoryInteractor.update(any()) } returns Unit
            coEvery { transactionRepo.addTransaction(any()) } returns Unit

            interactor.addTransaction(transaction)

            coVerify { transactionRepo.addTransaction(any()) }
            coVerify { categoryInteractor.update(match { it.usageCount == 1 }) }
            coVerify { budgetInteractor.update(any()) }
        }

    @Test
    fun `getTransactionById should return transaction with category`() =
        runTest {
            val transactionDataModel =
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

            coEvery { transactionRepo.getTransactionById(1L) } returns flowOf(transactionDataModel)

            val transaction = interactor.getTransactionById(1L).first()

            assertEquals(1L, transaction.id)
            assertEquals(testCategory, transaction.category)
            assertEquals(BigDecimal(100), transaction.sum)
            assertEquals(TransactionType.EXPENSE, transaction.type)
        }

    @Test
    fun `getTransactionsGroupedByCategory should group transactions by category`() =
        runTest {
            val transactionDataModel =
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

            val from = LocalDateTime(2024, 1, 1, 0, 0)
            val to = LocalDateTime(2024, 1, 31, 23, 59)

            coEvery {
                categoryInteractor.getGroupedCategoriesByType(TransactionType.EXPENSE)
            } returns flowOf(listOf(testCategory))

            coEvery {
                transactionRepo.getTransactionsByTypeInPeriod(from, to, TransactionType.EXPENSE, true)
            } returns flowOf(listOf(transactionDataModel))

            coEvery {
                currencyInteractor.toTargetCurrency(any(), any(), any())
            } returns BigDecimal(100)

            val result =
                interactor
                    .getTransactionsGroupedByCategory(
                        type = TransactionType.EXPENSE,
                        from = from,
                        to = to,
                        currencyCode = "USD",
                        inStatistics = true,
                        onlySubcategories = false,
                    ).first()

            assertEquals(1, result.size)
            assertEquals(testCategory.name, result.first().category.name)
            assertEquals(BigDecimal(100), result.first().sum)
        }

    @Test
    fun `update should update transaction and adjust budget`() =
        runTest {
            val oldTransaction =
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

            val updatedTransaction =
                Transaction.EMPTY.copy(
                    id = 1L,
                    type = TransactionType.EXPENSE,
                    category = testCategory,
                    sum = BigDecimal(150),
                    currencyCode = "USD",
                )

            coEvery { transactionRepo.getTransactionById(1L) } returns flowOf(oldTransaction)
            coEvery { transactionRepo.update(any()) } returns Unit
            coEvery { currencyInteractor.toUsd(any(), any()) } returns BigDecimal(150)

            interactor.update(updatedTransaction)

            coVerify { transactionRepo.update(any()) }
            coVerify { budgetInteractor.update(any()) }
        }

    @Test
    fun `removeTransaction should remove transaction and update budget`() =
        runTest {
            val transaction =
                Transaction.EMPTY.copy(
                    id = 1L,
                    type = TransactionType.EXPENSE,
                    category = testCategory,
                    sum = BigDecimal(100),
                    currencyCode = "USD",
                )

            coEvery { transactionRepo.removeTransaction(any()) } returns Unit
            coEvery { currencyInteractor.toTargetCurrency(any(), any(), any()) } returns BigDecimal(100)

            interactor.removeTransaction(transaction)

            coVerify { transactionRepo.removeTransaction(any()) }
            coVerify { budgetInteractor.update(any()) }
        }

    @Test
    fun `getTransactionMinMaxDate should return min max dates`() =
        runTest {
            val minMaxDateModel =
                TransactionMinMaxDateModel(
                    minDate = LocalDateTime(2024, 1, 1, 0, 0),
                    maxDate = LocalDateTime(2024, 12, 31, 23, 59),
                )

            coEvery { transactionRepo.getTransactionMinMaxDate() } returns minMaxDateModel

            val result = interactor.getTransactionMinMaxDate()

            assertEquals(minMaxDateModel.minDate, result.minDate)
            assertEquals(minMaxDateModel.maxDate, result.maxDate)
        }

    @Test
    fun `clearAll should call repo clearAll`() =
        runTest {
            coEvery { transactionRepo.clearAll() } returns Unit

            interactor.clearAll()

            coVerify { transactionRepo.clearAll() }
        }

    @Test
    fun `getTransactionsGroupedByDate should group transactions by date`() =
        runTest {
            val transactionDataModel1 =
                TransactionDataModel(
                    id = 1L,
                    clientId = "test-client-id-1",
                    type = TransactionType.EXPENSE,
                    categoryId = 1L,
                    currencyCode = "USD",
                    sum = BigDecimal(100),
                    usdSum = BigDecimal(100),
                    date = LocalDateTime(2024, 1, 1, 10, 0),
                    description = "Test",
                    qrCode = "",
                    inStatistics = true,
                    isRegular = false,
                    location = LocationData(0.0, 0.0),
                    photoUri = "",
                )

            val transactionDataModel2 =
                TransactionDataModel(
                    id = 2L,
                    clientId = "test-client-id-2",
                    type = TransactionType.EXPENSE,
                    categoryId = 1L,
                    currencyCode = "USD",
                    sum = BigDecimal(50),
                    usdSum = BigDecimal(50),
                    date = LocalDateTime(2024, 1, 1, 15, 0),
                    description = "Test 2",
                    qrCode = "",
                    inStatistics = true,
                    isRegular = false,
                    location = LocationData(0.0, 0.0),
                    photoUri = "",
                )

            val from = LocalDateTime(2024, 1, 1, 0, 0)
            val to = LocalDateTime(2024, 1, 31, 23, 59)

            coEvery {
                transactionRepo.getTransactionsByTypeInPeriod(from, to, TransactionType.EXPENSE, true)
            } returns flowOf(listOf(transactionDataModel1, transactionDataModel2))

            coEvery {
                currencyInteractor.toTargetCurrency(BigDecimal(100), "USD", "USD")
            } returns BigDecimal(100)
            coEvery {
                currencyInteractor.toTargetCurrency(BigDecimal(50), "USD", "USD")
            } returns BigDecimal(50)

            val result =
                interactor
                    .getTransactionsGroupedByDate(
                        type = TransactionType.EXPENSE,
                        from = from,
                        to = to,
                        currencyCode = "USD",
                        inStatistics = true,
                    ).first()

            assertEquals(1, result.size)
            val key = LocalDateTime(2024, 1, 1, 0, 0)
            assertEquals(0, result[key]?.sum?.compareTo(BigDecimal(150)))
        }

    @Test
    fun `getTransactionsByCategoryId should return transactions for category with children`() =
        runTest {
            val childCategory =
                Category(
                    id = 2L,
                    clientId = "test-child-category-client-id",
                    name = "Child Category",
                    type = TransactionType.EXPENSE,
                    icon = 1,
                    color = 1,
                    usageCount = 0,
                    children = emptyList(),
                    parent = testCategory,
                )

            val parentCategoryWithChildren = testCategory.copy(children = listOf(childCategory))

            val transactionDataModel =
                TransactionDataModel(
                    id = 1L,
                    clientId = "test-client-id",
                    type = TransactionType.EXPENSE,
                    categoryId = 2L,
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

            val from = LocalDateTime(2024, 1, 1, 0, 0)
            val to = LocalDateTime(2024, 1, 31, 23, 59)

            coEvery { categoryInteractor.getCategoryById(1L) } returns parentCategoryWithChildren
            coEvery {
                transactionRepo.getByCategoryInPeriod(childCategory, from, to, true)
            } returns flowOf(listOf(transactionDataModel))

            val result = interactor.getTransactionsByCategoryId(1L, from, to, true)

            assertEquals(1, result.size)
            assertEquals(2L, result.first().category.id)
        }

    @Test
    fun `getSumInPeriod should return sum of transactions in period`() =
        runTest {
            val transactionDataModel =
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

            val from = LocalDateTime(2024, 1, 1, 0, 0)
            val to = LocalDateTime(2024, 1, 31, 23, 59)

            coEvery {
                transactionRepo.getTransactionsByTypeInPeriod(from, to, TransactionType.EXPENSE, true, true)
            } returns flowOf(listOf(transactionDataModel))

            coEvery {
                currencyInteractor.toTargetCurrency(BigDecimal(100), "USD", "USD")
            } returns BigDecimal(100)

            val result =
                interactor
                    .getSumInPeriod(
                        from = from,
                        to = to,
                        transactionType = TransactionType.EXPENSE,
                        currencyCode = "USD",
                        inStatistics = true,
                    ).first()

            assertEquals(BigDecimal(100), result)
        }

    @Test
    fun `removeAllByCategory should remove all transactions for category`() =
        runTest {
            val transactionDataModel =
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

            coEvery { transactionRepo.getAllByCategory(testCategory) } returns listOf(transactionDataModel)
            coEvery { transactionRepo.removeTransaction(any()) } returns Unit
            coEvery { budgetInteractor.get() } returns flowOf(testBudget)
            coEvery { currencyInteractor.toTargetCurrency(any(), any(), any()) } returns BigDecimal(100)

            interactor.removeAllByCategory(testCategory)

            coVerify { transactionRepo.getAllByCategory(testCategory) }
            coVerify { transactionRepo.removeTransaction(any()) }
        }

    @Test
    fun `getApproxSumTodayCurrentCurrency should return sum for today`() =
        runTest {
            val transactionDataModel =
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

            coEvery {
                transactionRepo.getTransactionsByTypeInPeriod(any(), any(), TransactionType.EXPENSE)
            } returns flowOf(listOf(transactionDataModel))

            every { currencyInteractor.getMainCurrencyFlow() } returns flowOf(testCurrencyMetaData)
            coEvery { currencyInteractor.getCurrencyByCode("USD") } returns testCurrency

            val result = interactor.getApproxSumTodayCurrentCurrency(TransactionType.EXPENSE).first()

            assertEquals(0, result.compareTo(BigDecimal(100)))
        }

    @Test
    fun `getApproxSumInFiscalPeriodCurrentCurrency should return sum for fiscal period`() =
        runTest {
            val transactionDataModel =
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

            every { currencyInteractor.getMainCurrencyFlow() } returns flowOf(testCurrencyMetaData)
            coEvery { userInteractor.getFiscalDay() } returns 1
            coEvery {
                transactionRepo.getTransactionsByTypeInPeriod(any(), any(), TransactionType.EXPENSE)
            } returns flowOf(listOf(transactionDataModel))
            coEvery { currencyInteractor.getCurrencyByCode("USD") } returns testCurrency

            val result = interactor.getApproxSumInFiscalPeriodCurrentCurrency(TransactionType.EXPENSE).first()

            assertEquals(0, result.compareTo(BigDecimal(100)))
        }
}
