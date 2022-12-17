package com.d9tilov.moneymanager.goal.domain.entity

import com.d9tilov.android.core.constants.DataConstants
import com.d9tilov.android.common_android.utils.currentDateTime
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import java.math.BigDecimal
import kotlinx.datetime.LocalDateTime

data class Goal(
    val id: Long,
    val clientId: String,
    val currencyCode: String,
    val name: String,
    val targetSum: BigDecimal,
    val currentSum: BigDecimal,
    val createdDate: LocalDateTime,
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
            createdDate = currentDateTime(),
            description = ""
        )
    }
}
