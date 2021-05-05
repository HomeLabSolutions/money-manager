package com.d9tilov.moneymanager.splash.vm

import androidx.hilt.lifecycle.ViewModelInject
import com.d9tilov.moneymanager.App.Companion.TAG
import com.d9tilov.moneymanager.base.ui.navigator.SplashNavigator
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.addTo
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import com.d9tilov.moneymanager.user.domain.UserInteractor
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

class SplashViewModel @ViewModelInject constructor(
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
            userInfoInteractor.getCurrentUser()
                .firstOrError()
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .doOnSuccess {
                    if (it.uid != auth.uid) {
                        auth.signOut()
                        navigator?.openAuthScreen()
                    }
                }
                .filter { it.uid == auth.uid }
                .map { it.showPrepopulate }
                .subscribe(
                    {
                        if (it) {
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
                    },
                    {
                        Timber.tag(TAG).d("Error while getting user: ${it.message}")
                    }
                )
                .addTo(compositeDisposable)
        }
    }

    fun createUser() {
        userInfoInteractor.createUser(auth.currentUser)
            .flatMapCompletable { categoryInteractor.createDefaultCategories() }
            .toSingleDefault(true)
            .flatMap { userInfoInteractor.showPrepopulate() }
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe({ openPrepopulate ->
                if (openPrepopulate) {
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
            }, { Timber.tag(TAG).d(it) })
            .addTo(compositeDisposable)
    }
}
