package com.d9tilov.android.category.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.category.data.model.Category
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.ui.navigation.CategoryDestination
import com.d9tilov.android.category.ui.navigation.CategoryNavigator
import com.d9tilov.android.core.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn

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
                CategoryDestination.EditTransactionScreen -> navigator?.backToEditTransactionScreen(category)
                CategoryDestination.EditRegularTransactionScreen -> navigator?.backToEditRegularTransactionScreen(
                    category
                )
                CategoryDestination.MainWithSumScreen -> navigator?.backToMainScreen(category)
                CategoryDestination.MainScreen,
                CategoryDestination.CategoryCreationScreen,
                CategoryDestination.CategoryScreen,
                CategoryDestination.SubCategoryScreen,
                null -> navigator?.openCreateCategoryScreen(category)
            }
        }
    }

    override fun onCategoryRemoved(category: Category) {
        navigator?.openRemoveDialog(category)
    }
}
