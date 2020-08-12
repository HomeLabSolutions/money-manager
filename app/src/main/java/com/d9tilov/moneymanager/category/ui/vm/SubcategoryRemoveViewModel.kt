package com.d9tilov.moneymanager.category.ui.vm

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.d9tilov.moneymanager.base.ui.navigator.RemoveSubCategoryDialogNavigator
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.addTo
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor

class SubcategoryRemoveViewModel @ViewModelInject constructor(
    private val categoryInteractor: CategoryInteractor,
    private val transactionInteractor: TransactionInteractor,
    @Assisted val savedStateHandle: SavedStateHandle
) : BaseViewModel<RemoveSubCategoryDialogNavigator>() {

    fun remove(subcategory: Category) {
        transactionInteractor.removeAllByCategory(subcategory)
            .toSingleDefault(false)
            .flatMap { categoryInteractor.deleteSubCategory(subcategory) }
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe { parentRemoved ->
                if (parentRemoved) {
                    navigator?.closeDialogAndGoToCategory()
                } else {
                    navigator?.closeDialog()
                }
            }
            .addTo(compositeDisposable)
    }

    fun removeFromGroup(subcategory: Category) {
        categoryInteractor.deleteFromGroup(subcategory)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe { parentRemoved ->
                if (parentRemoved) {
                    navigator?.closeDialogAndGoToCategory()
                } else {
                    navigator?.closeDialog()
                }
            }
            .addTo(compositeDisposable)
    }
}