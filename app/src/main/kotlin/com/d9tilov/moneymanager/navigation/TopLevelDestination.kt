package com.d9tilov.moneymanager.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.d9tilov.android.designsystem.MoneyManagerIcons
import com.d9tilov.android.profile_ui.R

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    INCOME_EXPENSE(
        selectedIcon = MoneyManagerIcons.FormatList,
        unselectedIcon = MoneyManagerIcons.FormatList,
        iconTextId = R.string.bottom_menu_income_expense,
        titleTextId = R.string.bottom_menu_income_expense,
    ),
    STATISTICS(
        selectedIcon = MoneyManagerIcons.Chart,
        unselectedIcon = MoneyManagerIcons.Chart,
        iconTextId = R.string.bottom_menu_chart,
        titleTextId = R.string.bottom_menu_chart,
    ),
    PROFILE(
        selectedIcon = MoneyManagerIcons.Profile,
        unselectedIcon = MoneyManagerIcons.Profile,
        iconTextId = R.string.bottom_menu_profile,
        titleTextId = R.string.bottom_menu_profile,
    ),
}
