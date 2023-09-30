package com.d9tilov.android.core.utils

import org.junit.Assert.assertEquals
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
}