package com.d9tilov.android.category.ui.model

import com.d9tilov.android.category.domain.entity.CategoryGroup
import com.d9tilov.android.category.ui.R

val categoryGroupItemMap =
    mapOf(
        CategoryGroup.HOUSING to R.string.category_group_housing,
        CategoryGroup.TRANSPORT to R.string.category_group_transportation,
        CategoryGroup.FOOD to R.string.category_group_food,
        CategoryGroup.UTILITIES to R.string.category_group_utilities,
        CategoryGroup.INSURANCE to R.string.category_group_insurance,
        CategoryGroup.MEDICAL to R.string.category_group_medical,
        CategoryGroup.SPORT to R.string.category_group_sport,
        CategoryGroup.INVESTING to R.string.category_group_investing,
        CategoryGroup.RECREATION to R.string.category_group_recreation,
        CategoryGroup.PERSONAL to R.string.category_group_personal,
        CategoryGroup.OTHERS to R.string.category_group_others,
        CategoryGroup.UNKNOWN to R.string.category_group_free,
    )
