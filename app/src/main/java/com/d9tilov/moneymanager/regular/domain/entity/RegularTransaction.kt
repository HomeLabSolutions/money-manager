package com.d9tilov.moneymanager.regular.domain.entity

import android.os.Parcelable
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_DATA_ID
import com.d9tilov.moneymanager.period.PeriodType
import com.d9tilov.moneymanager.transaction.TransactionType
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.util.Date

@Parcelize
data class RegularTransaction(
    val id: Long,
    val clientId: String,
    val currencyCode: String,
    val type: TransactionType,
    val sum: BigDecimal,
    val usdSum: BigDecimal,
    val category: Category,
    val createdDate: Date,
    val startDate: Date,
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
            currencyCode = DataConstants.DEFAULT_CURRENCY_CODE,
            type = TransactionType.EXPENSE,
            sum = BigDecimal.ZERO,
            usdSum = BigDecimal.ZERO,
            category = Category.EMPTY,
            createdDate = Date(),
            startDate = Date(),
            periodType = PeriodType.MONTH,
            dayOfWeek = 0b0000000,
            description = "",
            pushEnabled = true,
            autoAdd = true
        )
    }

    fun isValid(): Boolean = id != DEFAULT_DATA_ID
}
