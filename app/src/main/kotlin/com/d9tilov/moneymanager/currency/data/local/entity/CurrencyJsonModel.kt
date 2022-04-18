package com.d9tilov.moneymanager.currency.data.local.entity

import com.beust.klaxon.Json

data class CurrencyJsonModel(
    @Json(name = "symbol") val symbol: String,
    @Json(name = "name") val name: String,
    @Json(name = "symbol_native") val symbolNative: String,
    @Json(name = "code") val code: String
)
