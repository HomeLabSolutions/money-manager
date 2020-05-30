package com.d9tilov.moneymanager.category.ui.vm

import androidx.lifecycle.MutableLiveData
import com.d9tilov.moneymanager.base.ui.BaseViewModel
import com.d9tilov.moneymanager.base.ui.navigator.CategoryNavigator
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.domain.ICategoryInteractor
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler

class CategoryViewModel(private val categoryInteractor: ICategoryInteractor) :
    BaseViewModel<CategoryNavigator>() {

    val categories = MutableLiveData<List<Category>>()

    init {
        unsubscribeOnDetach(categoryInteractor.getAllCategories()
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe {
                categories.value = it
            })
    }

    fun onCategoryClicked(category: Category) {
        if (category.children.isNotEmpty()) {
            navigator?.openSubCategoryScreen(category)
        }
    }

    fun onCategoryLongClicked() {}
}
