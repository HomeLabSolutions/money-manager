package com.d9tilov.moneymanager.base.data.local.db.prepopulate

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.data.local.db.prepopulate.entity.PrepopulateCategory
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.NO_ID
import com.d9tilov.moneymanager.transaction.TransactionType
import javax.inject.Inject

class PrepopulateDataManager @Inject constructor(private val context: Context) {

    fun createDefaultCategories(): List<PrepopulateCategory> {
        return listOf(
            createCategory(
                1,
                NO_ID,
                TransactionType.EXPENSE,
                R.string.default_category_food,
                R.drawable.ic_category_folder,
                R.color.category_all_color
            ),
            createCategory(
                2,
                1,
                TransactionType.EXPENSE,
                R.string.default_category_cafe,
                R.drawable.ic_category_cafe,
                R.color.picton_blue
            ),
            createCategory(
                3,
                NO_ID,
                TransactionType.EXPENSE,
                R.string.default_category_car,
                R.drawable.ic_category_car,
                R.color.category_light_green_a200
            ),
            createCategory(
                4,
                NO_ID,
                TransactionType.EXPENSE,
                R.string.default_category_doctor,
                R.drawable.ic_category_doctor,
                R.color.category_pink_300
            ),
            createCategory(
                5,
                NO_ID,
                TransactionType.EXPENSE,
                R.string.default_category_entertainment,
                R.drawable.ic_category_entertainment,
                R.color.category_pink_a200
            ),
            createCategory(
                6,
                NO_ID,
                TransactionType.EXPENSE,
                R.string.default_category_home,
                R.drawable.ic_category_home,
                R.color.category_teal_100
            ),
            createCategory(
                7,
                NO_ID,
                TransactionType.EXPENSE,
                R.string.default_category_travels,
                R.drawable.ic_category_travels,
                R.color.category_yellow_400
            ),
            createCategory(
                8,
                NO_ID,
                TransactionType.EXPENSE,
                R.string.default_category_internet,
                R.drawable.ic_category_internet,
                R.color.screamin_green
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
