package com.d9tilov.moneymanager.currency.data.local.mapper

import com.d9tilov.moneymanager.currency.data.entity.Currency
import com.d9tilov.moneymanager.currency.data.local.entity.CurrencyDbModel

fun CurrencyDbModel.toDataModel(): Currency =
    Currency(id, code, symbol, value, lastUpdateTime)

fun Currency.toDbModel(): CurrencyDbModel =
    CurrencyDbModel(id, code, symbol, value, lastUpdateTime)