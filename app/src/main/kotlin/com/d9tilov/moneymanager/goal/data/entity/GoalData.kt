package com.d9tilov.moneymanager.goal.data.entity

import com.d9tilov.android.core.constants.DataConstants
import com.d9tilov.android.core.utils.currentDateTime
import java.math.BigDecimal
import kotlinx.datetime.LocalDateTime

data class GoalData(
    val id: Long = DataConstants.DEFAULT_DATA_ID,
    val clientId: String = DataConstants.NO_ID.toString(),
    val currencyCode: String = DataConstants.DEFAULT_CURRENCY_CODE,
    val name: String,
    val targetSum: BigDecimal = BigDecimal.ZERO,
    val createdDate: LocalDateTime = currentDateTime(),
    val description: String = ""
)
