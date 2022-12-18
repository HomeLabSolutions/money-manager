package com.d9tilov.android.currency.data.contract


import com.d9tilov.android.currency.data.model.CurrencyMetaData
import kotlinx.coroutines.flow.Flow

interface CurrencyCacheSource {
    fun getCurrentCurrency(): Flow<CurrencyMetaData?>
    fun setCurrentCurrency(currency: CurrencyMetaData)
}
