package com.d9tilov.moneymanager.goal.data.entity

import android.os.Parcelable
import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.core.util.currentDateTime
import kotlinx.datetime.LocalDateTime
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.math.BigDecimal

@Parcelize
data class GoalData(
    val id: Long = DataConstants.DEFAULT_DATA_ID,
    val clientId: String = DataConstants.NO_ID.toString(),
    val currencyCode: String = DataConstants.DEFAULT_CURRENCY_CODE,
    val name: String,
    val targetSum: BigDecimal = BigDecimal.ZERO,
    val createdDate: @RawValue LocalDateTime = currentDateTime(),
    val description: String = ""
) : Parcelable
