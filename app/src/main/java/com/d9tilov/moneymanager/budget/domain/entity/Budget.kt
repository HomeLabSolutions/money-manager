package com.d9tilov.moneymanager.budget.domain.entity

import android.os.Parcelable
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.transaction.TransactionType
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal
import java.util.Date

@Parcelize
data class Budget(
    val id: Long = DataConstants.DEFAULT_DATA_ID,
    val clientId: String = DataConstants.NO_ID.toString(),
    val currencyCode: String = DataConstants.DEFAULT_CURRENCY_CODE,
    val type: TransactionType,
    val sum: BigDecimal,
    val category: Category,
    val createdDate: Date = Date(),
    val expireDate: Date,
    val description: String,
    val pushEnable: Boolean = true,
    val autoAdded: Boolean = true
) : Parcelable
