package com.d9tilov.moneymanager.navigation

import com.d9tilov.android.designsystem.Icon
import com.d9tilov.android.designsystem.MoneyManagerIcons
import com.d9tilov.moneymanager.R

enum class TopLevelDestination(
    val selectedIcon: Icon,
    val unselectedIcon: Icon,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    INCOME_EXPENSE(
        selectedIcon = Icon.ImageVectorIcon(MoneyManagerIcons.FormatList),
        unselectedIcon = Icon.ImageVectorIcon(MoneyManagerIcons.FormatList),
        iconTextId = R.string.bottom_menu_income_expense,
        titleTextId = R.string.bottom_menu_income_expense,
    ),
    STATISTICS(
        selectedIcon = Icon.ImageVectorIcon(MoneyManagerIcons.Chart),
        unselectedIcon = Icon.ImageVectorIcon(MoneyManagerIcons.Chart),
        iconTextId = R.string.bottom_menu_chart,
        titleTextId = R.string.bottom_menu_chart,
    ),
    PROFILE(
        selectedIcon = Icon.ImageVectorIcon(MoneyManagerIcons.Profile),
        unselectedIcon = Icon.ImageVectorIcon(MoneyManagerIcons.Profile),
        iconTextId = R.string.bottom_menu_profile,
        titleTextId = R.string.bottom_menu_profile,
    ),
}
