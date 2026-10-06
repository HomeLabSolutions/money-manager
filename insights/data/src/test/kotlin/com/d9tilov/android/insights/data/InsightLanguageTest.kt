package com.d9tilov.android.insights.data

import com.d9tilov.android.user.domain.model.InsightLanguage
import org.junit.Assert.assertEquals
import org.junit.Test

class InsightLanguageTest {
    @Test
    fun `stored language tags retain explicit language choices including system`() {
        InsightLanguage.entries.forEach { language ->
            assertEquals(language, InsightLanguage.fromTag(language.tag))
        }
    }

    @Test
    fun `missing or unsupported language uses system`() {
        assertEquals(InsightLanguage.SYSTEM, InsightLanguage.fromTag(null))
        assertEquals(InsightLanguage.SYSTEM, InsightLanguage.fromTag("unsupported"))
    }
}
