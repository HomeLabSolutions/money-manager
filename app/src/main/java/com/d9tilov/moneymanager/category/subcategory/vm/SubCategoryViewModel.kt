package com.d9tilov.moneymanager.category.subcategory.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.SubCategoryNavigator
import com.d9tilov.moneymanager.category.CategoryDestination
import com.d9tilov.moneymanager.category.common.BaseCategoryViewModel
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import java.math.BigDecimal

class SubCategoryViewModel @AssistedInject constructor(
    private val transactionInteractor: TransactionInteractor,
    categoryInteractor: CategoryInteractor,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseCategoryViewModel<SubCategoryNavigator>() {

    init {
        val parentCategory = savedStateHandle.get<Category>("parent_category")
        if (parentCategory == null || parentCategory.children.isEmpty()) {
            throw IllegalArgumentException("Parent category mustn't have at least one child")
        }
        viewModelScope.launch {
            categories = categoryInteractor.getChildrenByParent(parentCategory).asLiveData()
        }
    }

    override fun onCategoryClicked(category: Category) {
        when (savedStateHandle.get<CategoryDestination>("destination")) {
            CategoryDestination.EDIT_TRANSACTION_SCREEN -> navigator?.backToEditTransactionScreen(
                category
            )
            CategoryDestination.MAIN_WITH_SUM_SCREEN, CategoryDestination.PREPOPULATE_SCREEN -> {
                val inputSum = savedStateHandle.get<BigDecimal>("sum")
                if (inputSum == null) {
                    navigator?.backToRegularTransactionCreationScreen(category)
                } else {
                    viewModelScope.launch {
                        val transactionType =
                            requireNotNull(savedStateHandle.get<TransactionType>("transactionType"))
                        transactionInteractor.addTransaction(
                            Transaction(
                                type = transactionType,
                                sum = inputSum,
                                category = category
                            )
                        )
                        navigator?.backToMainScreen(transactionType)
                    }
                }
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
