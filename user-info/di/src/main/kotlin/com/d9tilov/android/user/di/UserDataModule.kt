package com.d9tilov.android.user.di

import com.d9tilov.android.user.data.contract.UserSource
import com.d9tilov.android.user.data.impl.UserDataRepo
import com.d9tilov.android.user.data.impl.UserLocalSource
import com.d9tilov.android.user.domain.contract.UserRepo
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory

val userDataModule =
    module {
        factory<UserLocalSource>() bind UserSource::class
        factory<UserDataRepo>() bind UserRepo::class
    }
