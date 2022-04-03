package com.d9tilov.moneymanager.category.ui.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import com.d9tilov.moneymanager.base.ui.navigator.CategoryNavigator
import com.d9tilov.moneymanager.category.CategoryDestination
import com.d9tilov.moneymanager.category.CategoryDestination.EDIT_REGULAR_TRANSACTION_SCREEN
import com.d9tilov.moneymanager.category.CategoryDestination.EDIT_TRANSACTION_SCREEN
import com.d9tilov.moneymanager.category.CategoryDestination.MAIN_WITH_SUM_SCREEN
import com.d9tilov.moneymanager.category.common.BaseCategoryViewModel
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.transaction.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    categoryInteractor: CategoryInteractor,
    private val savedStateHandle: SavedStateHandle
) : BaseCategoryViewModel<CategoryNavigator>() {

    val transactionType:  TransactionType = savedStateHandle.get<TransactionType>("transactionType") ?: TransactionType.EXPENSE
    override val categories: LiveData<List<Category>> = categoryInteractor.getAllCategoriesByType(transactionType).asLiveData()

    override fun onCategoryClicked(category: Category) {
        val destination = savedStateHandle.get<CategoryDestination>("destination")
        if (category.children.isNotEmpty()) {
            navigator?.openSubCategoryScreen(category)
        } else {
            when (destination) {
                EDIT_TRANSACTION_SCREEN -> navigator?.backToEditTransactionScreen(category)
                EDIT_REGULAR_TRANSACTION_SCREEN -> navigator?.backToEditRegularTransactionScreen(category)
                MAIN_WITH_SUM_SCREEN -> navigator?.backToMainScreen(category)
                else -> navigator?.openCreateCategoryScreen(category)
            }
        }
    }

    override fun onCategoryRemoved(category: Category) {
        navigator?.openRemoveDialog(category)
    }
}
