package com.d9tilov.android.goals.data.model

import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.core.constants.DataConstants
import java.math.BigDecimal

data class GoalData(
    val id: Long = DataConstants.DEFAULT_DATA_ID,
    val clientId: String = DataConstants.NO_ID.toString(),
    val currencyCode: String = DEFAULT_CURRENCY_CODE,
    val name: String,
    val targetSum: BigDecimal = BigDecimal.ZERO,
    val createdDate: Long = System.currentTimeMillis(),
    val description: String = ""
)
