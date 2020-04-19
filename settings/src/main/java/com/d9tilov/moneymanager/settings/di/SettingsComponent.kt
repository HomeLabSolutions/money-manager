package com.d9tilov.moneymanager.settings.di

import com.d9tilov.moneymanager.base.di.AppComponent
import com.d9tilov.moneymanager.core.di.BaseFragmentComponent
import com.d9tilov.moneymanager.core.di.scope.FeatureScope
import com.d9tilov.moneymanager.settings.ui.SettingsFragment
import dagger.Component

@Component(
    modules = [SettingsModule::class],
    dependencies = [AppComponent::class]
)
@FeatureScope
interface SettingsComponent : BaseFragmentComponent<SettingsFragment> {

    @Component.Builder
    interface Builder {

        fun build(): SettingsComponent
        fun appComponent(component: AppComponent): Builder
        fun settingsModule(module: SettingsModule): Builder
    }
}