package com.d9tilov.moneymanager.budget.domain.entity

import android.os.Parcelable
import com.d9tilov.android.core.constants.DataConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.core.constants.DataConstants.DEFAULT_DATA_ID
import com.d9tilov.android.core.constants.DataConstants.NO_ID
import com.d9tilov.moneymanager.core.util.currentDateTime
import kotlinx.datetime.LocalDateTime
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.math.BigDecimal

@Parcelize
data class BudgetData(
    val id: Long,
    val clientId: String,
    val currencyCode: String,
    val sum: BigDecimal,
    val saveSum: BigDecimal,
    val createdDate: @RawValue LocalDateTime
) : Parcelable {

    companion object {
        val EMPTY = BudgetData(
            id = DEFAULT_DATA_ID,
            clientId = NO_ID.toString(),
            currencyCode = DEFAULT_CURRENCY_CODE,
            sum = BigDecimal.ZERO,
            saveSum = BigDecimal.ZERO,
            createdDate = currentDateTime()
        )
    }
}
