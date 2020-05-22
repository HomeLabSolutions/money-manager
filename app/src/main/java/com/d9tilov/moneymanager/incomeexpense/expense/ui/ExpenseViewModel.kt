package com.d9tilov.moneymanager.incomeexpense.expense.ui

import com.d9tilov.moneymanager.base.ui.BaseViewModel
import com.d9tilov.moneymanager.base.ui.navigator.ExpenseNavigator
import javax.inject.Inject

class ExpenseViewModel @Inject constructor() :
    BaseViewModel<ExpenseNavigator>() {

    var mainSum: StringBuilder = StringBuilder()
        private set

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
}
