package com.d9tilov.android.category.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.category.ui.navigation.CategoryDestination
import com.d9tilov.android.category.ui.navigation.CategoryNavigator
import com.d9tilov.android.core.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    categoryInteractor: CategoryInteractor
) : BaseCategoryViewModel<CategoryNavigator>() {

    private val transactionType: TransactionType =
        checkNotNull(savedStateHandle["transaction_type"])
    private val destination: CategoryDestination = checkNotNull(savedStateHandle["destination"])
    override val categories: SharedFlow<List<Category>> =
        categoryInteractor.getAllCategoriesByType(transactionType)
            .distinctUntilChanged()
            .shareIn(viewModelScope, Eagerly, 1)

    override fun onCategoryClicked(category: Category) {
        if (category.children.isNotEmpty()) {
            navigator?.openSubCategoryScreen(category)
        } else {
            when (destination) {
                CategoryDestination.EditTransactionScreen -> navigator?.backToEditTransactionScreen(category)
                CategoryDestination.EditRegularTransactionScreen -> navigator?.backToEditRegularTransactionScreen(
                    category
                )

                CategoryDestination.MainWithSumScreen -> navigator?.backToMainScreen(category)
                CategoryDestination.MainScreen,
                CategoryDestination.CategoryCreationScreen,
                CategoryDestination.CategoryScreen,
                CategoryDestination.SubCategoryScreen
                -> navigator?.openCreateCategoryScreen(category)
            }
        }
    }

    override fun onCategoryRemoved(category: Category) {
        navigator?.openRemoveDialog(category)
    }
}
