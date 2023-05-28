package com.d9tilov.moneymanager.base.ui.navigator

import com.d9tilov.android.common_android.ui.base.BaseNavigator

interface HomeNavigator : BaseNavigator

interface SplashNavigator : BaseNavigator {
    fun openHomeScreen()
    fun openPrepopulate()
    fun openAuthScreen()
}

interface CurrencyNavigator : BaseNavigator {
    fun skip()
    fun showError(throwable: Throwable)
}
interface CurrencyChangeNavigator : BaseNavigator {
    fun change()
}

interface BudgetAmountNavigator : BaseNavigator

interface GoalsNavigator : BaseNavigator {
    fun save()
}
interface CreatedGoalNavigator : BaseNavigator {
    fun back()
}

interface RemoveGoalNavigator : BaseNavigator {
    fun closeDialog()
}

interface ProfileNavigator : BaseNavigator

interface LogoutDialogNavigator : BaseNavigator {
    fun logout()
}
