package com.d9tilov.moneymanager.splash.di

import com.d9tilov.moneymanager.core.di.BaseActivityComponent
import com.d9tilov.moneymanager.core.di.CoreComponent
import com.d9tilov.moneymanager.core.di.scope.FeatureScope
import com.d9tilov.moneymanager.splash.ui.SplashActivity
import dagger.Component

@Component(
    modules = [SplashModule::class],
    dependencies = [CoreComponent::class]
)
@FeatureScope
interface SplashComponent : BaseActivityComponent<SplashActivity> {

    @Component.Builder
    interface Builder {

        fun build(): SplashComponent
        fun coreComponent(component: CoreComponent): Builder
        fun splashModule(module: SplashModule): Builder
    }
}
