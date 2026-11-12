package com.d9tilov.android.user.di

import com.d9tilov.android.user.domain.contract.UserInteractor
import com.d9tilov.android.user.domain.contract.UserRepo
import com.d9tilov.android.user.domain.impl.UserInteractorImpl
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

@BindingContainer
@ContributesTo(AppScope::class)
object UserDomainModule {
    @Provides
    fun provideUserInteractor(userRepo: UserRepo): UserInteractor = UserInteractorImpl(userRepo)
}
