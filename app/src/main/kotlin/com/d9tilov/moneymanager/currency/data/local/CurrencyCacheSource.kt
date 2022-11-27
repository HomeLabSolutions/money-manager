package com.d9tilov.moneymanager.currency.data.local

import com.d9tilov.moneymanager.base.data.Source
import com.d9tilov.moneymanager.currency.data.entity.CurrencyMetaData
import kotlinx.coroutines.flow.Flow

interface CurrencyCacheSource : Source {
    fun getCurrentCurrency(): Flow<CurrencyMetaData?>
    fun setCurrentCurrency(currency: CurrencyMetaData)
}
