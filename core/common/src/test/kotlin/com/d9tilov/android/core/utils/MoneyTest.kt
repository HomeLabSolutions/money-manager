package com.d9tilov.android.core.utils

import com.d9tilov.android.core.model.Currency
import com.d9tilov.android.core.model.Money
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

class MoneyTest {
    @Test
    fun `currency defines the number of minor units`() {
        assertEquals(Money(123, Currency.JPY), "123".moneyOrNull(Currency.JPY))
        assertEquals(Money(123, Currency.KRW), "123".moneyOrNull(Currency.KRW))
        assertEquals(Money(1234, Currency.USD), "12.34".moneyOrNull(Currency.USD))
        assertEquals(Money(1234, Currency.KWD), "1,234".moneyOrNull(Currency.KWD))
        assertEquals(Money(1234, Currency.CLF), "0.1234".moneyOrNull(Currency.CLF))
        assertNull(Currency.fromCode("XAU"))
        assertNull(Currency.fromCode("XDR"))
    }

    @Test
    fun `conversions preserve Long boundaries`() {
        assertEquals(Money(Long.MIN_VALUE, Currency.JPY), Long.MIN_VALUE.toString().moneyOrNull(Currency.JPY))
        assertEquals(Money(Long.MAX_VALUE, Currency.JPY), Long.MAX_VALUE.toString().moneyOrNull(Currency.JPY))
        assertEquals(Money(Long.MIN_VALUE, Currency.USD), "-92233720368547758.080".moneyOrNull(Currency.USD))
        assertEquals(Money(Long.MAX_VALUE, Currency.USD), "92233720368547758.070".moneyOrNull(Currency.USD))
        assertEquals(Money(0L, Currency.USD), "0.00000".moneyOrNull(Currency.USD))
        assertEquals("-92233720368547758.08", Money(Long.MIN_VALUE, Currency.USD).toAmountString())
    }

    @Test
    fun `unfinished amounts cannot be parsed`() {
        assertNull("".moneyOrNull(Currency.USD))
        assertNull("-".moneyOrNull(Currency.USD))
        assertNull(".".moneyOrNull(Currency.USD))
    }

    @Test
    fun `input stops at the precision of the currency`() {
        assertEquals("123", MainPriceFieldParser.parse("123", KeyPress.Dot, Currency.JPY.precision))
        assertEquals("1.234", MainPriceFieldParser.parse("1.23", KeyPress.Four, Currency.KWD.precision))
        assertEquals("1.234", MainPriceFieldParser.parse("1.234", KeyPress.Five, Currency.KWD.precision))
        assertFalse(MainPriceFieldParser.allowsAmountInput("100.50", Currency.KRW.precision))
        assertFalse(MainPriceFieldParser.allowsAmountInput("1.2345", Currency.KWD.precision))
    }
}
