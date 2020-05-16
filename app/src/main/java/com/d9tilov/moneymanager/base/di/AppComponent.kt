package com.d9tilov.moneymanager.base.di

import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.base.di.builder.ActivityBuilder
import com.d9tilov.moneymanager.data.di.DatabaseModule
import com.d9tilov.moneymanager.data.di.NetworkModule
import com.d9tilov.moneymanager.data.di.UserDataModule
import com.d9tilov.moneymanager.domain.di.UserDomainModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    NetworkModule::class,
    DatabaseModule::class,
    UserDataModule::class,
    UserDomainModule::class,
    AndroidInjectionModule::class,
    ActivityBuilder::class])
interface AppComponent : AndroidInjector<App> {

    @Component.Factory
    interface Builder : AndroidInjector.Factory<App>
}
