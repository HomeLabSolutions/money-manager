package com.d9tilov.moneymanager.category.subcategory.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.SubCategoryNavigator
import com.d9tilov.moneymanager.category.CategoryDestination
import com.d9tilov.moneymanager.category.CategoryDestination.EDIT_REGULAR_TRANSACTION_SCREEN
import com.d9tilov.moneymanager.category.CategoryDestination.EDIT_TRANSACTION_SCREEN
import com.d9tilov.moneymanager.category.CategoryDestination.MAIN_WITH_SUM_SCREEN
import com.d9tilov.moneymanager.category.common.BaseCategoryViewModel
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SubCategoryViewModel @Inject constructor(
    categoryInteractor: CategoryInteractor,
    private val savedStateHandle: SavedStateHandle
) : BaseCategoryViewModel<SubCategoryNavigator>() {

    private val parentCategory: Category? = savedStateHandle.get<Category>("parent_category")
    override val categories: StateFlow<List<Category>> =
        categoryInteractor.getChildrenByParent(parentCategory!!)
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        if (parentCategory == null || parentCategory.children.isEmpty()) {
            throw IllegalArgumentException("Parent category mustn't have at least one child")
        }
    }

    override fun onCategoryClicked(category: Category) {
        when (savedStateHandle.get<CategoryDestination>("destination")) {
            EDIT_TRANSACTION_SCREEN -> navigator?.backToEditTransactionScreen(category)
            EDIT_REGULAR_TRANSACTION_SCREEN -> navigator?.backToEditRegularTransactionScreen(
                category
            )
            MAIN_WITH_SUM_SCREEN -> navigator?.backToMainScreen(category)
            else -> navigator?.openCreateCategoryScreen(category)
        }
    }

    override fun onCategoryRemoved(category: Category) {
        navigator?.openRemoveDialog(category)
    }
}
