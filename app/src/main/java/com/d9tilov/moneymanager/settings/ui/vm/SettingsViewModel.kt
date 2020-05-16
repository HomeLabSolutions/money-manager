package com.d9tilov.moneymanager.settings.ui.vm

import com.d9tilov.moneymanager.backup.domain.BackupInteractor
import com.d9tilov.moneymanager.base.ui.BaseViewModel
import com.d9tilov.moneymanager.base.ui.navigator.SettingsNavigator
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import com.d9tilov.moneymanager.domain.user.UserInfoInteractor
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val userInfoInteractor: UserInfoInteractor,
    private val backupInteractor: BackupInteractor
) : BaseViewModel<SettingsNavigator>() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getCurrentUser() = auth.currentUser

    fun logout() {
        subscribe(
            userInfoInteractor.logout()
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe()
        )
    }

    fun backup() {
        subscribe(
            backupInteractor.backupDatabase()
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe()
        )
    }
}
