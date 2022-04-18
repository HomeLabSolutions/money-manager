package com.d9tilov.moneymanager.currency.data

import com.d9tilov.moneymanager.base.data.local.preferences.CurrencyMetaData
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.currency.data.entity.Currency
import com.d9tilov.moneymanager.currency.data.local.CurrencySource
import com.d9tilov.moneymanager.currency.data.remote.CurrencyApi
import com.d9tilov.moneymanager.currency.data.remote.mapper.toDataModel
import com.d9tilov.moneymanager.currency.data.remote.mapper.toDataModelValues
import com.d9tilov.moneymanager.currency.domain.CurrencyRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CurrencyDataRepo(
    private val preferencesStore: PreferencesStore,
    private val currencySource: CurrencySource,
    private val currencyApi: CurrencyApi
) : CurrencyRepo {

    override fun getCurrencies(): Flow<List<Currency>> {
        return currencySource.getCurrencies().map { list ->
            if (list.isEmpty()) {
                val currencies = currencyApi.getCurrencies()
                val remoteCurrencyList = currencies.toDataModel()
                currencySource.saveCurrencies(remoteCurrencyList)
            }
            list
        }
    }

    override suspend fun getCurrentCurrency(): CurrencyMetaData =
        withContext(Dispatchers.IO) { preferencesStore.currentCurrency.first() }

    override suspend fun getCurrencyByCode(code: String): Currency =
        currencySource.getCurrencyByCode(code)

    override suspend fun updateCurrencies() {
        currencySource.getCurrencies().map { list ->
            val currencies = currencyApi.getCurrencies()
            val remoteCurrencyList =
                if (list.isEmpty()) currencies.toDataModel()
                else currencies.toDataModelValues(list)
            currencySource.saveCurrencies(remoteCurrencyList)
        }.first()
    }

    override suspend fun updateCurrentCurrency(code: String) {
        preferencesStore.updateCurrentCurrency(code)
    }

    override suspend fun hasAlreadyUpdatedToday(baseCurrency: String): Boolean =
        currencySource.hasAlreadyUpdatedToday(baseCurrency)
}
