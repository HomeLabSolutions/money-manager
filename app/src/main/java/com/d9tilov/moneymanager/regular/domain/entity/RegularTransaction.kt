package com.d9tilov.moneymanager.regular.domain.entity

import android.os.Parcelable
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_CURRENCY_CODE
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_DATA_ID
import com.d9tilov.moneymanager.core.util.currentDateTime
import com.d9tilov.moneymanager.period.PeriodType
import com.d9tilov.moneymanager.transaction.TransactionType
import kotlinx.datetime.LocalDateTime
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.math.BigDecimal

@Parcelize
data class RegularTransaction(
    val id: Long,
    val clientId: String,
    val currencyCode: String,
    val type: TransactionType,
    val sum: BigDecimal,
    val category: Category,
    val createdDate: @RawValue LocalDateTime,
    val startDate: @RawValue LocalDateTime,
    val periodType: PeriodType,
    val dayOfWeek: Int,
    val description: String,
    val pushEnabled: Boolean,
    val autoAdd: Boolean
) : Parcelable {

    companion object {
        val EMPTY = RegularTransaction(
            id = DEFAULT_DATA_ID,
            clientId = DataConstants.NO_ID.toString(),
            currencyCode = DEFAULT_CURRENCY_CODE,
            type = TransactionType.EXPENSE,
            sum = BigDecimal.ZERO,
            category = Category.EMPTY,
            createdDate = currentDateTime(),
            startDate = currentDateTime(),
            periodType = PeriodType.MONTH,
            dayOfWeek = 0b0000000,
            description = "",
            pushEnabled = true,
            autoAdd = true
        )
    }

    fun isValid(): Boolean = id != DEFAULT_DATA_ID
}
