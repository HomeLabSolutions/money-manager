package com.d9tilov.moneymanager.splash.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.SplashNavigator
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.user.domain.UserInteractor
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userInfoInteractor: UserInteractor,
    private val categoryInteractor: CategoryInteractor,
    private val firebaseAnalytics: FirebaseAnalytics
) : BaseViewModel<SplashNavigator>() {

    private val auth = FirebaseAuth.getInstance()

    override fun onNavigatorAttached() {
        super.onNavigatorAttached()
        if (auth.currentUser == null) {
            navigator?.openAuthScreen()
        } else {
            viewModelScope.launch {
                userInfoInteractor.getCurrentUser().collectLatest {
                    if (it.uid != auth.uid) {
                        auth.signOut()
                        navigator?.openAuthScreen()
                    } else {
                        if (it.showPrepopulate) {
                            navigator?.openPrepopulate()
                            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                                param(
                                    FirebaseAnalytics.Param.ITEM_ID, "prepopulate_screen"
                                )
                            }
                        } else {
                            navigator?.openHomeScreen()
                            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
                                param(
                                    FirebaseAnalytics.Param.ITEM_ID,
                                    auth.currentUser?.email ?: "unknown email"
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun createUser() = viewModelScope.launch {
        val user = userInfoInteractor.createUser(auth.currentUser)
        categoryInteractor.createDefaultCategories()
        if (user.showPrepopulate) {
            navigator?.openPrepopulate()
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                param(
                    FirebaseAnalytics.Param.ITEM_ID, "prepopulate_screen"
                )
            }
        } else {
            navigator?.openHomeScreen()
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP) {
                param(
                    FirebaseAnalytics.Param.ITEM_ID,
                    auth.currentUser?.email ?: "unknown email"
                )
            }
        }
    }
}
