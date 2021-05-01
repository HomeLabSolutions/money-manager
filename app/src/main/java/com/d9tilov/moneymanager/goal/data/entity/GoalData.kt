package com.d9tilov.moneymanager.goal.data.entity

import android.os.Parcelable
import com.d9tilov.moneymanager.core.constants.DataConstants
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal
import java.util.Date

@Parcelize
data class GoalData(
    val id: Long = DataConstants.DEFAULT_DATA_ID,
    val clientId: String = DataConstants.NO_ID.toString(),
    val currencyCode: String = DataConstants.DEFAULT_CURRENCY_CODE,
    val name: String,
    val targetSum: BigDecimal = BigDecimal.ZERO,
    val createdDate: Date = Date(),
    val description: String = ""
) : Parcelable
