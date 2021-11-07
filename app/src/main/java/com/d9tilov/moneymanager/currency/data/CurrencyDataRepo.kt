package com.d9tilov.moneymanager.currency.data

import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.currency.data.entity.Currency
import com.d9tilov.moneymanager.currency.data.local.CurrencySource
import com.d9tilov.moneymanager.currency.data.remote.CurrencyApi
import com.d9tilov.moneymanager.currency.data.remote.mapper.toDataModel
import com.d9tilov.moneymanager.currency.domain.CurrencyRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CurrencyDataRepo(
    private val preferencesStore: PreferencesStore,
    private val currencySource: CurrencySource,
    private val currencyApi: CurrencyApi
) : CurrencyRepo {

    override fun getCurrencies(baseCurrency: String): Flow<List<Currency>> {
        return currencySource.getCurrencies().map { list ->
            if (list.isEmpty()) {
                val currencies = currencyApi.getCurrencies(baseCurrency)
                val remoteCurrencyList = currencies.toDataModel()
                currencySource.saveCurrencies(remoteCurrencyList)
            }
            list
        }
    }

    override suspend fun updateCurrentCurrency(currency: Currency) {
        preferencesStore.saveCurrentCurrency(currency)
    }
}
