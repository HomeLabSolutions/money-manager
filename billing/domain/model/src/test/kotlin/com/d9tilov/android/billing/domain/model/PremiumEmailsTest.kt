package com.d9tilov.android.billing.domain.model

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Test

class PremiumEmailsTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `remote config emails remain compatible and unknown properties are ignored`() {
        val result =
            json.decodeFromString<PremiumEmails>(
                """{"emails":["one@example.com","two@example.com"],"extra":true}""",
            )
        assertEquals(listOf("one@example.com", "two@example.com"), result.emails)
    }

    @Test
    fun `null config retains the empty list fallback`() {
        assertNull(json.decodeFromString<PremiumEmails?>("null"))
    }

    @Test
    fun `invalid and missing email lists are rejected`() {
        listOf("", "{}", "{bad}", """{"emails":null}""").forEach { value ->
            assertThrows(SerializationException::class.java) { json.decodeFromString<PremiumEmails>(value) }
        }
    }
}
