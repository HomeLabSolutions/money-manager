package com.d9tilov.moneymanager.splash.vm

import com.d9tilov.moneymanager.App.Companion.TAG
import com.d9tilov.moneymanager.base.ui.navigator.SplashNavigator
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.addTo
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import com.d9tilov.moneymanager.user.domain.UserInteractor
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

class SplashViewModel(
    private val userInfoInteractor: UserInteractor,
    private val categoryInteractor: CategoryInteractor
) : BaseViewModel<SplashNavigator>() {

    private val auth = FirebaseAuth.getInstance()

    override fun onNavigatorAttached() {
        super.onNavigatorAttached()
        if (auth.currentUser == null) {
            navigator?.openAuthScreen()
        } else {
            userInfoInteractor.getCurrentUser()
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe(
                    {
                        if (it.uid == auth.uid) {
                            navigator?.openHomeScreen()
                        } else {
                            auth.signOut()
                            navigator?.openAuthScreen()
                        }
                    },
                    { Timber.tag(TAG).d("Error while getting user: ${it.message}") }
                )
                .addTo(compositeDisposable)
        }
    }

    fun createUser() {
        userInfoInteractor.createUserAndDefaultCategories(auth.currentUser)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe { navigator?.openHomeScreen() }
            .addTo(compositeDisposable)
    }
}
