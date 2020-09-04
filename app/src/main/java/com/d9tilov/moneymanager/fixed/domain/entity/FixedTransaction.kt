package com.d9tilov.moneymanager.fixed.domain.entity

import android.os.Parcelable
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.core.util.getDayOfWeek
import com.d9tilov.moneymanager.period.PeriodType
import com.d9tilov.moneymanager.transaction.TransactionType
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal
import java.util.Date

@Parcelize
data class FixedTransaction(
    val id: Long = DataConstants.DEFAULT_DATA_ID,
    val clientId: String = DataConstants.NO_ID.toString(),
    val currencyCode: String = DataConstants.DEFAULT_CURRENCY_CODE,
    val type: TransactionType,
    val sum: BigDecimal = BigDecimal.ZERO,
    val category: Category,
    val createdDate: Date = Date(),
    val startDate: Date = Date(),
    val periodType: PeriodType = PeriodType.MONTH,
    val dayOfWeek: Int = Date().getDayOfWeek(),
    val description: String,
    val pushEnable: Boolean = true
) : Parcelable
