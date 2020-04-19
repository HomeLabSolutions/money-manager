package com.d9tilov.moneymanager.splash.vm

import android.util.Log
import com.d9tilov.moneymanager.base.ui.BaseViewModel
import com.d9tilov.moneymanager.base.ui.navigator.SplashNavigator
import com.d9tilov.moneymanager.domain.user.UserInfoInteractor
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class SplashViewModel @Inject constructor(userInfoInteractor: UserInfoInteractor) : BaseViewModel<SplashNavigator>() {

    private val auth = FirebaseAuth.getInstance()

    override fun onNavigatorAttached() {
        super.onNavigatorAttached()
        Log.d("moggot", "onRouterAttached uid = " + auth.currentUser?.uid)
        if (auth.currentUser == null) {
            getNavigator()?.openAuthScreen()
        } else {
            getNavigator()?.openHomeScreen()
        }
    }

    fun createUser() {

    }
}
