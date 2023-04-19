package com.d9tilov.android.regular.transaction.ui.navigator

import com.d9tilov.android.common_android.ui.base.BaseNavigator

interface BaseRegularIncomeExpenseNavigator : BaseNavigator

interface RegularExpenseNavigator : BaseRegularIncomeExpenseNavigator
interface RegularIncomeNavigator : BaseRegularIncomeExpenseNavigator

interface RegularTransactionCreatedNavigator : BaseNavigator {
    fun back()
}

interface DayOfMonthDialogNavigator : BaseNavigator

