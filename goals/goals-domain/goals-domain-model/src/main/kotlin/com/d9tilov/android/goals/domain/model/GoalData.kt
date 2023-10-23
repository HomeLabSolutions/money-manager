package com.d9tilov.android.goals.domain.model

import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.core.constants.DataConstants
import com.d9tilov.android.core.utils.currentDateTime
import kotlinx.datetime.LocalDateTime
import java.math.BigDecimal

data class GoalData(
    val id: Long = DataConstants.DEFAULT_DATA_ID,
    val clientId: String = DataConstants.NO_ID.toString(),
    val currencyCode: String = DEFAULT_CURRENCY_CODE,
    val name: String,
    val targetSum: BigDecimal = BigDecimal.ZERO,
    val createdDate: LocalDateTime = currentDateTime(),
    val description: String = ""
)