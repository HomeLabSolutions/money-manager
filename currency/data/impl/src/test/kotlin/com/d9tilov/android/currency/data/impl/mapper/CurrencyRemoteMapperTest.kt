package com.d9tilov.android.currency.data.impl.mapper

import com.d9tilov.android.core.model.Decimal
import com.d9tilov.android.currency.data.contract.model.CurrencyResponse
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Test

class CurrencyRemoteMapperTest {
    @Test
    fun `only rates with supported currency codes are sent to storage`() {
        val response =
            CurrencyResponse(
                result = "success",
                base = "USD",
                date = 100L,
                rates =
                    mapOf(
                        "USD" to JsonPrimitive(1),
                        "JPY" to JsonPrimitive(150),
                        "KWD" to JsonPrimitive(0.3125),
                        "XDR" to JsonPrimitive(0.75),
                        "XAU" to JsonPrimitive(0.0001),
                        "ZZZ" to JsonPrimitive(1),
                    ),
            )

        val rates = response.toDataModel().associate { it.code to it.value }

        assertEquals(
            mapOf("USD" to Decimal.ONE, "JPY" to Decimal(150), "KWD" to Decimal("0.3125")),
            rates,
        )
    }
}
