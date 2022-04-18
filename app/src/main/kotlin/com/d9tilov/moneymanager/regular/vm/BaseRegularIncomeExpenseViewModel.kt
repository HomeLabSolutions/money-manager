package com.d9tilov.moneymanager.regular.vm

import com.d9tilov.moneymanager.base.ui.navigator.BaseRegularIncomeExpenseNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.regular.domain.entity.RegularTransaction

abstract class BaseRegularIncomeExpenseViewModel<T : BaseRegularIncomeExpenseNavigator> :
    BaseViewModel<T>() {

    abstract fun onCheckClicked(regularTransaction: RegularTransaction)
}
