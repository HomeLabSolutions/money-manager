package com.d9tilov.moneymanager.category.ui.vm

import androidx.annotation.DrawableRes
import com.d9tilov.moneymanager.base.ui.navigator.CategorySetNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategorySetViewModel @Inject constructor() :
    BaseViewModel<CategorySetNavigator>() {

    fun save(@DrawableRes categoryIcon: Int) {
        navigator?.save(categoryIcon)
    }
}
