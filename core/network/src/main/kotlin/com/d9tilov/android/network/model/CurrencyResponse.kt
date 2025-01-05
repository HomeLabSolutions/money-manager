package com.d9tilov.android.network.model

import com.squareup.moshi.Json

typealias CurrencyRateMap = MutableMap<String, Double>

data class CurrencyResponse(
    @field:Json(name = "base_code") val base: String,
    @field:Json(name = "time_last_update_unix") val date: Long,
    @field:Json(name = "conversion_rates") val rates: CurrencyRateMap,
)
