package com.d9tilov.moneymanager.currency.data.remote.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

typealias CurrencyRateMap = MutableMap<String, BigDecimal>

data class CurrencyResponse(
    @SerializedName("base") @Expose val base: String,
    @SerializedName("date") @Expose val date: String,
    @SerializedName("rates") @Expose val rates: CurrencyRateMap
)
