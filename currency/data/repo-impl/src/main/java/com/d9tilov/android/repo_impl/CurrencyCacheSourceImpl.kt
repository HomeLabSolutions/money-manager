package com.d9tilov.android.repo_impl

import com.d9tilov.android.repo.CurrencyCacheSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class CurrencyCacheSourceImpl : com.d9tilov.android.repo.CurrencyCacheSource {

    private val currentCurrency: MutableStateFlow<com.d9tilov.android.repo.model.CurrencyMetaData?> =
        MutableStateFlow(null)

    override fun getCurrentCurrency(): Flow<com.d9tilov.android.repo.model.CurrencyMetaData?> = currentCurrency
    override fun setCurrentCurrency(currency: com.d9tilov.android.repo.model.CurrencyMetaData) {
        currentCurrency.value = currency
    }
}
