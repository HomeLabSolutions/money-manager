package com.d9tilov.moneymanager.currency.domain

import com.d9tilov.moneymanager.currency.data.entity.Currency
import io.reactivex.Single

interface CurrencyRepo {

    fun getCurrencies(baseCurrency: String): Single<List<Currency>>
}
