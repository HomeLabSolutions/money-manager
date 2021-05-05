package com.d9tilov.moneymanager.settings.ui.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.navigator.SettingsNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.addTo
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import com.d9tilov.moneymanager.user.domain.UserInteractor
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SettingsViewModel @ViewModelInject constructor(
    private val userInfoInteractor: UserInteractor,
    private val firebaseAnalytics: FirebaseAnalytics
) : BaseViewModel<SettingsNavigator>() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val backupLastDate = MutableLiveData<Long>()

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    init {
        userInfoInteractor.getBackupData()
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe { backupLastDate.value = it.lastBackupTimestamp }
            .addTo(compositeDisposable)
    }

    fun logout() {
        userInfoInteractor.logout()
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe {
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
                    param(FirebaseAnalytics.Param.ITEM_CATEGORY, "logout")
                }
            }
            .addTo(compositeDisposable)
    }

    fun backup() {
        userInfoInteractor.backup()
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe(
                { setMessage(R.string.backup_succeeded) },
                { setMessage(R.string.backup_error) }
            )
            .addTo(compositeDisposable)
    }

    fun getBackupLastDate(): LiveData<Long> = backupLastDate
}
