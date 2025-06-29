package com.d9tilov.android.user.di

import com.d9tilov.android.user.data.contract.UserSource
import com.d9tilov.android.user.data.impl.UserDataRepo
import com.d9tilov.android.user.data.impl.UserLocalSource
import com.d9tilov.android.user.domain.contract.UserRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UserDataModule {
    @Binds
    fun provideUserSource(impl: UserLocalSource): UserSource

    @Binds
    fun provideUserRepo(impl: UserDataRepo): UserRepo
}
