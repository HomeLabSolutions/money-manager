package com.d9tilov.android.user.di

import com.d9tilov.android.user.data.contract.UserSource
import com.d9tilov.android.user.data.impl.UserDataRepo
import com.d9tilov.android.user.data.impl.UserLocalSource
import com.d9tilov.android.user.domain.contract.UserRepo
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface UserDataModule {
    @Binds
    val UserLocalSource.provideUserSource: UserSource

    @Binds
    val UserDataRepo.provideUserRepo: UserRepo
}
