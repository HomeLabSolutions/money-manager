package com.d9tilov.android.category.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.category.ui.navigation.CategoryDestination
import com.d9tilov.android.category.ui.navigation.SubCategoryNavigator
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

    override fun onCategoryRemoved(category: Category) {
        navigator?.openRemoveDialog(category)
    }
}
