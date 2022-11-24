package com.d9tilov.moneymanager.user.data.local

import com.d9tilov.moneymanager.base.data.local.preferences.CurrencyMetaData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class UserCacheSourceImpl : UserCacheSource {

    private val currentCurrency: MutableStateFlow<CurrencyMetaData?> =
        MutableStateFlow(null)

    override fun getCurrentCurrency(): Flow<CurrencyMetaData?> = currentCurrency
    override fun setCurrentCurrency(currency: CurrencyMetaData) {
        currentCurrency.value = currency
    }
}
