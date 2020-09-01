package com.d9tilov.moneymanager.fixed.data.entity

import android.os.Parcelable
import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.transaction.TransactionType
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal
import java.util.Date

@Parcelize
data class FixedTransactionData(
    val id: Long = DataConstants.DEFAULT_DATA_ID,
    val clientId: String = DataConstants.NO_ID.toString(),
    val currencyCode: String,
    val type: TransactionType,
    val sum: BigDecimal,
    val categoryId: Long,
    val createdDate: Date = Date(),
    val expireDate: Date,
    val description: String,
    val pushEnable: Boolean = true
) : Parcelable
