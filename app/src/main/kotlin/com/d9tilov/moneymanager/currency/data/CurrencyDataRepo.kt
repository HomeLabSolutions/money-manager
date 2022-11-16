package com.d9tilov.moneymanager.currency.data

import android.util.Log
import com.d9tilov.moneymanager.base.data.local.preferences.CurrencyMetaData
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.currency.data.entity.Currency
import com.d9tilov.moneymanager.currency.data.local.CurrencySource
import com.d9tilov.moneymanager.currency.data.remote.CurrencyApi
import com.d9tilov.moneymanager.currency.data.remote.mapper.toDataModel
import com.d9tilov.moneymanager.currency.domain.CurrencyRepo
import kotlinx.coroutines.flow.Flow

class CurrencyDataRepo(
    private val preferencesStore: PreferencesStore,
    private val currencySource: CurrencySource,
    private val currencyApi: CurrencyApi
) : CurrencyRepo {

    override fun getCurrencies(): Flow<List<Currency>> = currencySource.getCurrencies()

    override fun getCurrentCurrency(): Flow<CurrencyMetaData> = preferencesStore.currentCurrency

    override suspend fun getCurrencyByCode(code: String): Currency =
        currencySource.getCurrencyByCode(code)

    override suspend fun updateCurrencies() {
        val currencies = currencyApi.getCurrencies()
        val remoteCurrencyList = currencies.toDataModel()
        currencySource.saveCurrencies(remoteCurrencyList)
    }

    override suspend fun hasAlreadyUpdatedToday(baseCurrency: String): Boolean =
        currencySource.hasAlreadyUpdatedToday(baseCurrency)
}
