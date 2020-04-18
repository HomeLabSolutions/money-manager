package com.d9tilov.moneymanager.incomeexpense.presentation.income.di

import com.d9tilov.moneymanager.core.di.BaseFragmentComponent
import com.d9tilov.moneymanager.core.di.CoreComponent
import com.d9tilov.moneymanager.core.di.scope.FeatureScope
import com.d9tilov.moneymanager.incomeexpense.presentation.income.ui.IncomeFragment
import dagger.Component

@Component(
    modules = [IncomeModule::class],
    dependencies = [CoreComponent::class]
)
@FeatureScope
interface IncomeComponent : BaseFragmentComponent<IncomeFragment> {

    @Component.Builder
    interface Builder {

        fun build(): IncomeComponent
        fun coreComponent(component: CoreComponent): Builder
        fun incomeModule(module: IncomeModule): Builder
    }
}
