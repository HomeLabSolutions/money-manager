package com.d9tilov.moneymanager.user.data.entity

import com.d9tilov.moneymanager.backup.BackupData
import com.d9tilov.moneymanager.base.data.local.db.AppDatabase

data class UserProfile(
    val uid: String,
    val displayedName: String?,
    val firstName: String?,
    val lastName: String?,
    val budgetDayCreation: Long = 0L,
    val currencyCode: String = "USD",
    val showPrepopulate: Boolean = true,
    val backupData: BackupData = BackupData(0, AppDatabase.VERSION_NUMBER)
)
