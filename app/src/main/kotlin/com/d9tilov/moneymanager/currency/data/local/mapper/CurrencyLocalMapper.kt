package com.d9tilov.moneymanager.currency.data.local.mapper

import com.d9tilov.moneymanager.core.util.CurrencyUtils.getSymbolByCode
import com.d9tilov.moneymanager.currency.data.entity.Currency
import com.d9tilov.moneymanager.currency.data.entity.CurrencyMetaData
import com.d9tilov.moneymanager.currency.data.local.entity.CurrencyDbModel
import com.d9tilov.moneymanager.currency.data.local.entity.MainCurrencyDbModel

fun CurrencyDbModel.toDataModel(): Currency =
    Currency(id, symbol, value, lastUpdateTime)

fun Currency.toDbModel(): CurrencyDbModel =
    CurrencyDbModel(code, symbol, value, lastUpdateTime)

fun MainCurrencyDbModel.toDataModel(): CurrencyMetaData =
    CurrencyMetaData(clientId, code, code.getSymbolByCode())

fun CurrencyMetaData.toDbModel(): MainCurrencyDbModel = MainCurrencyDbModel(1, code, clientId)
