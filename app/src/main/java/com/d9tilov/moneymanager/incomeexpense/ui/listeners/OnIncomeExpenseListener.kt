package com.d9tilov.moneymanager.incomeexpense.ui.listeners

interface OnIncomeExpenseListener {
    fun onKeyboardShown(show: Boolean)
    fun onHandleInput(str: String?)
}
