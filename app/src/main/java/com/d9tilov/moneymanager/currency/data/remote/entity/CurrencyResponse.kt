package com.d9tilov.moneymanager.currency.data.remote.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

typealias CurrencyRateMap = MutableMap<String, BigDecimal>

data class CurrencyResponse(
    @SerializedName("base_code") @Expose val base: String,
    @SerializedName("time_last_update_unix") @Expose val date: String,
    @SerializedName("conversion_rates") @Expose val rates: CurrencyRateMap
)
