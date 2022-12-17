@file:Suppress("PackageNaming")
package com.d9tilov.android.currency.data.impl.mapper

import com.d9tilov.android.currency.data.contract.model.Currency
import com.d9tilov.android.currency.data.contract.model.CurrencyMetaData
import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.database.entity.CurrencyDbModel
import com.d9tilov.android.database.entity.MainCurrencyDbModel

fun CurrencyDbModel.toDataModel(): Currency =
    Currency(id, symbol, value, lastUpdateTime)

fun Currency.toDbModel(): CurrencyDbModel =
    CurrencyDbModel(code, symbol, value, lastUpdateTime)

fun MainCurrencyDbModel.toDataModel(): CurrencyMetaData =
    CurrencyMetaData(clientId, code, code.getSymbolByCode())

fun CurrencyMetaData.toDbModel(): MainCurrencyDbModel = MainCurrencyDbModel(1, code, clientId)
