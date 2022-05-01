package com.d9tilov.moneymanager.base.data.local.db.prepopulate

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.data.local.db.prepopulate.entity.PrepopulateCategory
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.NO_ID
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionType
import javax.inject.Inject

class PrepopulateDataManager @Inject constructor(private val context: Context) {

    fun createDefaultExpenseCategories(): List<PrepopulateCategory> {
        return listOf(
            createCategory(
                1,
                NO_ID,
                TransactionType.EXPENSE,
                R.string.default_category_expense_food,
                R.drawable.ic_category_folder,
                R.color.category_all_color
            ),
            createCategory(
                2,
                1,
                TransactionType.EXPENSE,
                R.string.default_category_expense_cafe,
                R.drawable.ic_category_cafe,
                R.color.category_pink
            ),
            createCategory(
                3,
                1,
                TransactionType.EXPENSE,
                R.string.default_category_expense_products,
                R.drawable.ic_category_grocery,
                R.color.category_light_blue
            ),
            createCategory(
                4,
                NO_ID,
                TransactionType.EXPENSE,
                R.string.default_category_expense_car,
                R.drawable.ic_category_folder,
                R.color.category_all_color
            ),
            createCategory(
                5,
                4,
                TransactionType.EXPENSE,
                R.string.default_category_expense_car_fuel,
                R.drawable.ic_category_fuel,
                R.color.category_dark_orange
            ),
            createCategory(
                6,
                4,
                TransactionType.EXPENSE,
                R.string.default_category_expense_car_service,
                R.drawable.ic_category_car_service,
                R.color.category_grey_blue
            ),
            createCategory(
                7,
                NO_ID,
                TransactionType.EXPENSE,
                R.string.default_category_expense_doctor,
                R.drawable.ic_category_doctor,
                R.color.category_dark_red
            ),
            createCategory(
                8,
                NO_ID,
                TransactionType.EXPENSE,
                R.string.default_category_expense_relax,
                R.drawable.ic_category_relax,
                R.color.category_lollipop
            ),
            createCategory(
                9,
                NO_ID,
                TransactionType.EXPENSE,
                R.string.default_category_expense_home,
                R.drawable.ic_category_home,
                R.color.category_flower_violet
            ),
            createCategory(
                10,
                NO_ID,
                TransactionType.EXPENSE,
                R.string.default_category_expense_travels,
                R.drawable.ic_category_travels,
                R.color.category_grass_green
            ),
            createCategory(
                11,
                NO_ID,
                TransactionType.EXPENSE,
                R.string.default_category_expense_internet,
                R.drawable.ic_category_internet,
                R.color.category_yellow
            )
        )
    }

    fun createDefaultIncomeCategories(): List<PrepopulateCategory> {
        return listOf(
            createCategory(
                12,
                NO_ID,
                TransactionType.INCOME,
                R.string.default_category_income_salary,
                R.drawable.ic_category_salary,
                R.color.category_light_violet
            ),
            createCategory(
                13,
                NO_ID,
                TransactionType.INCOME,
                R.string.default_category_income_part_time_job,
                R.drawable.ic_category_part_time_job,
                R.color.category_purple
            ),
            createCategory(
                14,
                NO_ID,
                TransactionType.INCOME,
                R.string.default_category_income_business,
                R.drawable.ic_category_business,
                R.color.category_grey_blue
            ),
            createCategory(
                15,
                NO_ID,
                TransactionType.INCOME,
                R.string.default_category_income_sale,
                R.drawable.ic_category_sale,
                R.color.category_mud_green
            )
        )
    }

    private fun createCategory(
        id: Long,
        parentId: Long,
        type: TransactionType,
        @StringRes name: Int,
        @DrawableRes icon: Int,
        @ColorRes color: Int
    ): PrepopulateCategory {
        return PrepopulateCategory(
            id,
            parentId,
            type,
            context.getString(name),
            icon,
            color,
            0
        )
    }
}
