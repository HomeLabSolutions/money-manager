package com.d9tilov.moneymanager.regular.data.entity

import android.os.Parcelable
import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.core.util.currentDate
import com.d9tilov.moneymanager.core.util.currentDateTime
import com.d9tilov.moneymanager.period.PeriodType
import com.d9tilov.moneymanager.transaction.TransactionType
import kotlinx.datetime.LocalDateTime
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.math.BigDecimal

@Parcelize
data class RegularTransactionData(
    val id: Long = DataConstants.DEFAULT_DATA_ID,
    val clientId: String = DataConstants.NO_ID.toString(),
    val type: TransactionType,
    val sum: BigDecimal,
    val categoryId: Long,
    val currencyCode: String,
    val createdDate: @RawValue LocalDateTime = currentDateTime(),
    val nextExecutionDate: @RawValue LocalDateTime,
    val periodType: PeriodType,
    val dayOfWeek: Int = currentDate().dayOfWeek.ordinal,
    val description: String,
    val pushEnable: Boolean = true,
    val autoAdd: Boolean = false
) : Parcelable
