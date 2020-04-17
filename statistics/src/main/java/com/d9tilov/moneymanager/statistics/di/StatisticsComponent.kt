package com.d9tilov.moneymanager.statistics.di

import com.d9tilov.moneymanager.core.di.BaseFragmentComponent
import com.d9tilov.moneymanager.core.di.CoreComponent
import com.d9tilov.moneymanager.core.di.scope.FeatureScope
import com.d9tilov.moneymanager.statistics.ui.StatisticsFragment
import dagger.Component

@Component(
    modules = [StatisticsModule::class],
    dependencies = [CoreComponent::class]
)
@FeatureScope
interface StatisticsComponent : BaseFragmentComponent<StatisticsFragment> {

    @Component.Builder
    interface Builder {

        fun build(): StatisticsComponent
        fun coreComponent(component: CoreComponent): Builder
        fun statisticsModule(module: StatisticsModule): Builder
    }
}
