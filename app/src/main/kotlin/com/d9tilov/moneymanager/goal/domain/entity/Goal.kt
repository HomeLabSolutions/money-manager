package com.d9tilov.moneymanager.goal.domain.entity

import android.os.Parcelable
import com.d9tilov.android.core.constants.DataConstants
import com.d9tilov.moneymanager.core.util.currentDateTime
import kotlinx.datetime.LocalDateTime
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.math.BigDecimal

@Parcelize
data class Goal(
    val id: Long,
    val clientId: String,
    val currencyCode: String,
    val name: String,
    val targetSum: BigDecimal,
    val currentSum: BigDecimal,
    val createdDate: @RawValue LocalDateTime,
    val description: String
) : Parcelable {

    companion object {
        val EMPTY = Goal(
            id = DataConstants.DEFAULT_DATA_ID,
            clientId = DataConstants.NO_ID.toString(),
            currencyCode = DataConstants.DEFAULT_CURRENCY_CODE,
            name = "",
            targetSum = BigDecimal.ZERO,
            currentSum = BigDecimal.ZERO,
            createdDate = currentDateTime(),
            description = ""
        )
    }
}
