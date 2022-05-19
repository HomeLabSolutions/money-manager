package com.d9tilov.moneymanager.category.ui.vm

import com.d9tilov.moneymanager.base.ui.navigator.CategoryGroupSetNavigator
import com.d9tilov.moneymanager.category.domain.entity.CategoryGroupItem
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryGroupSetViewModel @Inject constructor() : BaseViewModel<CategoryGroupSetNavigator>() {

    fun openGroupIconSet(item: CategoryGroupItem) {
        navigator?.openCategoryGroup(item)
    }
}
