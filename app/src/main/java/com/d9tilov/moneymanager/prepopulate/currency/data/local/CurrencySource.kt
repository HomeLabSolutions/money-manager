package com.d9tilov.moneymanager.prepopulate.currency.data.local

import com.d9tilov.moneymanager.base.data.Source
import com.d9tilov.moneymanager.prepopulate.currency.data.entity.Currency
import io.reactivex.Completable
import io.reactivex.Maybe

interface CurrencySource : Source {

    fun saveCurrencies(currencies: List<Currency>): Completable
    fun getCurrencies(): Maybe<List<Currency>>
}
