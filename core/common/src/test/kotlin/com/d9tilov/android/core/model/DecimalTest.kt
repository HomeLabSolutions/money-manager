package com.d9tilov.android.core.model

import com.d9tilov.android.core.utils.divideBy
import com.d9tilov.android.core.utils.roundToCurrency
import com.d9tilov.android.core.utils.toAmountString
import com.d9tilov.android.core.utils.toDecimalOrNull
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertThrows
import org.junit.Test

class DecimalTest {
    @Test
    fun `parsing preserves monetary values across plain and scientific notation`() {
        assertEquals(Decimal("0.123"), Decimal("1.23E-1"))
        assertEquals(Decimal("12"), Decimal("+.12e+2"))
        assertEquals(Decimal("1"), Decimal("1."))
        assertEquals(Decimal("1000").hashCode(), Decimal("1E+3").hashCode())
        assertEquals("-0.050", Decimal("-.0050E+1").toPlainString())
        assertEquals("0.01", Decimal("1e-0002").toPlainString())
    }

    @Test
    fun `decimal parser rejects malformed mantissas and exponents`() {
        listOf("", "NaN", ".", "-.", "+", "1.2.3", " 1 ", "2E", "2E+", "2e+-1", "2e1e2", "2e١", "٣.1")
            .forEach { assertNull(it, it.toDecimalOrNull()) }
    }

    @Test
    fun `equal values have the same hash across different scales and exponents`() {
        listOf(
            listOf("1", "1.0", "1.00", "0.01e2"),
            listOf("-100", "-100.00", "-1e2"),
            listOf("0", "0.00", "-0e3"),
        ).forEach { representations ->
            assertEquals(1, representations.map(::Decimal).toSet().size)
        }
    }

    @Test
    fun `scientific notation and point shifts preserve a nonnegative fixed scale`() {
        assertEquals(0, Decimal("1e3").fractionDigits)
        assertEquals("1000", Decimal("1e3").toPlainString())
        assertEquals("123.00", Decimal("1.2300e2").toPlainString())
        assertEquals("12.340", Decimal("1.2340").movePoint(1).toPlainString())
    }

    @Test
    fun `division rounds and retains exactly the requested fractional digits`() {
        val half = Decimal.ONE.divide(Decimal(2), 4)
        assertEquals(4, half.fractionDigits)
        assertEquals("0.5000", half.toPlainString())
        assertEquals("0.0000", Decimal.ZERO.divide(Decimal.ONE, 4).toPlainString())
        assertEquals("0.00", Decimal("0.004").divide(Decimal.ONE, 2).toPlainString())
        assertEquals("-1.24", Decimal("-1.235").divide(Decimal.ONE, 2).toPlainString())
        assertEquals("2", Decimal("1.5").divide(Decimal.ONE, 0).toPlainString())
    }

    @Test
    fun `invalid scales and oversized exponents are rejected`() {
        assertNull("1e2147483647".toDecimalOrNull())
        assertThrows(IllegalArgumentException::class.java) { Decimal.ONE.movePoint(Int.MIN_VALUE) }
        assertThrows(IllegalArgumentException::class.java) { Decimal.ONE.divide(Decimal.TEN, -1) }
        assertThrows(IllegalArgumentException::class.java) { Decimal.ONE.divide(Decimal.TEN, Int.MAX_VALUE) }
    }

    @Test
    fun `display rounding reuses amounts that already have two fractional digits`() {
        val amount = Decimal("12.30")

        assertSame(amount, amount.roundToCurrency(Currency.USD.name))
        assertEquals("12.3", Decimal("12.3").toAmountString(Currency.USD.name))
        assertEquals("12.31", Decimal("12.305").toAmountString(Currency.USD.name))
    }

    @Test
    fun `amount formatting uses the currency precision`() {
        val amount = Decimal("123.45678")

        assertEquals("123", amount.toAmountString(Currency.JPY.name))
        assertEquals("123.46", amount.toAmountString(Currency.USD.name))
        assertEquals("123.457", amount.toAmountString(Currency.KWD.name))
        assertEquals("123.4568", amount.toAmountString(Currency.CLF.name))
    }

    @Test
    fun `normalized amounts retain current display and report precision`() {
        assertEquals("12.35", Decimal("12.345").toAmountString(Currency.USD.name))
        assertEquals("-12.35", Decimal("-12.345").toAmountString(Currency.USD.name))
        assertEquals("10.00000000000000000000", Decimal("100").divideBy(Decimal.TEN).toPlainString())
        assertEquals("123.2", Decimal("123.20").toAmountString(Currency.USD.name))
        assertEquals("123.02", Decimal("123.02").toAmountString(Currency.USD.name))
        assertEquals("1", Decimal("1.00").toAmountString(Currency.USD.name))
        assertEquals("1", Decimal("0.995").toAmountString(Currency.USD.name))
        assertEquals("0", Decimal("-0.004").toAmountString(Currency.USD.name))
        assertEquals("0", Decimal.ZERO.toAmountString(Currency.USD.name))
        assertEquals("0", null.toAmountString(Currency.USD.name))
    }

    @Test(expected = ArithmeticException::class)
    fun `division by zero still fails explicitly`() {
        Decimal.ONE.divideBy(Decimal.ZERO)
    }
}
