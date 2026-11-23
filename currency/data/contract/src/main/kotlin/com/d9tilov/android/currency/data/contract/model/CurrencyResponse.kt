package com.d9tilov.android.currency.data.contract.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive

@Serializable
data class CurrencyResponse(
    @SerialName("result") val result: String,
    @SerialName("base_code") val base: String,
    @SerialName("time_last_update_unix") val date: Long,
    @SerialName("conversion_rates") val rates: Map<String, JsonPrimitive>,
)
