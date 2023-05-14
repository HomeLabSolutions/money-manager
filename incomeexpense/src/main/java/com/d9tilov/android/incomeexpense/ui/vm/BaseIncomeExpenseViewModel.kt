package com.d9tilov.android.incomeexpense.ui.vm

import androidx.paging.PagingData
import com.d9tilov.android.category.data.model.Category
import com.d9tilov.android.common_android.ui.base.BaseViewModel
import com.d9tilov.android.incomeexpense.navigation.BaseIncomeExpenseNavigator
import com.d9tilov.android.transaction.domain.model.BaseTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.math.BigDecimal

abstract class BaseIncomeExpenseViewModel<T : BaseIncomeExpenseNavigator> : BaseViewModel<T>() {

    abstract val categories: StateFlow<List<Category>>
    abstract val transactions: Flow<PagingData<BaseTransaction>>
    abstract fun saveTransaction(category: Category, sum: BigDecimal, currencyCode: String)

    fun openAllCategories() {
        navigator?.openCategoriesScreen()
    }
}
