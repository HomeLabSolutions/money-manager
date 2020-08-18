package com.d9tilov.moneymanager.category.ui.vm

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.d9tilov.moneymanager.analytics.AnalyticsViewModel
import com.d9tilov.moneymanager.analytics.EventsTracker
import com.d9tilov.moneymanager.base.ui.navigator.CategoryCreationNavigator
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_DATA_ID
import com.d9tilov.moneymanager.core.util.addTo
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler

class CategoryCreationViewModel @ViewModelInject constructor(
    private val categoryInteractor: CategoryInteractor,
    eventsTracker: EventsTracker,
    @Assisted val savedStateHandle: SavedStateHandle
) : AnalyticsViewModel<CategoryCreationNavigator>(eventsTracker) {

    fun save(category: Category) {
        val receivedCategory = savedStateHandle.get<Category>("category")
        if (receivedCategory == null || receivedCategory.id == DEFAULT_DATA_ID) {
            categoryInteractor.create(category)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe { _ ->
                    navigator?.save()
                    logEvent("CATEGORY", "CREATE", category.name)
                }
                .addTo(compositeDisposable)
        } else {
            categoryInteractor.update(category)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe { navigator?.save() }
                .addTo(compositeDisposable)
        }
    }
}
