package com.d9tilov.android.transaction.domain.impl

import com.d9tilov.android.budget.domain.contract.BudgetInteractor
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.core.model.LocationData
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.transaction.domain.contract.TransactionRepo
import com.d9tilov.android.transaction.domain.model.TransactionDataModel
import com.d9tilov.android.transaction.regular.domain.contract.RegularTransactionInteractor
import com.d9tilov.android.user.domain.contract.UserInteractor
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal

class TransactionInteractorCategoryLookupTest {
    private val transactionRepo: TransactionRepo = mockk(relaxed = true)
    private val categoryInteractor: CategoryInteractor = mockk(relaxed = true)
    private val interactor =
        TransactionInteractorImpl(
            transactionRepo = transactionRepo,
            regularTransactionInteractor = mockk<RegularTransactionInteractor>(relaxed = true),
            categoryInteractor = categoryInteractor,
            userInteractor = mockk<UserInteractor>(relaxed = true),
            currencyInteractor = mockk<CurrencyInteractor>(relaxed = true),
            budgetInteractor = mockk<BudgetInteractor>(relaxed = true),
        )

    @Test
    fun `getTransactionsGroupedByCategory should look up categories in linear time`() =
        runTest {
            val categories = CountingList((1L..CATEGORY_COUNT).map(::category))
            val transactions = (1L..TRANSACTION_COUNT).map { id -> transaction(id, categories.last().id) }
            val from = LocalDateTime(2024, 1, 1, 0, 0)
            val to = LocalDateTime(2024, 1, 31, 23, 59)

            coEvery {
                categoryInteractor.getGroupedCategoriesByType(TransactionType.EXPENSE)
            } returns flowOf(categories)
            coEvery {
                transactionRepo.getTransactionsByTypeInPeriod(from, to, TransactionType.EXPENSE, true)
            } returns flowOf(transactions)

            interactor
                .getTransactionsGroupedByCategory(
                    type = TransactionType.EXPENSE,
                    from = from,
                    to = to,
                    currencyCode = "USD",
                    inStatistics = true,
                    onlySubcategories = false,
                ).first()

            assertTrue(
                "Category lookup read the ${categories.size}-item list ${categories.readCount} times",
                categories.readCount <= categories.size * 3,
            )
        }

    private fun category(id: Long) =
        Category(
            id = id,
            clientId = "category-$id",
            name = "Category $id",
            type = TransactionType.EXPENSE,
            icon = 1,
            color = 1,
            usageCount = 0,
            children = emptyList(),
            parent = null,
        )

    private fun transaction(
        id: Long,
        categoryId: Long,
    ) = TransactionDataModel(
        id = id,
        clientId = "transaction-$id",
        type = TransactionType.EXPENSE,
        categoryId = categoryId,
        currencyCode = "USD",
        sum = BigDecimal.ONE,
        usdSum = BigDecimal.ONE,
        date = LocalDateTime(2024, 1, 1, 0, 0),
        description = "Test",
        qrCode = "",
        inStatistics = true,
        isRegular = false,
        location = LocationData(0.0, 0.0),
        photoUri = "",
    )

    private class CountingList<T>(
        private val delegate: List<T>,
    ) : AbstractList<T>() {
        var readCount: Int = 0
            private set

        override val size: Int = delegate.size

        override fun get(index: Int): T {
            readCount++
            return delegate[index]
        }
    }

    private companion object {
        const val CATEGORY_COUNT = 30L
        const val TRANSACTION_COUNT = 30L
    }
}
