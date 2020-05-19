package com.d9tilov.moneymanager.backup.domain

import com.d9tilov.moneymanager.backup.BackupManager
import io.reactivex.Completable

class BackupInteractorImpl constructor(private val backupManager: BackupManager) :
    BackupInteractor {

    override fun backupDatabase() = backupManager.backupDatabase()
    override fun restoreDatabase() = Completable.fromAction { backupManager.restoreDatabase() }
}
