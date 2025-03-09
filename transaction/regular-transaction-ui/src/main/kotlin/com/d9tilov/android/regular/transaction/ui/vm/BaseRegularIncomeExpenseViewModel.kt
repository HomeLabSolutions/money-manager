package com.d9tilov.android.regular.transaction.ui.vm

import com.d9tilov.android.common.android.ui.base.BaseViewModel
import com.d9tilov.android.regular.transaction.domain.model.RegularTransaction
import com.d9tilov.android.regular.transaction.ui.navigator.BaseRegularIncomeExpenseNavigator

abstract class BaseRegularIncomeExpenseViewModel<T : BaseRegularIncomeExpenseNavigator> : BaseViewModel<T>() {
    abstract fun onCheckClicked(regularTransaction: RegularTransaction)
}
