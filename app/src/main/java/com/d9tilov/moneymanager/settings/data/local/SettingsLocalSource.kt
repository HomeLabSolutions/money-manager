package com.d9tilov.moneymanager.settings.data.local

import io.reactivex.Completable

interface SettingsLocalSource {

    fun backupDatabase(): Completable

    fun restoreDatabase(): Completable
}
