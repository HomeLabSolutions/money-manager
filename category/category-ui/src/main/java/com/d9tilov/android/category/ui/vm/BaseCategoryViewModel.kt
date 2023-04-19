package com.d9tilov.android.category.ui.vm

import com.d9tilov.android.category.data.model.Category
import com.d9tilov.android.common_android.ui.base.BaseNavigator
import com.d9tilov.android.common_android.ui.base.BaseViewModel
import kotlinx.coroutines.flow.SharedFlow

abstract class BaseCategoryViewModel<T : BaseNavigator> : BaseViewModel<T>() {

    abstract val categories: SharedFlow<List<Category>>

    abstract fun onCategoryClicked(category: Category)
    abstract fun onCategoryRemoved(category: Category)
}
