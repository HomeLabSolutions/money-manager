package com.d9tilov.android.currency.data.impl

import com.d9tilov.android.currency.data.contract.CurrencyCacheSource
import com.d9tilov.android.currency.data.contract.model.CurrencyMetaData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class CurrencyCacheSourceImpl : CurrencyCacheSource {

    private val currentCurrency: MutableStateFlow<CurrencyMetaData?> =
        MutableStateFlow(null)

    override fun getCurrentCurrency(): Flow<CurrencyMetaData?> = currentCurrency
    override fun setCurrentCurrency(currency: CurrencyMetaData) {
        currentCurrency.value = currency
    }
}
