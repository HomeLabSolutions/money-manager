package com.d9tilov.moneymanager.category.subcategory.vm

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.d9tilov.moneymanager.base.ui.navigator.SubCategoryNavigator
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

class SubCategoryViewModel @ViewModelInject constructor(
    private val transactionInteractor: TransactionInteractor,
    private val categoryInteractor: CategoryInteractor,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseCategoryViewModel<SubCategoryNavigator>() {

    init {
        val parentCategory = savedStateHandle.get<Category>("parent_category")
        if (parentCategory == null || parentCategory.children.isEmpty()) {
            throw IllegalArgumentException("Parent category mustn't have at least one child")
        }
        categoryInteractor.getChildrenByParent(parentCategory)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe { list ->
                expenseCategories.value = list
            }
            .addTo(compositeDisposable)
    }

    override fun onCategoryClicked(category: Category) {
        when (savedStateHandle.get<CategoryDestination>("destination")) {
            CategoryDestination.EDIT_TRANSACTION_SCREEN -> navigator?.backToEditTransactionScreen(
                category
            )
            CategoryDestination.MAIN_WITH_SUM_SCREEN -> {
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
            else -> navigator?.openCreateCategoryScreen(category)
        }
    }

    override fun onCategoryRemoved(category: Category) {
        navigator?.openRemoveDialog(category)
    }

    override fun update(name: String) {
        TODO("Not yet implemented")
    }
}
