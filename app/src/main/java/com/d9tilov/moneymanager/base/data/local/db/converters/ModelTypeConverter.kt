package com.d9tilov.moneymanager.base.data.local.db.converters

import androidx.room.TypeConverter
import com.d9tilov.moneymanager.category.CategoryType
import com.d9tilov.moneymanager.category.CategoryType.Companion.EXPENSE_CATEGORY_NAME
import com.d9tilov.moneymanager.category.CategoryType.Companion.INCOME_CATEGORY_NAME

object ModelTypeConverter {

    @TypeConverter
    @JvmStatic
    fun fromTransactionType(value: CategoryType): String {
        return value.name
    }

    @TypeConverter
    @JvmStatic
    fun toTransactionType(value: String): CategoryType {
        return when (value) {
            INCOME_CATEGORY_NAME -> CategoryType.INCOME
            EXPENSE_CATEGORY_NAME -> CategoryType.EXPENSE
            else -> throw IllegalArgumentException("Wrong category type: $value")
        }
    }
}
