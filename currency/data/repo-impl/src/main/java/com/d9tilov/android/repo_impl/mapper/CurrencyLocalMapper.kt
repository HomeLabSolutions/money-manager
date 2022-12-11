@file:Suppress("PackageNaming")
package com.d9tilov.android.repo_impl.mapper

import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.database.entity.CurrencyDbModel
import com.d9tilov.android.database.entity.MainCurrencyDbModel
import com.d9tilov.android.repo.model.Currency
import com.d9tilov.android.repo.model.CurrencyMetaData

fun CurrencyDbModel.toDataModel(): com.d9tilov.android.repo.model.Currency =
    com.d9tilov.android.repo.model.Currency(id, symbol, value, lastUpdateTime)

fun com.d9tilov.android.repo.model.Currency.toDbModel(): CurrencyDbModel =
    CurrencyDbModel(code, symbol, value, lastUpdateTime)

fun MainCurrencyDbModel.toDataModel(): com.d9tilov.android.repo.model.CurrencyMetaData =
    com.d9tilov.android.repo.model.CurrencyMetaData(clientId, code, code.getSymbolByCode())

fun com.d9tilov.android.repo.model.CurrencyMetaData.toDbModel(): MainCurrencyDbModel = MainCurrencyDbModel(1, code, clientId)
