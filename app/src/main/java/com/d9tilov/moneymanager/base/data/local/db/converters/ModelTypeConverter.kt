package com.d9tilov.moneymanager.base.data.local.db.converters

import androidx.room.TypeConverter
import com.d9tilov.moneymanager.period.PeriodType
import com.d9tilov.moneymanager.period.PeriodType.Companion.DAY_NAME
import com.d9tilov.moneymanager.period.PeriodType.Companion.MONTH_NAME
import com.d9tilov.moneymanager.period.PeriodType.Companion.WEEK_NAME
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.TransactionType.Companion.EXPENSE_TRANSACTION_NAME
import com.d9tilov.moneymanager.transaction.TransactionType.Companion.INCOME_TRANSACTION_NAME

object ModelTypeConverter {

    @TypeConverter
    @JvmStatic
    fun fromTransactionType(value: TransactionType): String {
        return value.name
    }

    @TypeConverter
    @JvmStatic
    fun toTransactionType(value: String): TransactionType {
        return when (value) {
            INCOME_TRANSACTION_NAME -> TransactionType.INCOME
            EXPENSE_TRANSACTION_NAME -> TransactionType.EXPENSE
            else -> throw IllegalArgumentException("Wrong transaction type: $value")
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromPeriodType(value: PeriodType): String {
        return value.name
    }

    @TypeConverter
    @JvmStatic
    fun toPeriodType(value: String): PeriodType {
        return when (value) {
            MONTH_NAME -> PeriodType.MONTH
            WEEK_NAME -> PeriodType.WEEK
            DAY_NAME -> PeriodType.DAY
            else -> throw IllegalArgumentException("Wrong period type: $value")
        }
    }
}
