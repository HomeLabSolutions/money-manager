package com.d9tilov.moneymanager.base.data.local.preferences

import android.content.Context
import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.BASE_NAMESPACE
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_CURRENCY_SYMBOL
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.STORE_NAME
import com.d9tilov.moneymanager.core.util.string
import com.d9tilov.moneymanager.core.util.stringNullable
import com.d9tilov.moneymanager.currency.data.entity.Currency
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesStore @Inject constructor(@ApplicationContext context: Context) {

    private val sharedPreferences = context.getSharedPreferences(
        STORE_NAME,
        Context.MODE_PRIVATE
    )

    var uid by sharedPreferences.stringNullable(key = { PREFERENCE_CLIENT_UID })

    fun getCurrentCurrency(): CurrencyMetaData =
        CurrencyMetaData(currentCurrencyCode, currentCurrencySymbol)

    fun saveCurrentCurrency(currency: Currency) {
        currentCurrencyCode = currency.code
        currentCurrencySymbol = currency.symbol
    }

    private var currentCurrencyCode by sharedPreferences.string(
        defaultValue = DataConstants.DEFAULT_CURRENCY_CODE,
        key = { DataConstants.PREFERENCE_CURRENT_CURRENCY }
    )
    private var currentCurrencySymbol by sharedPreferences.string(
        defaultValue = DEFAULT_CURRENCY_SYMBOL,
        key = { DataConstants.PREFERENCE_CURRENT_CURRENCY_SYMBOL }
    )

    fun clearAllData() {
        val prefs: Map<String, *> = sharedPreferences.all
        for ((key) in prefs) {
            sharedPreferences.edit().remove(key).apply()
        }
    }

    companion object {
        const val PREFERENCE_CLIENT_UID = BASE_NAMESPACE + "current.client.uid"
    }
}
