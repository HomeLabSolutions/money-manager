package com.d9tilov.android.category.ui.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.category.domain.model.CategoryGroup
import com.d9tilov.android.category.domain.model.toGroupId
import com.d9tilov.android.category.ui.CategoryCreationRoute
import com.d9tilov.android.category.ui.CategoryGroupIconListRoute
import com.d9tilov.android.category.ui.CategoryIconGridRoute
import com.d9tilov.android.category.ui.CategoryListRoute
import com.d9tilov.android.category.ui.vm.CategorySharedViewModel
import com.d9tilov.android.common.android.ui.base.BaseNavigator
import com.d9tilov.android.common.android.utils.sharedViewModel
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.model.toType

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

const val categoryIdArg = "category_id"
const val categoryNavigationRoute = "category"
const val categoryCreationNavigationRoute = "category_creation"
const val categoryIconListNavigationRoute = "category_icon_list"
const val categoryIconGridNavigationRoute = "category_icon_grid"
internal const val transactionTypeArg = "transaction_type"
internal const val categoryGroup = "category_group"

internal sealed class CategoryArgs {
    class CategoryListArgs(val transactionType: TransactionType) {
        constructor(savedStateHandle: SavedStateHandle) :
                this((checkNotNull(savedStateHandle[transactionTypeArg]) as Int).toType())
    }

    class CategoryCreationArgs(val categoryId: Long, val transactionType: TransactionType) {
        constructor(savedStateHandle: SavedStateHandle) :
                this(
                    checkNotNull(savedStateHandle[categoryIdArg]) as Long,
                    (checkNotNull(savedStateHandle[transactionTypeArg]) as Int).toType()
                )
    }

    class CategoryIconsArgs(val groupId: CategoryGroup) {
        constructor(savedStateHandle: SavedStateHandle) :
                this(
                    (checkNotNull(savedStateHandle[categoryGroup]) as Int).toGroupId()
                )
    }
}

fun NavController.navigateToCategoryListScreen(
    transactionType: TransactionType,
    navOptions: NavOptions? = null,
) {
    this.navigate("$categoryNavigationRoute/${transactionType.value}", navOptions)
}

fun NavGraphBuilder.categoryListScreen(clickBack: () -> Unit, openCategory: (Long, TransactionType) -> Unit) {
    composable(
        route = "$categoryNavigationRoute/{$transactionTypeArg}",
        arguments = listOf(navArgument(transactionTypeArg) { type = NavType.IntType })
    ) { CategoryListRoute(openCategory = openCategory, clickBack = clickBack) }
}

fun NavController.navigateToCategoryCreationScreen(
    categoryId: Long,
    transactionType: TransactionType,
    navOptions: NavOptions? = null,
) {
    val route = "$categoryCreationNavigationRoute/$categoryId/${transactionType.value}"
    this.navigate(route, navOptions)
}

fun NavGraphBuilder.categoryCreationScreen(
    navController: NavController,
    clickBack: () -> Unit,
    openCategoryGroupIconList: () -> Unit,
    openCategoryIconGrid: () -> Unit,
) {
    val route = "$categoryCreationNavigationRoute/{$categoryIdArg}/{${transactionTypeArg}}"
    composable(
        route = route,
        arguments = listOf(
            navArgument(categoryIdArg) { type = NavType.LongType },
            navArgument(transactionTypeArg) { type = NavType.IntType },
        )
    ) { entry ->
        val sharedViewModel = entry.sharedViewModel<CategorySharedViewModel>(navController)
        CategoryCreationRoute(
            sharedViewModel = sharedViewModel,
            openCategoryGroupIconList = openCategoryGroupIconList,
            openCategoryIconGrid = openCategoryIconGrid,
            clickBack = clickBack
        )
    }
}

fun NavController.navigateToCategoryIconListScreen(navOptions: NavOptions? = null) {
    this.navigate(categoryIconListNavigationRoute, navOptions)
}

fun NavGraphBuilder.categoryIconListScreen(
    clickBack: () -> Unit,
    onItemClick: (Int) -> Unit,
) {
    composable(route = categoryIconListNavigationRoute) {
        CategoryGroupIconListRoute(
            onItemClick = onItemClick,
            clickBack = clickBack
        )
    }
}

fun NavController.navigateToCategoryIconGridScreen(
    groupItem: Int,
    navOptions: NavOptions? = null,
) {
    this.navigate("$categoryIconGridNavigationRoute/$groupItem", navOptions)
}

fun NavGraphBuilder.categoryIconGridScreen(
    navController: NavController,
    clickBack: () -> Unit,
    onIconClick: () -> Unit,
) {
    composable(
        route = "$categoryIconGridNavigationRoute/{$categoryGroup}",
        arguments = listOf(navArgument(categoryGroup) { type = NavType.IntType })
    ) { entry ->
        val sharedViewModel = entry.sharedViewModel<CategorySharedViewModel>(navController)
        CategoryIconGridRoute(
            sharedViewModel = sharedViewModel,
            clickBack = clickBack,
            onIconClick = onIconClick,
        )
    }
}
