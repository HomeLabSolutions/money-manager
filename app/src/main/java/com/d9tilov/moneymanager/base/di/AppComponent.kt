package com.d9tilov.moneymanager.base.di

import android.app.Application
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.base.di.builder.ActivityBuilder
import com.d9tilov.moneymanager.category.di.CategoryModule
import com.d9tilov.moneymanager.settings.di.SettingsModule
import com.d9tilov.moneymanager.transaction.di.TransactionModule
import com.d9tilov.moneymanager.user.di.UserModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityBuilder::class,
        AppModule::class,

        NetworkModule::class,
        DatabaseModule::class,

        UserModule::class,
        CategoryModule::class,
        SettingsModule::class,
        TransactionModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    override fun inject(app: App)
}
