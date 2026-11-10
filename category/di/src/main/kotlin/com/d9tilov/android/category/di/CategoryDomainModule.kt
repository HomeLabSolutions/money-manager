package com.d9tilov.android.category.di

import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.impl.CategoryInteractorImpl
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory

val categoryDomainModule =
    module {
        factory<CategoryInteractorImpl>() bind CategoryInteractor::class
    }
