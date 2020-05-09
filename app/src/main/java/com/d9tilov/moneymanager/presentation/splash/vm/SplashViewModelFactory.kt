package com.d9tilov.moneymanager.presentation.splash.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.d9tilov.moneymanager.domain.user.UserInfoInteractor
import com.d9tilov.moneymanager.domain.user.mappers.DomainUserMapper

class SplashViewModelFactory constructor(
    private val userInfoInteractor: UserInfoInteractor,
    private val domainUserMapper: DomainUserMapper
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass != SplashViewModel::class.java) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return SplashViewModel(userInfoInteractor, domainUserMapper) as T
    }
}
