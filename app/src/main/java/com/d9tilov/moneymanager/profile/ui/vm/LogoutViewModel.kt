package com.d9tilov.moneymanager.profile.ui.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.LogoutDialogNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.user.domain.UserInteractor
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogoutViewModel @Inject constructor(
    private val userInteractor: UserInteractor,
    private val firebaseAnalytics: FirebaseAnalytics
) : BaseViewModel<LogoutDialogNavigator>() {

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) { userInteractor.deleteUser() }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
            param(
                FirebaseAnalytics.Param.ITEM_CATEGORY,
                "logout"
            )
        }
        navigator?.logout()
    }
}
