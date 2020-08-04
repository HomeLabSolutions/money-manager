package com.d9tilov.moneymanager.category.ui.vm

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.d9tilov.moneymanager.base.ui.navigator.CategoryNavigator
import com.d9tilov.moneymanager.category.CategoryDestination
import com.d9tilov.moneymanager.category.common.BaseCategoryViewModel
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.core.util.addTo
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import java.math.BigDecimal

class CategoryViewModel @ViewModelInject constructor(
    private val transactionInteractor: TransactionInteractor,
    private val categoryInteractor: CategoryInteractor,
    @Assisted val savedStateHandle: SavedStateHandle
) : BaseCategoryViewModel<CategoryNavigator>() {

    init {
        categoryInteractor.getAllCategoriesByType(TransactionType.EXPENSE)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe { expenseCategories.value = it }
            .addTo(compositeDisposable)
        categoryInteractor.getAllCategoriesByType(TransactionType.INCOME)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe { incomeCategories.value = it }
            .addTo(compositeDisposable)
    }

    override fun onCategoryClicked(category: Category) {
        val destination = savedStateHandle.get<CategoryDestination>("destination")
        if (category.children.isNotEmpty()) {
            navigator?.openSubCategoryScreen(category)
        } else {
            if (destination == CategoryDestination.EDIT_TRANSACTION_SCREEN) {
                navigator?.backToEditTransactionScreen(category)
            } else if (destination == CategoryDestination.MAIN_WITH_SUM_SCREEN) {
                val inputSum = requireNotNull(savedStateHandle.get<BigDecimal>("sum"))
                val transactionType =
                    requireNotNull(savedStateHandle.get<TransactionType>("transactionType"))
                transactionInteractor.addTransaction(
                    Transaction(
                        type = transactionType,
                        sum = inputSum,
                        category = category
                    )
                )
                    .subscribeOn(ioScheduler)
                    .observeOn(uiScheduler)
                    .subscribe { navigator?.backToMainScreen(transactionType) }
            }
        }
    }

    fun onCategoryLongClicked() {}

    override fun onCategoryRemoved(category: Category) {
        categoryInteractor.deleteCategory(category)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe()
            .addTo(compositeDisposable)
    }

    fun onItemMoveAndUnit(childItem: Category, parentItem: Category) {
    }

    override fun update(name: String) {
        categoryInteractor.update(expenseCategories.value!![0].copy(name = name))
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe()
            .addTo(compositeDisposable)
    }
}