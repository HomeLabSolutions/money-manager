package com.d9tilov.moneymanager.category.ui.vm

import androidx.hilt.lifecycle.ViewModelInject
import com.d9tilov.moneymanager.base.ui.navigator.RemoveCategoryDialogNavigator
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.addTo
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import io.reactivex.Observable

class CategoryRemoveViewModel @ViewModelInject constructor(
    private val categoryInteractor: CategoryInteractor,
    private val transactionInteractor: TransactionInteractor
) : BaseViewModel<RemoveCategoryDialogNavigator>() {

    fun remove(category: Category) {
        transactionInteractor.removeAllByCategory(category)
            .andThen(
                Observable.fromIterable(category.children)
                    .flatMapCompletable { transactionInteractor.removeAllByCategory(it) }
            )
            .andThen(categoryInteractor.deleteCategory(category))
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe { navigator?.closeDialog() }
            .addTo(compositeDisposable)
    }
}
