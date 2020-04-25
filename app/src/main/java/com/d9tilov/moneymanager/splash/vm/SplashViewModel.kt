package com.d9tilov.moneymanager.splash.vm

import com.d9tilov.moneymanager.base.ui.BaseViewModel
import com.d9tilov.moneymanager.base.ui.navigator.SplashNavigator
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import com.d9tilov.moneymanager.domain.user.UserInfoInteractor
import com.d9tilov.moneymanager.domain.user.mappers.DomainUserMapper
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val userInfoInteractor: UserInfoInteractor,
    private val domainUserMapper: DomainUserMapper
) : BaseViewModel<SplashNavigator>() {

    private val auth = FirebaseAuth.getInstance()

    override fun onNavigatorAttached() {
        super.onNavigatorAttached()
        if (auth.currentUser == null) {
            navigator?.openAuthScreen()
        } else {
            subscribe(userInfoInteractor.getCurrentUser()
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe {
                    if (it.uid == auth.uid) {
                        navigator?.openHomeScreen()
                    } else {
                        auth.signOut()
                        navigator?.openAuthScreen()
                    }
                }
            )
        }
    }

    fun createUser() {
        subscribe(
            userInfoInteractor.createUser(domainUserMapper.toDataModel(auth.currentUser))
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe { navigator?.openHomeScreen() }
        )
    }
}
