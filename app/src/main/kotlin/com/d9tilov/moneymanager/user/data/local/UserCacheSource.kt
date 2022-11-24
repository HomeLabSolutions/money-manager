package com.d9tilov.moneymanager.user.data.local

import com.d9tilov.moneymanager.base.data.Source
import com.d9tilov.moneymanager.base.data.local.preferences.CurrencyMetaData
import kotlinx.coroutines.flow.Flow

interface UserCacheSource : Source {
    fun getCurrentCurrency(): Flow<CurrencyMetaData?>
    fun setCurrentCurrency(currency: CurrencyMetaData)
}
