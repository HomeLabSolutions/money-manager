package com.d9tilov.android.profile.ui.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.android.common.android.ui.base.BaseViewModel
import com.d9tilov.android.profile.ui.navigation.LogoutDialogNavigator
import com.d9tilov.android.user.domain.contract.UserInteractor
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LogoutViewModel @Inject constructor(
    private val userInteractor: UserInteractor,
    private val firebaseAnalytics: FirebaseAnalytics
) : BaseViewModel<LogoutDialogNavigator>() {

    fun logout() {
        viewModelScope.launch {
            userInteractor.deleteUser()
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
                param(FirebaseAnalytics.Param.ITEM_CATEGORY, "logout")
            }
            withContext(Dispatchers.Main) { navigator?.logout() }
        }
    }
}