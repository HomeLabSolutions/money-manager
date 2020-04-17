package com.d9tilov.moneymanager.settings.di

import com.d9tilov.moneymanager.core.di.BaseFragmentComponent
import com.d9tilov.moneymanager.core.di.CoreComponent
import com.d9tilov.moneymanager.core.di.scope.FeatureScope
import com.d9tilov.moneymanager.settings.ui.SettingsFragment
import dagger.Component

@Component(
    modules = [SettingsModule::class],
    dependencies = [CoreComponent::class]
)
@FeatureScope
interface SettingsComponent : BaseFragmentComponent<SettingsFragment> {

    @Component.Builder
    interface Builder {

        fun build(): SettingsComponent
        fun coreComponent(component: CoreComponent): Builder
        fun settingsModule(module: SettingsModule): Builder
    }
}