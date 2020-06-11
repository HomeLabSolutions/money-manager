package com.d9tilov.moneymanager.settings.ui.vm

import androidx.lifecycle.MutableLiveData
import com.d9tilov.moneymanager.base.ui.navigator.SettingsNavigator
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.addTo
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import com.d9tilov.moneymanager.settings.domain.SettingsInteractor
import com.d9tilov.moneymanager.user.domain.UserInteractor
import com.google.firebase.auth.FirebaseAuth

class SettingsViewModel(
    private val userInfoInteractor: UserInteractor,
    private val categoryInteractor: CategoryInteractor,
    private val settingsInteractor: SettingsInteractor
) : BaseViewModel<SettingsNavigator>() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val numberLiveData = MutableLiveData<Int>()

    fun getCurrentUser() = auth.currentUser

    fun logout() {
        userInfoInteractor.logout()
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe()
            .addTo(compositeDisposable)
    }

    fun backup() {
        categoryInteractor.createExpenseDefaultCategories()
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe()
            .addTo(compositeDisposable)
    }

    fun save() {
        settingsInteractor.saveNumber()
    }

    fun restore() {
        numberLiveData.value = settingsInteractor.restoreNumber()
    }
}
