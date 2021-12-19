package com.d9tilov.moneymanager.user.data.entity

import com.d9tilov.moneymanager.backup.BackupData
import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.core.constants.DataConstants

data class UserProfile(
    val uid: String,
    val displayedName: String?,
    val firstName: String?,
    val lastName: String?,
    val currentCurrencyCode: String = DataConstants.DEFAULT_CURRENCY_CODE,
    val showPrepopulate: Boolean = true,
    val backupData: BackupData = BackupData(0, AppDatabase.VERSION_NUMBER),
    val fiscalDay: Int = 1
)
