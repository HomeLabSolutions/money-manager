package com.d9tilov.android.budget.domain.model

import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.core.constants.DataConstants.DEFAULT_DATA_ID
import com.d9tilov.android.core.constants.DataConstants.NO_ID
import com.d9tilov.android.core.model.Decimal
import com.d9tilov.android.core.utils.currentDateTime
import kotlinx.datetime.LocalDateTime

data class BudgetData(
    val id: Long,
    val clientId: String,
    val currencyCode: String,
    val sum: Decimal,
    val saveSum: Decimal,
    val createdDate: LocalDateTime,
) {
    companion object {
        val EMPTY =
            BudgetData(
                id = DEFAULT_DATA_ID,
                clientId = NO_ID.toString(),
                currencyCode = DEFAULT_CURRENCY_CODE,
                sum = Decimal.ZERO,
                saveSum = Decimal.ZERO,
                createdDate = currentDateTime(),
            )
    }
}
