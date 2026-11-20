package com.d9tilov.moneymanager.di

import android.app.Application
import com.d9tilov.android.common.android.worker.WorkerFactoryOwner
import com.d9tilov.android.widget.ui.SpendingWidgetEntryPoint
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.viewmodel.ViewModelGraph

@DependencyGraph(AppScope::class)
interface AppGraph :
    ViewModelGraph,
    WorkerFactoryOwner,
    SpendingWidgetEntryPoint {
    @DependencyGraph.Factory
    fun interface Factory {
        fun create(
            @Provides application: Application,
        ): AppGraph
    }
}
