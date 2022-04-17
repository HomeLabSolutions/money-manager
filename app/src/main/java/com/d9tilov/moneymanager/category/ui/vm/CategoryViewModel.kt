package com.d9tilov.moneymanager.category.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.CategoryNavigator
import com.d9tilov.moneymanager.category.CategoryDestination
import com.d9tilov.moneymanager.category.CategoryDestination.EDIT_REGULAR_TRANSACTION_SCREEN
import com.d9tilov.moneymanager.category.CategoryDestination.EDIT_TRANSACTION_SCREEN
import com.d9tilov.moneymanager.category.CategoryDestination.MAIN_WITH_SUM_SCREEN
import com.d9tilov.moneymanager.category.CategoryDestination.MAIN_SCREEN
import com.d9tilov.moneymanager.category.CategoryDestination.CATEGORY_CREATION_SCREEN
import com.d9tilov.moneymanager.category.CategoryDestination.CATEGORY_SCREEN
import com.d9tilov.moneymanager.category.CategoryDestination.SUB_CATEGORY_SCREEN
import com.d9tilov.moneymanager.category.common.BaseCategoryViewModel
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.transaction.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    categoryInteractor: CategoryInteractor,
    private val savedStateHandle: SavedStateHandle
) : BaseCategoryViewModel<CategoryNavigator>() {

    val transactionType: TransactionType =
        savedStateHandle.get<TransactionType>("transactionType") ?: TransactionType.EXPENSE
    override val categories: StateFlow<List<Category>> =
        categoryInteractor.getAllCategoriesByType(transactionType)
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    override fun onCategoryClicked(category: Category) {
        val destination = savedStateHandle.get<CategoryDestination>("destination")
        if (category.children.isNotEmpty()) navigator?.openSubCategoryScreen(category)
        else {
            when (destination) {
                EDIT_TRANSACTION_SCREEN -> navigator?.backToEditTransactionScreen(category)
                EDIT_REGULAR_TRANSACTION_SCREEN -> navigator?.backToEditRegularTransactionScreen(
                    category
                )
                MAIN_WITH_SUM_SCREEN -> navigator?.backToMainScreen(category)
                MAIN_SCREEN,
                CATEGORY_CREATION_SCREEN,
                CATEGORY_SCREEN,
                SUB_CATEGORY_SCREEN,
                null -> navigator?.openCreateCategoryScreen(category)
            }
        }
    }

    override fun onCategoryRemoved(category: Category) {
        navigator?.openRemoveDialog(category)
    }
}
