package com.d9tilov.moneymanager.category.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.CategoryNavigator
import com.d9tilov.moneymanager.category.domain.entity.CategoryDestination
import com.d9tilov.moneymanager.category.domain.entity.CategoryDestination.CategoryCreationScreen
import com.d9tilov.moneymanager.category.domain.entity.CategoryDestination.CategoryScreen
import com.d9tilov.moneymanager.category.domain.entity.CategoryDestination.EditRegularTransactionScreen
import com.d9tilov.moneymanager.category.domain.entity.CategoryDestination.EditTransactionScreen
import com.d9tilov.moneymanager.category.domain.entity.CategoryDestination.MainScreen
import com.d9tilov.moneymanager.category.domain.entity.CategoryDestination.MainWithSumScreen
import com.d9tilov.moneymanager.category.domain.entity.CategoryDestination.SubCategoryScreen
import com.d9tilov.moneymanager.category.common.BaseCategoryViewModel
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    categoryInteractor: CategoryInteractor,
    private val savedStateHandle: SavedStateHandle
) : BaseCategoryViewModel<CategoryNavigator>() {

    val transactionType: TransactionType =
        savedStateHandle.get<TransactionType>("transactionType") ?: TransactionType.EXPENSE
    override val categories: SharedFlow<List<Category>> =
        categoryInteractor.getAllCategoriesByType(transactionType)
            .flowOn(Dispatchers.IO)
            .distinctUntilChanged()
            .shareIn(viewModelScope, Eagerly, 1)

    override fun onCategoryClicked(category: Category) {
        val destination = savedStateHandle.get<CategoryDestination>("destination")
        if (category.children.isNotEmpty()) navigator?.openSubCategoryScreen(category)
        else {
            when (destination) {
                EditTransactionScreen -> navigator?.backToEditTransactionScreen(category)
                EditRegularTransactionScreen -> navigator?.backToEditRegularTransactionScreen(
                    category
                )
                MainWithSumScreen -> navigator?.backToMainScreen(category)
                MainScreen,
                CategoryCreationScreen,
                CategoryScreen,
                SubCategoryScreen,
                null -> navigator?.openCreateCategoryScreen(category)
            }
        }
    }

    override fun onCategoryRemoved(category: Category) {
        navigator?.openRemoveDialog(category)
    }
}
