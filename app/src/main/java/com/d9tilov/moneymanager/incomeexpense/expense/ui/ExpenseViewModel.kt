package com.d9tilov.moneymanager.incomeexpense.expense.ui

import androidx.lifecycle.MutableLiveData
import com.d9tilov.moneymanager.base.ui.BaseViewModel
import com.d9tilov.moneymanager.base.ui.navigator.ExpenseNavigator
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.domain.ICategoryInteractor
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import javax.inject.Inject

class ExpenseViewModel @Inject constructor(categoryInteractor: ICategoryInteractor) :
    BaseViewModel<ExpenseNavigator>() {

    val categories = MutableLiveData<List<Category>>()
    var mainSum: StringBuilder = StringBuilder()
        private set

    init {
        unsubscribeOnDetach(categoryInteractor.getExpenseCategories()
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe {
                categories.value = it
            })
    }

    fun updateNum(num: String) {
        mainSum = StringBuilder(num)
    }

    fun tapNum(num: String): String {
        mainSum.append(num)
        return mainSum.toString()
    }

    fun removeNum(): String {
        return if (mainSum.isNotEmpty() && mainSum.toString() != "0") {
            mainSum.deleteCharAt(mainSum.length - 1)
            if (mainSum.toString().isEmpty()) {
                "0"
            } else {
                mainSum.toString()
            }
        } else {
            "0"
        }
    }

    fun onCategoryClicked(category: Category) {
        if (category.id == Category.ALL_ITEMS_ID) {
            navigator?.openCategoriesScreen()
        }
    }
}
