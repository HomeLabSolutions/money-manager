package com.d9tilov.android.incomeexpense.ui

import androidx.compose.foundation.lazy.LazyListItemInfo
import com.d9tilov.android.transaction.ui.model.TransactionUiHeader
import com.d9tilov.android.transaction.ui.model.TransactionUiModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TransactionScrollAnchorTest {
    private val first = TransactionUiModel.EMPTY.copy(id = 1L)
    private val second = TransactionUiModel.EMPTY.copy(id = 2L)
    private val third = TransactionUiModel.EMPTY.copy(id = 3L)
    private val header = TransactionUiHeader(first.date, first.currencyCode)
    private val snapshot = listOf(header, first, second, third)
    private val visibleItems =
        listOf(
            visibleItem("header:${header.date}", index = 0, offset = -80),
            visibleItem("transaction:1", index = 1, offset = -20),
            visibleItem("transaction:2", index = 2, offset = 40),
        )

    @Test
    fun `opening a visible transaction anchors that row and its offset`() {
        val anchor = findTransactionScrollAnchor(snapshot, visibleItems, preferredId = 2L, excludedId = null)

        assertEquals(ScrollAnchor("transaction:2", -40, snapshot.hashCode()), anchor)
    }

    @Test
    fun `deleting the first visible transaction anchors the next visible row`() {
        val anchor = findTransactionScrollAnchor(snapshot, visibleItems, preferredId = null, excludedId = 1L)

        assertEquals(ScrollAnchor("transaction:2", -40, snapshot.hashCode()), anchor)
    }

    @Test
    fun `deleting a later row preserves the partially visible first row`() {
        val anchor = findTransactionScrollAnchor(snapshot, visibleItems, preferredId = null, excludedId = 2L)

        assertEquals(ScrollAnchor("transaction:1", 20, snapshot.hashCode()), anchor)
    }

    @Test
    fun `an offscreen preferred transaction falls back to the first visible row`() {
        val anchor = findTransactionScrollAnchor(snapshot, visibleItems, preferredId = 3L, excludedId = null)

        assertEquals(ScrollAnchor("transaction:1", 20, snapshot.hashCode()), anchor)
    }

    @Test
    fun `headers and removed rows cannot become scroll anchors`() {
        val items = listOf(visibleItems.first(), visibleItem("transaction:99", index = 1, offset = 0))

        assertNull(findTransactionScrollAnchor(snapshot, items, preferredId = null, excludedId = null))
    }

    @Test
    fun `deleting the only visible transaction leaves no replacement anchor`() {
        val items = listOf(visibleItems.first(), visibleItems[1])

        assertNull(findTransactionScrollAnchor(snapshot, items, preferredId = null, excludedId = 1L))
    }

    private fun visibleItem(
        key: String,
        index: Int,
        offset: Int,
    ): LazyListItemInfo =
        object : LazyListItemInfo {
            override val key: Any = key
            override val index: Int = index
            override val offset: Int = offset
            override val size: Int = 60
        }
}
