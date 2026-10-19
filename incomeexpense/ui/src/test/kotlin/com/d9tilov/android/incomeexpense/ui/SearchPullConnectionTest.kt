package com.d9tilov.android.incomeexpense.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchPullConnectionTest {
    private var searchVisible = false
    private val connection =
        SearchPullConnection(
            threshold = 48f,
            isSearchVisible = { searchVisible },
            onRevealSearch = { searchVisible = true },
        )

    @Test
    fun `search is revealed after enough unconsumed downward dragging`() {
        drag(30f)
        assertFalse(searchVisible)
        drag(18f)
        assertTrue(searchVisible)
        assertEquals(Offset.Zero, drag(10f))
    }

    @Test
    fun `normal list scrolling and flings do not reveal search`() {
        connection.onPostScroll(Offset(0f, 100f), Offset.Zero, NestedScrollSource.UserInput)
        connection.onPostScroll(Offset.Zero, Offset(0f, 100f), NestedScrollSource.SideEffect)
        assertFalse(searchVisible)
    }

    @Test
    fun `separate short pulls cannot accumulate into opening search`() =
        runTest {
            drag(30f)
            connection.onPostFling(Velocity.Zero, Velocity.Zero)
            drag(30f)
            assertFalse(searchVisible)
        }

    @Test
    fun `reversing the drag resets the reveal threshold`() {
        drag(30f)
        connection.onPreScroll(Offset(0f, -20f), NestedScrollSource.UserInput)
        drag(30f)
        assertFalse(searchVisible)
    }

    @Test
    fun `scrolling down the list keeps search open`() {
        searchVisible = true

        val consumed = connection.onPreScroll(Offset(0f, -20f), NestedScrollSource.UserInput)
        connection.onPostScroll(Offset(0f, -20f), Offset(0f, -10f), NestedScrollSource.UserInput)

        assertTrue(searchVisible)
        assertEquals(Offset.Zero, consumed)
    }

    @Test
    fun `programmatic scrolling keeps search open`() {
        searchVisible = true

        connection.onPreScroll(Offset(0f, -20f), NestedScrollSource.SideEffect)

        assertTrue(searchVisible)
    }

    @Test
    fun `pulling down an open search keeps it open`() {
        searchVisible = true

        connection.onPreScroll(Offset(0f, 20f), NestedScrollSource.UserInput)
        drag(20f)

        assertTrue(searchVisible)
    }

    private fun drag(distance: Float): Offset =
        connection.onPostScroll(Offset.Zero, Offset(0f, distance), NestedScrollSource.UserInput)
}
