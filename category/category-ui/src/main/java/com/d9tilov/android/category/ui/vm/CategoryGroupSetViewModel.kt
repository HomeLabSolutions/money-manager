package com.d9tilov.android.category.ui.vm

import com.d9tilov.android.category.domain.model.CategoryGroupItem
import com.d9tilov.android.category.ui.navigation.CategoryGroupSetNavigator
import com.d9tilov.android.common_android.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryGroupSetViewModel @Inject constructor() : BaseViewModel<CategoryGroupSetNavigator>() {

    fun openGroupIconSet(item: CategoryGroupItem) {
        navigator?.openCategoryGroup(item)
    }
}
