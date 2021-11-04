package com.d9tilov.moneymanager.currency.data.local.entity

import com.beust.klaxon.Json

class CurrencyListJsonModel {

    @Json(name = "currencies")
    val currencies = mutableListOf<CurrencyJsonModel>()
}
