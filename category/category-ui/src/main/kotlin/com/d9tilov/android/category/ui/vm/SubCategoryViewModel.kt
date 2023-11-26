package com.d9tilov.android.category.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.category.domain.model.CategoryDestination
import com.d9tilov.android.category.ui.navigation.SubCategoryNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubCategoryViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    categoryInteractor: CategoryInteractor
) : BaseCategoryViewModel<SubCategoryNavigator>() {

    private val categoryDestination: CategoryDestination =
        checkNotNull(savedStateHandle["destination"])
    private val parentCategoryId: Long = checkNotNull(savedStateHandle["parent_category_id"])
    private val _parentCategory = MutableStateFlow(Category.EMPTY_INCOME)
    val parentCategory = _parentCategory

    override val categories: SharedFlow<List<Category>> =
        categoryInteractor.getChildrenByParent(parentCategory.value)
            .distinctUntilChanged()
            .shareIn(viewModelScope, SharingStarted.Eagerly, 1)

    init {
        viewModelScope.launch {
            _parentCategory.value = categoryInteractor.getCategoryById(parentCategoryId)
            if (parentCategory.value.children.isEmpty()) {
                throw IllegalArgumentException("Parent category mustn't have at least one child")
            }
        }
    }

    override fun onCategoryClicked(category: Category) {
        when (categoryDestination) {
            CategoryDestination.EDIT_TRANSACTION_SCREEN -> navigator?.backToEditTransactionScreen(
                category
            )

            CategoryDestination.EDIT_REGULAR_TRANSACTION_SCREEN -> navigator?.backToEditRegularTransactionScreen(
                category
            )

            CategoryDestination.MAIN_WITH_SUM_SCREEN -> navigator?.backToMainScreen(category)
            CategoryDestination.MAIN_SCREEN,
            CategoryDestination.CATEGORY_CREATION_SCREEN,
            CategoryDestination.CATEGORY_SCREEN,
            CategoryDestination.SUB_CATEGORY_SCREEN
            -> navigator?.openCreateCategoryScreen(category)
        }
    }

    override fun onCategoryRemoved(category: Category) {
        navigator?.openRemoveDialog(category)
    }
}
