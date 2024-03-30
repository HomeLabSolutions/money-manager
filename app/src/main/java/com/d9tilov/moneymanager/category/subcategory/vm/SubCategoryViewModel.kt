package com.d9tilov.moneymanager.category.subcategory.vm

import androidx.lifecycle.MutableLiveData
import com.d9tilov.moneymanager.base.ui.BaseViewModel
import com.d9tilov.moneymanager.base.ui.navigator.SubCategoryNavigator
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.domain.ICategoryInteractor
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import javax.inject.Inject

class SubCategoryViewModel @Inject constructor(
    private val categoryInteractor: ICategoryInteractor
) : BaseViewModel<SubCategoryNavigator>() {

    val categories = MutableLiveData<List<Category>>()

    // init {
    //     if (parentCategory == null) {
    //         throw IllegalArgumentException("Parent category mustn't be null")
    //     }
    //     unsubscribeOnDetach(categoryInteractor.getChildrenByParent(parentCategory)
    //         .subscribeOn(ioScheduler)
    //         .observeOn(uiScheduler)
    //         .subscribe {
    //             categories.value = it
    //         })
    // }
}
