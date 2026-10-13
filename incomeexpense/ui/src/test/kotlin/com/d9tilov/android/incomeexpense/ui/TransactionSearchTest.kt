package com.d9tilov.android.incomeexpense.ui

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.testing.asPagingSourceFactory
import androidx.paging.testing.asSnapshot
import com.d9tilov.android.transaction.ui.model.TransactionUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal

class TransactionSearchTest {
    private val transaction =
        TransactionUiModel.EMPTY.copy(
            sum = BigDecimal("1234.50"),
            category = TransactionUiModel.EMPTY.category.copy(name = "Продукты"),
            description = "Покупка в магазине у дома",
        )

    @Test
    fun `category and description match substrings ignoring case`() {
        assertTrue(transaction.matchesSearch("ПРОДУ"))
        assertTrue(transaction.matchesSearch("МАГАЗИНЕ У ДОМА"))
        assertTrue(transaction.matchesSearch(" продукты "))
        assertFalse(transaction.matchesSearch("транспорт"))
    }

    @Test
    fun `amount accepts decimal separators and digit grouping`() {
        assertTrue(transaction.matchesSearch("234"))
        assertTrue(transaction.matchesSearch("1234.5"))
        assertTrue(transaction.matchesSearch("1234,50"))
        assertTrue(transaction.matchesSearch("1 234,50"))
        assertTrue(transaction.matchesSearch("1\u00a0234,50"))
        assertFalse(transaction.matchesSearch("1234,51"))
    }

    @Test
    fun `blank queries match and unrelated fields do not`() {
        assertTrue(transaction.matchesSearch(""))
        assertTrue(transaction.matchesSearch("   "))
        assertFalse(transaction.copy(currencyCode = "USD").matchesSearch("USD"))
    }

    @Test
    fun `search finds a matching transaction beyond the initially loaded pages`() =
        runTest {
            val items = (1L..50L).map { id -> transaction.copy(id = id, description = "Запись $id") }
            val data =
                Pager(
                    config = PagingConfig(pageSize = 5, initialLoadSize = 5, enablePlaceholders = false),
                    pagingSourceFactory = items.asPagingSourceFactory(),
                ).flow.cachedIn(backgroundScope)

            val snapshot = data.searchTransactions(flowOf("Запись 50")).asSnapshot()

            assertEquals(listOf(50L), snapshot.map { it.id })
        }

    @Test
    fun `changing and clearing the query reuses cached paging data`() =
        runTest {
            val items = (1L..10L).map { id -> transaction.copy(id = id, description = "Запись $id") }
            val data =
                Pager(
                    config = PagingConfig(pageSize = 5, enablePlaceholders = false),
                    pagingSourceFactory = items.asPagingSourceFactory(),
                ).flow.cachedIn(backgroundScope)
            val query = MutableStateFlow("Запись 7")
            val results = data.searchTransactions(query).cachedIn(backgroundScope)

            assertEquals(listOf(7L), results.asSnapshot().map { it.id })
            query.value = "Запись 9"
            assertEquals(listOf(9L), results.asSnapshot().map { it.id })
            query.value = ""
            assertEquals(items, results.asSnapshot { appendScrollWhile { true } })
        }

    @Test
    fun `a query with no matches returns an empty list after pagination completes`() =
        runTest {
            val items = (1L..30L).map { id -> transaction.copy(id = id) }
            val data =
                Pager(
                    config = PagingConfig(pageSize = 5, initialLoadSize = 5, enablePlaceholders = false),
                    pagingSourceFactory = items.asPagingSourceFactory(),
                ).flow.cachedIn(backgroundScope)

            assertTrue(data.searchTransactions(flowOf("Несуществующее описание")).asSnapshot().isEmpty())
        }
}
