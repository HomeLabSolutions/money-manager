package com.d9tilov.android.datastore.model

import com.d9tilov.android.core.constants.CurrencyConstants.VERSION_NUMBER
import com.d9tilov.android.datastore.PreferencesStore.Companion.UNKNOWN_BACKUP_DATE

data class BackupData(
    val lastBackupTimestamp: Long,
    val dbVersion: Int
) {

    companion object {
        val EMPTY = BackupData(
            lastBackupTimestamp = UNKNOWN_BACKUP_DATE,
            dbVersion = VERSION_NUMBER
        )
    }
}
