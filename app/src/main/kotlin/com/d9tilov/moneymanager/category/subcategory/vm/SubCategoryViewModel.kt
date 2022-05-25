package com.d9tilov.moneymanager.category.subcategory.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.SubCategoryNavigator
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class SubCategoryViewModel @Inject constructor(
    categoryInteractor: CategoryInteractor,
    private val savedStateHandle: SavedStateHandle
) : BaseCategoryViewModel<SubCategoryNavigator>() {

    private val parentCategory: Category? = savedStateHandle.get<Category>("parent_category")
    override val categories: SharedFlow<List<Category>> =
        categoryInteractor.getChildrenByParent(parentCategory!!)
            .flowOn(Dispatchers.IO)
            .distinctUntilChanged()
            .shareIn(viewModelScope, SharingStarted.Eagerly, 1)

    init {
        if (parentCategory == null || parentCategory.children.isEmpty()) {
            throw IllegalArgumentException("Parent category mustn't have at least one child")
        }
    }

    override fun onCategoryClicked(category: Category) {
        when (savedStateHandle.get<CategoryDestination>("destination")) {
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

    override fun onCategoryRemoved(category: Category) {
        navigator?.openRemoveDialog(category)
    }
}
