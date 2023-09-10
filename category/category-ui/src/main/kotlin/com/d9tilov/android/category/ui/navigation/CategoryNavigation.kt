package com.d9tilov.android.category.ui.navigation

import androidx.annotation.DrawableRes
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.category.domain.model.CategoryGroupItem
import com.d9tilov.android.common.android.ui.base.BaseNavigator

interface CategoryCreationNavigator : BaseNavigator {
    fun save()
    fun showError(error: Throwable)
}

interface CategorySetNavigator : BaseNavigator {
    fun save(@DrawableRes icon: Int)
}

interface SubCategoryNavigator : BaseNavigator {
    fun backToEditTransactionScreen(category: Category)
    fun backToEditRegularTransactionScreen(category: Category)
    fun backToMainScreen(category: Category)
    fun openCreateCategoryScreen(category: Category)
    fun openRemoveDialog(subCategory: Category)
}

interface RemoveCategoryDialogNavigator : BaseNavigator {
    fun closeDialog()
}

interface CategoryUnionDialogNavigator : BaseNavigator {
    fun accept()
    fun showError(error: Throwable)
    fun cancel()
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
    fun openCreateCategoryScreen(category: Category)
    fun openRemoveDialog(category: Category)
    fun backToEditTransactionScreen(category: Category)
    fun backToEditRegularTransactionScreen(category: Category)
    fun backToMainScreen(category: Category)
}

interface CategoryGroupSetNavigator : BaseNavigator {
    fun openCategoryGroup(item: CategoryGroupItem)
}
