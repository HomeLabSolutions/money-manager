package com.d9tilov.android.category.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.category.domain.model.CategoryGroup
import com.d9tilov.android.category.domain.model.CategoryGroupItem
import com.d9tilov.android.category_ui.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class CategoryIconListUiState(
    val groups: List<CategoryGroupItem> = listOf(
        CategoryGroupItem(
            CategoryGroup.HOUSING,
            R.string.category_group_housing
        ),
        CategoryGroupItem(
            CategoryGroup.TRANSPORT,
            R.string.category_group_transportation
        ),
        CategoryGroupItem(
            CategoryGroup.FOOD,
            R.string.category_group_food
        ),
        CategoryGroupItem(
            CategoryGroup.UTILITIES,
            R.string.category_group_utilities
        ),
        CategoryGroupItem(
            CategoryGroup.INSURANCE,
            R.string.category_group_insurance
        ),
        CategoryGroupItem(
            CategoryGroup.MEDICAL,
            R.string.category_group_medical
        ),
        CategoryGroupItem(
            CategoryGroup.SPORT,
            R.string.category_group_sport
        ),
        CategoryGroupItem(
            CategoryGroup.INVESTING,
            R.string.category_group_investing
        ),
        CategoryGroupItem(
            CategoryGroup.RECREATION,
            R.string.category_group_recreation
        ),
        CategoryGroupItem(
            CategoryGroup.PERSONAL,
            R.string.category_group_personal
        ),
        CategoryGroupItem(
            CategoryGroup.OTHERS,
            R.string.category_group_others
        )
    ),
) {
    companion object {
        val EMPTY = CategoryIconListUiState()
    }
}

@HiltViewModel
class CategoryGroupSetViewModel @Inject constructor() : ViewModel() {

    val uiState: StateFlow<CategoryIconListUiState> = flowOf(CategoryIconListUiState.EMPTY)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = CategoryIconListUiState.EMPTY
        )

    fun openGroupIconSet(item: CategoryGroupItem) {
    }
}
