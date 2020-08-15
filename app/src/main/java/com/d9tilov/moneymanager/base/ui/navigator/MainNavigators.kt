package com.d9tilov.moneymanager.base.ui.navigator

import androidx.annotation.DrawableRes
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.core.ui.BaseNavigator
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction

interface HomeNavigator : BaseNavigator

interface SplashNavigator : BaseNavigator {
    fun openHomeScreen()
    fun openAuthScreen()
}

interface IncomeNavigator : BaseNavigator

interface ExpenseNavigator : BaseNavigator {
    fun openCategoriesScreen()
    fun openRemoveConfirmationDialog(transaction: Transaction)
}

interface IncomeExpenseNavigator : BaseNavigator

interface SettingsNavigator : BaseNavigator

interface StatisticsNavigator : BaseNavigator

interface RemoveTransactionDialogNavigator : BaseNavigator {
    fun remove()
}

interface CategoryUnionDialogNavigator : BaseNavigator {
    fun accept()
    fun showError(error: Throwable)
    fun cancel()
}

interface RemoveCategoryDialogNavigator : BaseNavigator {
    fun closeDialog()
}

interface EditCategoryDialogNavigator : BaseNavigator {
    fun showError(error: Throwable)
    fun save()
    fun closeDialog()
}

interface RemoveSubCategoryDialogNavigator : BaseNavigator {
    fun closeDialog()
    fun closeDialogAndGoToCategory()
}

interface CategoryNavigator : BaseNavigator {
    fun openSubCategoryScreen(category: Category)
    fun openCreateCategoryScreen(category: Category? = null)
    fun openRemoveDialog(category: Category)
    fun backToEditTransactionScreen(category: Category)
    fun backToMainScreen(transactionType: TransactionType)
}

interface CategoryCreationNavigator : BaseNavigator {
    fun save()
}

interface CategorySetNavigator : BaseNavigator {
    fun save(@DrawableRes icon: Int)
}

interface EditTransactionNavigator : BaseNavigator {
    fun save()
}

interface SubCategoryNavigator : BaseNavigator {
    fun backToEditTransactionScreen(category: Category)
    fun backToMainScreen(transactionType: TransactionType)
    fun openCreateCategoryScreen(category: Category? = null)
    fun openRemoveDialog(subCategory: Category)
}
