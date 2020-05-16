package com.d9tilov.moneymanager.backup.domain

import io.reactivex.Completable

interface BackupInteractor {

    fun backupDatabase(): Completable
    fun restoreDatabase(): Completable
}
