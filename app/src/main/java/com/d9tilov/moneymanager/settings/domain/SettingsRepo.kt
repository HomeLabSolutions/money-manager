package com.d9tilov.moneymanager.settings.domain

import io.reactivex.Completable

interface SettingsRepo {

    fun backupDatabase():Completable
    fun restoreDatabase() : Completable
}