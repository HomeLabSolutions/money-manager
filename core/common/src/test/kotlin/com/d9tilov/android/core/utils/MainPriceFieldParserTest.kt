package com.d9tilov.android.core.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MainPriceFieldParserTest {
    @Test
    fun `do not remove zero`() {
        val res = MainPriceFieldParser.parse("0", KeyPress.Del)

        assertEquals("0", res)
    }

    @Test
    fun `make number as zero after removing`() {
        val res = MainPriceFieldParser.parse("5", KeyPress.Del)

        assertEquals("0", res)
    }

    @Test
    fun `remove one digit`() {
        val res = MainPriceFieldParser.parse("123", KeyPress.Del)

        assertEquals("12", res)
    }

    @Test
    fun `transform zero to number`() {
        val res = MainPriceFieldParser.parse("0", KeyPress.Three)

        assertEquals("3", res)
    }

    @Test
    fun `add dot after zero`() {
        val res = MainPriceFieldParser.parse("0", KeyPress.Dot)

        assertEquals("0.", res)
    }

    @Test
    fun `prevent adding two dots`() {
        val res = MainPriceFieldParser.parse("0.5", KeyPress.Dot)

        assertEquals("0.5", res)
    }

    @Test
    fun `remove dot`() {
        val res = MainPriceFieldParser.parse("123.", KeyPress.Del)

        assertEquals("123", res)
    }

    @Test
    fun `exceed maximum length`() {
        val res = MainPriceFieldParser.parse("12345678910", KeyPress.Six)

        assertEquals("12345678910", res)
    }

    @Test
    fun `add one digit`() {
        val res = MainPriceFieldParser.parse("123", KeyPress.Four)

        assertEquals("1234", res)
    }

    @Test
    fun `only two digits after dot`() {
        val res = MainPriceFieldParser.parse("123.12", KeyPress.Four)

        assertEquals("123.12", res)
    }

    @Test
    fun `only one digits after dot`() {
        val res = MainPriceFieldParser.parse("123.1", KeyPress.Four)

        assertEquals("123.14", res)
    }

    @Test
    fun `validate empty string`() {
        val res = MainPriceFieldParser.isInputValid("")

        assertFalse(res)
    }

    @Test
    fun `validate zero`() {
        val res = MainPriceFieldParser.isInputValid("0")

        assertFalse(res)
    }

    @Test
    fun `validate dot`() {
        val res = MainPriceFieldParser.isInputValid(".")

        assertFalse(res)
    }

    @Test
    fun `validate zero and number`() {
        val res = MainPriceFieldParser.isInputValid("01")

        assertFalse(res)
    }

    @Test
    fun `validate zero and dot`() {
        val res = MainPriceFieldParser.isInputValid("0.")

        assertFalse(res)
    }

    @Test
    fun `validate zero dot and number`() {
        val res = MainPriceFieldParser.isInputValid("0.1")

        assertTrue(res)
    }

    @Test
    fun `validate long number`() {
        val res = MainPriceFieldParser.isInputValid("123456789012")

        assertFalse(res)
    }

    @Test
    fun `validate two dots`() {
        val res = MainPriceFieldParser.isInputValid("1.2.3")

        assertFalse(res)
    }

    @Test
    fun `validate not number`() {
        val res = MainPriceFieldParser.isInputValid("12-3")

        assertFalse(res)
    }

    @Test
    fun `do not append to an amount already beyond maximum length`() {
        assertEquals("123456789012", MainPriceFieldParser.parse("123456789012", KeyPress.Three))
    }

    @Test
    fun `deleting an empty amount restores zero`() {
        assertEquals("0", MainPriceFieldParser.parse("", KeyPress.Del))
    }

    @Test
    fun `do not append when fractional precision already exceeds two digits`() {
        assertEquals("12.345", MainPriceFieldParser.parse("12.345", KeyPress.Six))
    }

    @Test
    fun `deletion remains available above the length limit`() {
        assertEquals("12345678901", MainPriceFieldParser.parse("123456789012", KeyPress.Del))
    }

    @Test
    fun `dot cannot be appended at the length limit`() {
        assertEquals("12345678901", MainPriceFieldParser.parse("12345678901", KeyPress.Dot))
    }

    @Test
    fun `fractional amount can reach the length limit`() {
        assertEquals("12345678.12", MainPriceFieldParser.parse("12345678.1", KeyPress.Two))
    }

    @Test
    fun `validation preserves accepted decimal forms`() {
        listOf("12345678901", "0.0", "0.12", "123.", "1.234").forEach { amount ->
            assertTrue(amount, MainPriceFieldParser.isInputValid(amount))
        }
    }

    @Test
    fun `validation rejects malformed prefixes and whitespace`() {
        listOf("00", "00.1", ".5", " 1", "1 ", "0..1").forEach { amount ->
            assertFalse(amount, MainPriceFieldParser.isInputValid(amount))
        }
    }
}
