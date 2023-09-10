package com.d9tilov.android.goals.domain.model

import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.core.constants.DataConstants
import java.math.BigDecimal

data class Goal(
    val id: Long,
    val clientId: String,
    val currencyCode: String,
    val name: String,
    val targetSum: BigDecimal,
    val currentSum: BigDecimal,
    val createdDate: Long,
    val description: String
) {

    companion object {
        val EMPTY = Goal(
            id = DataConstants.DEFAULT_DATA_ID,
            clientId = DataConstants.NO_ID.toString(),
            currencyCode = DEFAULT_CURRENCY_CODE,
            name = "",
            targetSum = BigDecimal.ZERO,
            currentSum = BigDecimal.ZERO,
            createdDate = System.currentTimeMillis(),
            description = ""
        )
    }
}
