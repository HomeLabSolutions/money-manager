package com.d9tilov.moneymanager.splash.vm

import com.d9tilov.moneymanager.App.Companion.TAG
import com.d9tilov.moneymanager.base.ui.BaseViewModel
import com.d9tilov.moneymanager.base.ui.navigator.SplashNavigator
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import com.d9tilov.moneymanager.user.domain.IUserInfoInteractor
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

class SplashViewModel(
    private val userInfoInteractor: IUserInfoInteractor
) : BaseViewModel<SplashNavigator>() {

    private val auth = FirebaseAuth.getInstance()

    override fun onNavigatorAttached() {
        super.onNavigatorAttached()
        if (auth.currentUser == null) {
            navigator?.openAuthScreen()
        } else {
            unsubscribeOnDetach(
                userInfoInteractor.getCurrentUser()
                    .subscribeOn(ioScheduler)
                    .observeOn(uiScheduler)
                    .subscribe({
                        if (it.uid == auth.uid) {
                            navigator?.openHomeScreen()
                        } else {
                            auth.signOut()
                            navigator?.openAuthScreen()
                        }
                    }, {
                        Timber.tag(TAG).d("Error while getting user: ${it.message}") })
            )
        }
    }

    fun createUser() {
        unsubscribeOnDetach(
            userInfoInteractor.createUser(auth.currentUser)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe { navigator?.openHomeScreen() }
        )
    }
}
