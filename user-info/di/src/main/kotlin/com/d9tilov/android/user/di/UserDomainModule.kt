package com.d9tilov.android.user.di

import com.d9tilov.android.user.domain.contract.UserInteractor
import com.d9tilov.android.user.domain.contract.UserRepo
import com.d9tilov.android.user.domain.impl.UserInteractorImpl
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import org.koin.plugin.module.dsl.factory

fun provideUserInteractor(userRepo: UserRepo): UserInteractor = UserInteractorImpl(userRepo)

val userDomainModule =
    module {
        factory { create(::provideUserInteractor) }
    }
