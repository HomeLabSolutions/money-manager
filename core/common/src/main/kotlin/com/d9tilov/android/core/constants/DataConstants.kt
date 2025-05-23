package com.d9tilov.android.core.constants

object DataConstants {
    const val TAG = "[MoneyManager]"
    private const val BASE_NAMESPACE = "com.d9tilov.moneymanager"
    const val DATABASE_NAME = "money-manager-db"
    const val STORE_NAME = "MoneyManagerPreferencesStore"
    const val DATA_STORE_NAME = "MoneyManagerDataPreferencesStore"
    const val PREFERENCE_CLIENT_UID = BASE_NAMESPACE + "current.client.uid"
    const val PREFERENCE_LOCAL_CURRENCY = BASE_NAMESPACE + "local.currency"
    const val PREFERENCE_LAST_BACKUP_DATE = BASE_NAMESPACE + "last.backup.date"
    const val DEFAULT_DATA_ID = 0L
    const val NO_ID = -1L
    const val UNKNOWN_BACKUP_DATE = -1L
    const val NO_RES_ID = -1
}
