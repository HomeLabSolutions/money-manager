package com.d9tilov.moneymanager.settings.data.local

import com.d9tilov.moneymanager.backup.BackupManager
import io.reactivex.Completable

class SettingsLocalSourceImpl(
    private val backupManager: BackupManager
) : SettingsLocalSource {

    override fun backupDatabase(): Completable {
        return Completable.fromAction { backupManager.backupDatabase() }
    }

    override fun restoreDatabase(): Completable {
        return Completable.fromAction { backupManager.restoreDatabase() }
    }
}
