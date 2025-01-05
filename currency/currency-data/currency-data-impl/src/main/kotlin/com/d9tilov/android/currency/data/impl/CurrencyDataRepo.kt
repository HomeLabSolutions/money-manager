package com.d9tilov.android.currency.data.impl

import com.d9tilov.android.currency.data.contract.CurrencySource
import com.d9tilov.android.currency.data.impl.mapper.toDataModel
import com.d9tilov.android.currency.domain.contract.CurrencyRepo
import com.d9tilov.android.currency.domain.model.Currency
import com.d9tilov.android.currency.domain.model.CurrencyMetaData
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.network.CurrencyApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class CurrencyDataRepo(
    private val currencySource: CurrencySource,
    private val currencyApi: CurrencyApi,
    private val preferencesStore: PreferencesStore,
) : CurrencyRepo {
    override fun getCurrencies(): Flow<List<Currency>> = currencySource.getCurrencies()

    override fun getMainCurrency(): Flow<CurrencyMetaData> = currencySource.getMainCurrency()

    override suspend fun getCurrencyByCode(code: String): Currency = currencySource.getCurrencyByCode(code)

    override suspend fun updateCurrencies() {
        val currencies = currencyApi.getCurrencies()
        val remoteCurrencyList = currencies.toDataModel()
        currencySource.saveCurrencies(remoteCurrencyList)
    }

    override suspend fun updateMainCurrency(code: String) {
        val uid = preferencesStore.uid.firstOrNull()
        uid?.run { currencySource.updateMainCurrency(code) }
    }

    override suspend fun hasAlreadyUpdatedToday(baseCurrency: String): Boolean = currencySource.hasAlreadyUpdatedToday(baseCurrency)
}
