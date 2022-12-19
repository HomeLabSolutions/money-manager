package com.d9tilov.android.database.converters

import androidx.room.TypeConverter
import com.d9tilov.android.core.model.ExecutionPeriod
import com.d9tilov.android.core.model.PeriodType
import com.d9tilov.android.core.model.PeriodType.Companion.DAY_NAME
import com.d9tilov.android.core.model.PeriodType.Companion.MONTH_NAME
import com.d9tilov.android.core.model.PeriodType.Companion.WEEK_NAME
import com.d9tilov.android.database.model.TransactionType
import com.d9tilov.android.database.model.TransactionType.Companion.EXPENSE_TRANSACTION_NAME
import com.d9tilov.android.database.model.TransactionType.Companion.INCOME_TRANSACTION_NAME

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

    @TypeConverter
    @JvmStatic
    fun fromExecutionPeriod(value: ExecutionPeriod): String {
        return when (value.periodType) {
            PeriodType.DAY -> "${value.periodType.name}:1:${DateConverter.fromOffsetDateTime(value.lastExecutionDateTime)}"
            PeriodType.WEEK ->
                value.periodType.name +
                    ":${(value as ExecutionPeriod.EveryWeek).dayOfWeek}" +
                    ":${DateConverter.fromOffsetDateTime(value.lastExecutionDateTime)}"
            PeriodType.MONTH ->
                value.periodType.name +
                    ":${(value as ExecutionPeriod.EveryMonth).dayOfMonth}" +
                    ":${DateConverter.fromOffsetDateTime(value.lastExecutionDateTime)}"
        }
    }

    @TypeConverter
    @JvmStatic
    fun toExecutionPeriod(value: String): ExecutionPeriod {
        val ar = value.split(":")
        return when (ar[0]) {
            DAY_NAME -> ExecutionPeriod.EveryDay(DateConverter.toOffsetDateTime(ar[2].toLong()))
            WEEK_NAME -> ExecutionPeriod.EveryWeek(
                ar[1].toInt(),
                DateConverter.toOffsetDateTime(ar[2].toLong())
            )
            MONTH_NAME -> ExecutionPeriod.EveryMonth(
                ar[1].toInt(),
                DateConverter.toOffsetDateTime(ar[2].toLong())
            )
            else -> throw IllegalArgumentException("Wrong period name: ${ar[0]}")
        }
    }
}
