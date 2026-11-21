package com.d9tilov.android.currency.data.impl.mapper

import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.currency.domain.model.CurrencyMetaData
import com.d9tilov.android.currency.domain.model.CurrencyRate
import com.d9tilov.android.database.entity.CurrencyDbModel
import com.d9tilov.android.database.entity.MainCurrencyDbModel

fun CurrencyDbModel.toDataModel(): CurrencyRate = CurrencyRate(id, symbol, value, lastUpdateTime)

fun CurrencyRate.toDbModel(): CurrencyDbModel = CurrencyDbModel(code, symbol, value, lastUpdateTime)

fun MainCurrencyDbModel.toDataModel(): CurrencyMetaData = CurrencyMetaData(clientId, code, code.getSymbolByCode())

fun CurrencyMetaData.toDbModel(): MainCurrencyDbModel = MainCurrencyDbModel(1, clientId, code)
