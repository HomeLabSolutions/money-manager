package com.d9tilov.moneymanager.incomeexpense.expense.di

import com.d9tilov.moneymanager.core.di.BaseFragmentComponent
import com.d9tilov.moneymanager.core.di.CoreComponent
import com.d9tilov.moneymanager.core.di.scope.FeatureScope
import com.d9tilov.moneymanager.incomeexpense.expense.ui.ExpenseFragment
import dagger.Component

@Component(
    modules = [ExpenseModule::class],
    dependencies = [CoreComponent::class]
)
@FeatureScope
interface ExpenseComponent : BaseFragmentComponent<ExpenseFragment> {

    @Component.Builder
    interface Builder {

        fun build(): ExpenseComponent
        fun coreComponent(component: CoreComponent): Builder
        fun expenseModule(module: ExpenseModule): Builder
    }
}