package com.d9tilov.moneymanager.settings.ui.vm

import androidx.lifecycle.MutableLiveData
import com.d9tilov.moneymanager.base.ui.BaseViewModel
import com.d9tilov.moneymanager.base.ui.navigator.SettingsNavigator
import com.d9tilov.moneymanager.category.domain.ICategoryInteractor
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import com.d9tilov.moneymanager.settings.domain.ISettingsInteractor
import com.d9tilov.moneymanager.user.domain.IUserInfoInteractor
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val userInfoInteractor: IUserInfoInteractor,
    private val categoryInteractor: ICategoryInteractor,
    private val settingsInteractor: ISettingsInteractor
) : BaseViewModel<SettingsNavigator>() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val numberLiveData = MutableLiveData<Int>()

    fun getCurrentUser() = auth.currentUser

    fun logout() {
        unsubscribeOnDetach(
            userInfoInteractor.logout()
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe()
        )
    }

    fun backup() {
        unsubscribeOnDetach(
            categoryInteractor.createExpenseDefaultCategories()
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe()
        )
    }

    fun save() {
        settingsInteractor.saveNumber()
    }

    fun restore() {
        numberLiveData.value = settingsInteractor.restoreNumber()
    }
}
