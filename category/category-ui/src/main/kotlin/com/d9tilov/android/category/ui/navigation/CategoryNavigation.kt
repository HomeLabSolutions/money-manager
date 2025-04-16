package com.d9tilov.android.category.ui.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.category.domain.model.CategoryDestination
import com.d9tilov.android.category.domain.model.CategoryGroup
import com.d9tilov.android.category.domain.model.toDestination
import com.d9tilov.android.category.domain.model.toGroupId
import com.d9tilov.android.category.ui.CategoryCreationRoute
import com.d9tilov.android.category.ui.CategoryGroupIconListRoute
import com.d9tilov.android.category.ui.CategoryIconGridRoute
import com.d9tilov.android.category.ui.CategoryListRoute
import com.d9tilov.android.category.ui.vm.CategorySharedViewModel
import com.d9tilov.android.common.android.utils.sharedViewModel
import com.d9tilov.android.core.constants.NavigationConstants.TRANSACTION_TYPE_ARG
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.model.toType

const val CATEGORY_ID_ARG = "category_id"
const val CATEGORY_GROUP_ARG = "category_group"
const val CATEGORY_DESTINATION_ARG = "category_destination"
const val CATEGORY_NAVIGATION_ROUTE = "category_route"
const val CATEGORY_CREATION_NAVIGATION_ROUTE = "category_creation_route"
const val CATEGORY_ICON_LIST_NAVIGATION_ROUTE = "category_icon_list_route"
const val CATEGORY_ICON_GRID_NAVIGATION_ROUTE = "category_icon_grid_route"

internal sealed class CategoryArgs {
    data class CategoryListArgs(
        val transactionType: TransactionType,
        val destination: CategoryDestination?,
    ) {
        constructor(savedStateHandle: SavedStateHandle) :
            this(
                checkNotNull(savedStateHandle[TRANSACTION_TYPE_ARG]).toString().toInt().toType(),
                checkNotNull(savedStateHandle[CATEGORY_DESTINATION_ARG]).toString().toInt().toDestination(),
            )
    }

    data class CategoryCreationArgs(
        val categoryId: Long,
        val transactionType: TransactionType,
    ) {
        constructor(savedStateHandle: SavedStateHandle) :
            this(
                checkNotNull(savedStateHandle[CATEGORY_ID_ARG]).toString().toLong(),
                checkNotNull(savedStateHandle[TRANSACTION_TYPE_ARG]).toString().toInt().toType(),
            )
    }

    data class CategoryIconsArgs(
        val groupId: CategoryGroup,
    ) {
        constructor(savedStateHandle: SavedStateHandle) :
            this(
                checkNotNull(savedStateHandle[CATEGORY_GROUP_ARG]).toString().toInt().toGroupId(),
            )
    }
}

fun NavController.navigateToCategoryListScreen(
    transactionType: TransactionType,
    destination: CategoryDestination,
    navOptions: NavOptions? = null,
) {
    this.navigate("$CATEGORY_NAVIGATION_ROUTE/${transactionType.value}/${destination.ordinal}", navOptions)
}

fun NavGraphBuilder.categoryListScreen(
    route: String,
    clickBack: () -> Unit,
    openCategory: (Long, TransactionType) -> Unit,
    onCategoryClickAndBack: (Category) -> Unit,
) {
    composable(
        route = route,
        arguments =
            listOf(
                navArgument(TRANSACTION_TYPE_ARG) { type = NavType.IntType },
                navArgument(CATEGORY_DESTINATION_ARG) { type = NavType.IntType },
            ),
    ) {
        CategoryListRoute(
            openCategory = openCategory,
            clickBack = clickBack,
            onCategoryClickAndBack = onCategoryClickAndBack,
        )
    }
}

fun NavController.navigateToCategoryCreationScreen(
    categoryId: Long,
    transactionType: TransactionType,
    navOptions: NavOptions? = null,
) {
    val route = "$CATEGORY_CREATION_NAVIGATION_ROUTE/$categoryId/${transactionType.value}"
    this.navigate(route, navOptions)
}

fun NavGraphBuilder.categoryCreationScreen(
    route: String,
    navController: NavController,
    clickBack: () -> Unit,
    openCategoryGroupIconList: () -> Unit,
    openCategoryIconGrid: () -> Unit,
) {
    composable(
        route = route,
        arguments =
            listOf(
                navArgument(CATEGORY_ID_ARG) { type = NavType.LongType },
                navArgument(TRANSACTION_TYPE_ARG) { type = NavType.IntType },
            ),
    ) { entry ->
        val sharedViewModel = entry.sharedViewModel<CategorySharedViewModel>(navController)
        CategoryCreationRoute(
            sharedViewModel = sharedViewModel,
            openCategoryGroupIconList = openCategoryGroupIconList,
            openCategoryIconGrid = openCategoryIconGrid,
            clickBack = clickBack,
        )
    }
}

fun NavController.navigateToCategoryIconListScreen(navOptions: NavOptions? = null) {
    this.navigate(CATEGORY_ICON_LIST_NAVIGATION_ROUTE, navOptions)
}

fun NavGraphBuilder.categoryIconListScreen(
    route: String,
    clickBack: () -> Unit,
    onItemClick: (Int) -> Unit,
) {
    composable(route = route) {
        CategoryGroupIconListRoute(
            onItemClick = onItemClick,
            clickBack = clickBack,
        )
    }
}

fun NavController.navigateToCategoryIconGridScreen(
    groupItem: Int,
    navOptions: NavOptions? = null,
) {
    this.navigate("$CATEGORY_ICON_GRID_NAVIGATION_ROUTE/$groupItem", navOptions)
}

fun NavGraphBuilder.categoryIconGridScreen(
    route: String,
    navController: NavController,
    clickBack: () -> Unit,
    onIconClick: (Boolean) -> Unit,
) {
    composable(
        route = route,
        arguments = listOf(navArgument(CATEGORY_GROUP_ARG) { type = NavType.IntType }),
    ) { entry ->
        val sharedViewModel = entry.sharedViewModel<CategorySharedViewModel>(navController)
        CategoryIconGridRoute(
            sharedViewModel = sharedViewModel,
            clickBack = clickBack,
            onIconClick = onIconClick,
        )
    }
}
