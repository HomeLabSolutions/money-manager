package com.d9tilov.moneymanager.settings.vm

import com.d9tilov.moneymanager.base.BaseViewModel
import com.d9tilov.moneymanager.base.ui.navigator.SettingsNavigator
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class SettingsViewModel @Inject constructor() : BaseViewModel<SettingsNavigator>() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getCurrentUser() = auth.currentUser

}