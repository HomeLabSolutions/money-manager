package com.d9tilov.moneymanager.category.ui.vm

import androidx.hilt.lifecycle.ViewModelInject
import com.d9tilov.moneymanager.base.ui.navigator.EditCategoryDialogNavigator
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.addTo
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler

class CategoryGroupEditViewModel @ViewModelInject constructor(
    private val categoryInteractor: CategoryInteractor
) :
    BaseViewModel<EditCategoryDialogNavigator>() {

    fun save(category: Category) {
        categoryInteractor.update(category)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe({ navigator?.save() }, { navigator?.showError(it) })
            .addTo(compositeDisposable)
    }
}
