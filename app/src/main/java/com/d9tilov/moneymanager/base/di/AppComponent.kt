package com.d9tilov.moneymanager.base.di

import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.base.di.builder.ActivityBuilder
import com.d9tilov.moneymanager.category.di.CategoryModule
import com.d9tilov.moneymanager.settings.di.SettingsModule
import com.d9tilov.moneymanager.user.di.UserModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    ViewModelModule::class,
    NetworkModule::class,
    DatabaseModule::class,
    UserModule::class,
    CategoryModule::class,
    SettingsModule::class,
    AndroidInjectionModule::class,
    ActivityBuilder::class])
interface AppComponent : AndroidInjector<App> {

    @Component.Factory
    interface Builder : AndroidInjector.Factory<App>
}
