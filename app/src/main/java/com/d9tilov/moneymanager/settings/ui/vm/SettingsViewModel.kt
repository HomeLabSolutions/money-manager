package com.d9tilov.moneymanager.settings.ui.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.backup.BackupData
import com.d9tilov.moneymanager.base.ui.navigator.SettingsNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.user.domain.UserInteractor
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class SettingsViewModel @ViewModelInject constructor(
    private val userInfoInteractor: UserInteractor,
    private val firebaseAnalytics: FirebaseAnalytics
) : BaseViewModel<SettingsNavigator>() {

    val backupData: LiveData<BackupData> = userInfoInteractor.getBackupData().asLiveData()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val settingsExceptionHandler = CoroutineExceptionHandler { _, _ ->
        setMessage(R.string.backup_error)
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun logout() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
            param(
                FirebaseAnalytics.Param.ITEM_CATEGORY,
                "logout"
            )
        }
    }

    fun backup() = viewModelScope.launch(settingsExceptionHandler) {
        userInfoInteractor.backup()
        setMessage(R.string.backup_succeeded)
    }
}
