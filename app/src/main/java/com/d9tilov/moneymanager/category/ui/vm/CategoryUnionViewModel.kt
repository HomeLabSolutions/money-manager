package com.d9tilov.moneymanager.category.ui.vm

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.d9tilov.moneymanager.base.ui.navigator.CategoryUnionDialogNavigator
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.addTo
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler

class CategoryUnionViewModel @ViewModelInject constructor(
    private val categoryInteractor: CategoryInteractor,
    @Assisted val savedStateHandle: SavedStateHandle
) : BaseViewModel<CategoryUnionDialogNavigator>() {

    fun addToGroup(categoryItem: Category, parentCategory: Category) {
        categoryInteractor.update(categoryItem.copy(parent = parentCategory))
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe({ navigator?.accept() }, { navigator?.showError(it) })
            .addTo(compositeDisposable)
    }

    fun createGroup(categoryItem1: Category, categoryItem2: Category, groupedCategory: Category) {
        categoryInteractor.create(groupedCategory)
            .flatMapMaybe { parentId -> categoryInteractor.getCategoryById(parentId) }
            .flatMapCompletable {
                categoryInteractor.update(categoryItem1.copy(parent = it))
                    .andThen(categoryInteractor.update(categoryItem2.copy(parent = it)))
            }
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe({ navigator?.accept() }, { navigator?.showError(it) })
            .addTo(compositeDisposable)
    }

    fun cancel() {
        navigator?.cancel()
    }
}
