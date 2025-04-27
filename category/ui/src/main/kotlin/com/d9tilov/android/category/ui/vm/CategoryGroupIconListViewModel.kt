package com.d9tilov.android.category.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.category.domain.entity.CategoryGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class CategoryIconListUiState(
    val groups: List<CategoryGroup> =
        listOf(
            CategoryGroup.HOUSING,
            CategoryGroup.TRANSPORT,
            CategoryGroup.FOOD,
            CategoryGroup.UTILITIES,
            CategoryGroup.INSURANCE,
            CategoryGroup.MEDICAL,
            CategoryGroup.SPORT,
            CategoryGroup.INVESTING,
            CategoryGroup.RECREATION,
            CategoryGroup.PERSONAL,
            CategoryGroup.OTHERS,
            CategoryGroup.UNKNOWN,
        ),
) {
    companion object {
        val EMPTY = CategoryIconListUiState()
    }
}

@HiltViewModel
class CategoryGroupIconListViewModel
    @Inject
    constructor() : ViewModel() {
        val route = ""

        val uiState: StateFlow<CategoryIconListUiState> =
            flowOf(CategoryIconListUiState.EMPTY)
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(),
                    initialValue = CategoryIconListUiState.EMPTY,
                )
    }
