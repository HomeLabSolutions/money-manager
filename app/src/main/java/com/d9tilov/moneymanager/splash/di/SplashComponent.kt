package com.d9tilov.moneymanager.splash.di

import com.d9tilov.moneymanager.base.di.AppComponent
import com.d9tilov.moneymanager.core.di.BaseActivityComponent
import com.d9tilov.moneymanager.core.di.scope.FeatureScope
import com.d9tilov.moneymanager.data.di.DatabaseModule
import com.d9tilov.moneymanager.data.di.UserDataModule
import com.d9tilov.moneymanager.splash.ui.SplashActivity
import dagger.Component

@Component(
    modules = [
        SplashModule::class,
        UserDataModule::class,
        DatabaseModule::class
    ],
    dependencies = [AppComponent::class]
)
@FeatureScope
interface SplashComponent : BaseActivityComponent<SplashActivity> {

    @Component.Builder
    interface Builder {

        fun build(): SplashComponent
        fun appComponent(component: AppComponent): Builder
        fun splashModule(module: SplashModule): Builder
        fun userModule(module: UserDataModule): Builder
        fun databaseModule(module: DatabaseModule): Builder
    }
}
