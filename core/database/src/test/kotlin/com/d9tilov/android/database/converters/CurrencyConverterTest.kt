package com.d9tilov.android.database.converters

import com.d9tilov.android.core.model.Decimal
import org.junit.Assert.assertEquals
import org.junit.Test

class CurrencyConverterTest {
    @Test
    fun `reads legacy stored amounts and removes only fractional zeros`() {
        val cases =
            mapOf(
                "1200" to "12",
                "1230.00" to "12.3",
                "-120030.00" to "-1200.3",
                "0.0000" to "0",
                "1.230E-7" to "0.00000000123",
                "123456789012345678901.230000" to "1234567890123456789.0123",
            )

        for ((stored, expected) in cases) {
            assertEquals(stored, expected, CurrencyConverter.fromStringToDecimal(stored).toPlainString())
        }
    }

    @Test
    fun `round trips a converted amount without rounding it to cents`() {
        val amount = Decimal("0.12345678901234567890")

        val restored = CurrencyConverter.fromStringToDecimal(CurrencyConverter.fromDecimalToString(amount))

        assertEquals(amount, restored)
        assertEquals("0.1234567890123456789", restored.toPlainString())
    }
}
