package com.d9tilov.moneymanager.regular.data.entity

import android.os.Parcelable
import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.core.util.getDayOfWeek
import com.d9tilov.moneymanager.period.PeriodType
import com.d9tilov.moneymanager.transaction.TransactionType
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.util.Date

@Parcelize
data class RegularTransactionData(
    val id: Long = DataConstants.DEFAULT_DATA_ID,
    val clientId: String = DataConstants.NO_ID.toString(),
    val currencyCode: String,
    val type: TransactionType,
    val sum: BigDecimal,
    val categoryId: Long,
    val createdDate: Date = Date(),
    val startDate: Date,
    val periodType: PeriodType,
    val dayOfWeek: Int = Date().getDayOfWeek(),
    val description: String,
    val pushEnable: Boolean = true
) : Parcelable
