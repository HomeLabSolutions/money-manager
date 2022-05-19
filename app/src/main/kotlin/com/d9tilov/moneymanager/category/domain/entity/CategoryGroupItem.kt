package com.d9tilov.moneymanager.category.domain.entity

import androidx.annotation.StringRes

data class CategoryGroupItem(
    val groupId: CategoryGroup,
    @StringRes val name: Int
)
