package com.d9tilov.android.insights.data

import com.d9tilov.android.insights.data.InsightsDataRepo.Companion.hasTwoWeeksOfHistory
import kotlinx.datetime.LocalDate
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InsightHistoryTest {
    @Test
    fun `requires fourteen days between first and last transaction`() {
        val first = LocalDate(2026, 9, 1)

        assertFalse(hasTwoWeeksOfHistory(emptyList()))
        assertFalse(hasTwoWeeksOfHistory(listOf(first)))
        assertFalse(hasTwoWeeksOfHistory(listOf(first, LocalDate(2026, 9, 14))))
        assertTrue(hasTwoWeeksOfHistory(listOf(LocalDate(2026, 9, 15), first)))
    }
}
