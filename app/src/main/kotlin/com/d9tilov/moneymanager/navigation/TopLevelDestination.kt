package com.d9tilov.moneymanager.navigation

import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.core.designsystem.Icon
import com.d9tilov.moneymanager.core.designsystem.Icon.ImageVectorIcon
import com.d9tilov.moneymanager.core.designsystem.MoneyManagerIcons

enum class TopLevelDestination(
    val selectedIcon: Icon,
    val unselectedIcon: Icon,
    val iconTextId: Int,
    val titleTextId: Int
) {
    INCOME_EXPENSE(
        selectedIcon = ImageVectorIcon(MoneyManagerIcons.FormatList),
        unselectedIcon = ImageVectorIcon(MoneyManagerIcons.FormatList),
        iconTextId = R.string.bottom_menu_income_expense,
        titleTextId = R.string.bottom_menu_income_expense
    ),
    STATISTICS(
        selectedIcon = ImageVectorIcon(MoneyManagerIcons.Chart),
        unselectedIcon = ImageVectorIcon(MoneyManagerIcons.Chart),
        iconTextId = R.string.bottom_menu_chart,
        titleTextId = R.string.bottom_menu_chart
    ),
    PROFILE(
        selectedIcon = ImageVectorIcon(MoneyManagerIcons.Profile),
        unselectedIcon = ImageVectorIcon(MoneyManagerIcons.Profile),
        iconTextId = R.string.bottom_menu_profile,
        titleTextId = R.string.bottom_menu_profile
    )
}
