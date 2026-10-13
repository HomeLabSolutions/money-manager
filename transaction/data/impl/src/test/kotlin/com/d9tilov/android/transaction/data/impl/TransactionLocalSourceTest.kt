package com.d9tilov.android.transaction.data.impl

import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.database.dao.TransactionDao
import com.d9tilov.android.database.entity.TransactionMinMaxDateDbModel
import com.d9tilov.android.datastore.PreferencesStore
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Test

class TransactionLocalSourceTest {
    private val preferencesStore: PreferencesStore = mockk()
    private val transactionDao: TransactionDao = mockk()
    private val source =
        TransactionLocalSource(
            ioDispatcher = StandardTestDispatcher(),
            preferencesStore = preferencesStore,
            transactionDao = transactionDao,
        )

    @Test
    fun `ordinary pager uses the unfiltered DAO method`() =
        runTest {
            every { preferencesStore.uid } returns flowOf(CURRENT_USER_ID)
            every {
                transactionDao.getAllByType(
                    clientId = CURRENT_USER_ID,
                    from = any(),
                    to = any(),
                    type = TransactionType.EXPENSE.value,
                )
            } returns mockk(relaxed = true)
            val pagingSource =
                TransactionLocalSource(
                    ioDispatcher = StandardTestDispatcher(testScheduler),
                    preferencesStore = preferencesStore,
                    transactionDao = transactionDao,
                )

            pagingSource.getAllByTypePaging(TransactionType.EXPENSE).first()

            verify(exactly = 1) {
                transactionDao.getAllByType(
                    clientId = CURRENT_USER_ID,
                    from = any(),
                    to = any(),
                    type = TransactionType.EXPENSE.value,
                )
            }
        }

    @Test
    fun `search pager passes the current user and normalized query to the search DAO method`() =
        runTest {
            every { preferencesStore.uid } returns flowOf(CURRENT_USER_ID)
            every {
                transactionDao.searchByTypeQuery(
                    clientId = CURRENT_USER_ID,
                    from = any(),
                    to = any(),
                    type = TransactionType.EXPENSE.value,
                    textPattern = "*1 234,50*",
                    amountQuery = "1234.5",
                )
            } returns mockk(relaxed = true)
            val pagingSource =
                TransactionLocalSource(
                    ioDispatcher = StandardTestDispatcher(testScheduler),
                    preferencesStore = preferencesStore,
                    transactionDao = transactionDao,
                )

            pagingSource.searchByTypePaging(TransactionType.EXPENSE, " 1 234,50 ").first()

            verify(exactly = 1) {
                transactionDao.searchByTypeQuery(
                    clientId = CURRENT_USER_ID,
                    from = any(),
                    to = any(),
                    type = TransactionType.EXPENSE.value,
                    textPattern = "*1 234,50*",
                    amountQuery = "1234.5",
                )
            }
        }

    @Test
    fun `getMinMaxDate returns dates only for the current user`() =
        runTest {
            val currentUserDates =
                TransactionMinMaxDateDbModel(
                    minDate = LocalDateTime(2025, 1, 1, 0, 0),
                    maxDate = LocalDateTime(2025, 12, 31, 0, 0),
                )
            every { preferencesStore.uid } returns flowOf(CURRENT_USER_ID)
            coEvery { transactionDao.getMinMaxDate(CURRENT_USER_ID) } returns currentUserDates

            val result = source.getMinMaxDate()

            assertEquals(currentUserDates.minDate, result.minDate)
            assertEquals(currentUserDates.maxDate, result.maxDate)
            coVerify(exactly = 1) { transactionDao.getMinMaxDate(CURRENT_USER_ID) }
        }

    @Test
    fun `getAllByTypeInPeriod delegates all filter combinations to DAO`() =
        runTest {
            every { preferencesStore.uid } returns flowOf(CURRENT_USER_ID)
            every {
                transactionDao.getAllByTypeInPeriod(
                    CURRENT_USER_ID,
                    any(),
                    any(),
                    TransactionType.EXPENSE.value,
                    any(),
                    any(),
                )
            } returns flowOf(emptyList())
            val filterCombinations =
                listOf(
                    false to false,
                    false to true,
                    true to false,
                    true to true,
                )

            filterCombinations.forEach { (onlyInStatistics, withRegular) ->
                source
                    .getAllByTypeInPeriod(
                        FROM,
                        TO,
                        TransactionType.EXPENSE,
                        onlyInStatistics,
                        withRegular,
                    ).first()

                verify(exactly = 1) {
                    transactionDao.getAllByTypeInPeriod(
                        CURRENT_USER_ID,
                        any(),
                        any(),
                        TransactionType.EXPENSE.value,
                        onlyInStatistics,
                        withRegular,
                    )
                }
            }
        }

    @Test
    fun `getByCategoryInPeriod delegates statistics filter to DAO`() =
        runTest {
            every { preferencesStore.uid } returns flowOf(CURRENT_USER_ID)
            every {
                transactionDao.getByCategoryIdInPeriod(
                    CURRENT_USER_ID,
                    CATEGORY.id,
                    FROM,
                    TO,
                    any(),
                )
            } returns flowOf(emptyList())

            listOf(false, true).forEach { onlyInStatistics ->
                source.getByCategoryInPeriod(CATEGORY, FROM, TO, onlyInStatistics).first()

                verify(exactly = 1) {
                    transactionDao.getByCategoryIdInPeriod(
                        CURRENT_USER_ID,
                        CATEGORY.id,
                        FROM,
                        TO,
                        onlyInStatistics,
                    )
                }
            }
        }

    private companion object {
        const val CURRENT_USER_ID = "current-user"
        val FROM = LocalDateTime(2025, 1, 1, 0, 0)
        val TO = LocalDateTime(2025, 1, 31, 23, 59, 59)
        val CATEGORY =
            Category(
                id = 1,
                clientId = CURRENT_USER_ID,
                name = "Food",
                type = TransactionType.EXPENSE,
                icon = 0,
                color = 0,
                usageCount = 0,
                children = emptyList(),
                parent = null,
            )
    }
}
