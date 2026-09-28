package com.d9tilov.android.insights.data

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InsightDeduplicationTest {
    @Test
    fun `detects an exact repeat despite case and whitespace`() {
        assertTrue(matchesPreviousInsight(" Business income   rose. ", listOf("business income rose.")))
        assertFalse(matchesPreviousInsight("Business expenses rose.", listOf("Business income rose.")))
    }
}
