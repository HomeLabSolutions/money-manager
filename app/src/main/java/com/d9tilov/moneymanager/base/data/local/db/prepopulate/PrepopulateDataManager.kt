package com.d9tilov.moneymanager.base.data.local.db.prepopulate

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.data.local.db.AppDatabase.Companion.NO_ID
import com.d9tilov.moneymanager.base.data.local.db.prepopulate.entity.PrepopulateCategory
import com.d9tilov.moneymanager.category.CategoryType
import javax.inject.Inject

class PrepopulateDataManager @Inject constructor(private val context: Context) {

    fun createDefaultCategories(): List<PrepopulateCategory> {
        return listOf(
            createCategory(
                1,
                NO_ID,
                CategoryType.EXPENSE,
                R.string.default_category_food,
                R.drawable.ic_category_food,
                R.color.category_deep_purple_a100,
                0,
                0
            ),
            createCategory(
                2,
                1,
                CategoryType.EXPENSE,
                R.string.default_category_cafe,
                R.drawable.ic_catefory_cafe,
                R.color.blue_grey_400,
                1,
                1
            ),
            createCategory(
                3,
                NO_ID,
                CategoryType.EXPENSE,
                R.string.default_category_car,
                R.drawable.ic_category_car,
                R.color.category_light_green_a200,
                2,
                2
            ),
            createCategory(
                4,
                NO_ID,
                CategoryType.EXPENSE,
                R.string.default_category_doctor,
                R.drawable.ic_category_doctor,
                R.color.category_pink_300,
                3,
                3
            ),
            createCategory(
                5,
                NO_ID,
                CategoryType.EXPENSE,
                R.string.default_category_entertainment,
                R.drawable.ic_category_entertainment,
                R.color.category_pink_a200,
                4,
                4
            ),
            createCategory(
                6,
                NO_ID,
                CategoryType.EXPENSE,
                R.string.default_category_home,
                R.drawable.ic_category_home,
                R.color.category_teal_100,
                5,
                5
            ),
            createCategory(
                7,
                NO_ID,
                CategoryType.EXPENSE,
                R.string.default_category_travels,
                R.drawable.ic_category_travels,
                R.color.category_yellow_400,
                6,
                6
            ),
            createCategory(
                8,
                NO_ID,
                CategoryType.EXPENSE,
                R.string.default_category_internet,
                R.drawable.ic_category_internet,
                R.color.category_teal_600,
                7,
                7
            )
        )
    }

    private fun createCategory(
        id: Long,
        parentId: Long,
        type: CategoryType,
        @StringRes name: Int,
        @DrawableRes icon: Int,
        @ColorRes color: Int,
        priority: Int,
        ordinal: Int
    ): PrepopulateCategory {
        return PrepopulateCategory(
            id,
            parentId,
            type,
            context.getString(name),
            icon,
            color,
            priority,
            ordinal
        )
    }
}
