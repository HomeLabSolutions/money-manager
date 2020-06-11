package com.d9tilov.moneymanager.base.ui.navigator

import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.core.ui.BaseNavigator

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

interface CategoryNavigator : BaseNavigator {
    fun openSubCategoryScreen(category: Category)
    fun openCreateCategoryDialog()
}

interface SubCategoryNavigator : BaseNavigator {
    fun openCreateCategoryDialog()
}
