package com.d9tilov.moneymanager.core.constants

class DataConstants {
    companion object {
        const val DATABASE_NAME = "money-manager-db"
        const val STORE_NAME = "MoneyManagerPreferencesStore"
        const val BASE_NAMESPACE = "com.d9tilov.moneymanager"
        const val PREFERENCE_BASE_CURRENCY = BASE_NAMESPACE + "base.currency"
        const val PREFERENCE_BASE_CURRENCY_SYMBOL = BASE_NAMESPACE + "base.currency.symbol"
        const val DEFAULT_DATA_ID = 0L
        const val DEFAULT_CURRENCY_CODE = "USD"
        const val DEFAULT_CURRENCY_SYMBOL = "$"
        const val NO_ID = -1L
    }
}