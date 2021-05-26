package com.d9tilov.moneymanager.currency.data.remote.entity

import com.squareup.moshi.Json

typealias CurrencyRateMap = MutableMap<String, Double>

data class CurrencyResponse(
    @field:Json(name = "base_code") val base: String,
    @field:Json(name = "time_last_update_unix") val date: String,
    @field:Json(name = "conversion_rates") val rates: CurrencyRateMap
)
