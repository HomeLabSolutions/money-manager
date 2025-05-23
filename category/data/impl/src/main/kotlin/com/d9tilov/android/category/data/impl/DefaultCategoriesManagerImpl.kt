package com.d9tilov.android.category.data.impl

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.d9tilov.android.category.data.contract.DefaultCategoriesManager
import com.d9tilov.android.category.domain.entity.DefaultCategory
import com.d9tilov.android.core.constants.DataConstants.NO_ID
import com.d9tilov.android.core.model.TransactionType
import javax.inject.Inject

class DefaultCategoriesManagerImpl
    @Inject
    constructor(
        private val context: Context,
    ) : DefaultCategoriesManager {
        @Suppress("MagicNumber")
        override fun createDefaultExpenseCategories(): List<DefaultCategory> =
            listOf(
                createCategory(
                    1,
                    NO_ID,
                    TransactionType.EXPENSE,
                    R.string.default_category_expense_food,
                    R.drawable.ic_category_food,
                    R.color.category_all_color,
                ),
                createCategory(
                    2,
                    NO_ID,
                    TransactionType.EXPENSE,
                    R.string.default_category_expense_cafe,
                    R.drawable.ic_category_cafe,
                    R.color.category_red,
                ),
                createCategory(
                    3,
                    NO_ID,
                    TransactionType.EXPENSE,
                    R.string.default_category_expense_products,
                    R.drawable.ic_category_grocery,
                    R.color.category_purple,
                ),
                createCategory(
                    4,
                    NO_ID,
                    TransactionType.EXPENSE,
                    R.string.default_category_expense_car,
                    R.drawable.ic_category_car,
                    R.color.category_all_color,
                ),
                createCategory(
                    5,
                    NO_ID,
                    TransactionType.EXPENSE,
                    R.string.default_category_expense_car_fuel,
                    R.drawable.ic_category_fuel,
                    R.color.category_indigo,
                ),
                createCategory(
                    6,
                    NO_ID,
                    TransactionType.EXPENSE,
                    R.string.default_category_expense_car_service,
                    R.drawable.ic_category_car_service,
                    R.color.category_blue,
                ),
                createCategory(
                    7,
                    NO_ID,
                    TransactionType.EXPENSE,
                    R.string.default_category_expense_doctor,
                    R.drawable.ic_category_doctor,
                    R.color.category_teal,
                ),
                createCategory(
                    8,
                    NO_ID,
                    TransactionType.EXPENSE,
                    R.string.default_category_expense_relax,
                    R.drawable.ic_category_relax,
                    R.color.category_green,
                ),
                createCategory(
                    9,
                    NO_ID,
                    TransactionType.EXPENSE,
                    R.string.default_category_expense_home,
                    R.drawable.ic_category_home,
                    R.color.category_lime,
                ),
                createCategory(
                    10,
                    NO_ID,
                    TransactionType.EXPENSE,
                    R.string.default_category_expense_travels,
                    R.drawable.ic_category_travels,
                    R.color.category_orange,
                ),
                createCategory(
                    11,
                    NO_ID,
                    TransactionType.EXPENSE,
                    R.string.default_category_expense_internet,
                    R.drawable.ic_category_internet,
                    R.color.category_brown,
                ),
            )

        @Suppress("MagicNumber")
        override fun createDefaultIncomeCategories(): List<DefaultCategory> =
            listOf(
                createCategory(
                    12,
                    NO_ID,
                    TransactionType.INCOME,
                    R.string.default_category_income_salary,
                    R.drawable.ic_category_salary,
                    R.color.category_blue_grey,
                ),
                createCategory(
                    13,
                    NO_ID,
                    TransactionType.INCOME,
                    R.string.default_category_income_part_time_job,
                    R.drawable.ic_category_part_time_job,
                    R.color.category_grey,
                ),
                createCategory(
                    14,
                    NO_ID,
                    TransactionType.INCOME,
                    R.string.default_category_income_business,
                    R.drawable.ic_category_business,
                    R.color.category_cyan,
                ),
                createCategory(
                    15,
                    NO_ID,
                    TransactionType.INCOME,
                    R.string.default_category_income_sale,
                    R.drawable.ic_category_sale,
                    R.color.category_light_blue,
                ),
            )

        private fun createCategory(
            id: Long,
            parentId: Long,
            type: TransactionType,
            @StringRes name: Int,
            @DrawableRes icon: Int,
            @ColorRes color: Int,
        ): DefaultCategory =
            DefaultCategory(
                id,
                parentId,
                type,
                context.getString(name),
                icon,
                color,
                0,
            )
    }
