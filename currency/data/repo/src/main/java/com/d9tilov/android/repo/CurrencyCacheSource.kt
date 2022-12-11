package com.d9tilov.android.repo

import com.d9tilov.android.core.model.Source
import com.d9tilov.android.repo.model.CurrencyMetaData
import kotlinx.coroutines.flow.Flow

interface CurrencyCacheSource : Source {
    fun getCurrentCurrency(): Flow<CurrencyMetaData?>
    fun setCurrentCurrency(currency: CurrencyMetaData)
}
