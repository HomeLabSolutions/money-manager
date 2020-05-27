package com.d9tilov.moneymanager.base.ui.navigator

import com.d9tilov.moneymanager.category.data.entities.Category

interface HomeNavigator : BaseNavigator

interface SplashNavigator : BaseNavigator {
    fun openHomeScreen()
    fun openAuthScreen()
}

interface IncomeNavigator : BaseNavigator

interface ExpenseNavigator : BaseNavigator {
    fun openCategoriesScreen()
}

interface IncomeExpenseNavigator : BaseNavigator

interface SettingsNavigator : BaseNavigator

interface StatisticsNavigator : BaseNavigator

interface CategoryNavigator : BaseNavigator
