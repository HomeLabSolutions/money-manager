package com.d9tilov.moneymanager.currency.data.entity

import android.os.Parcelable
import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_CURRENCY_CODE
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_CURRENCY_SYMBOL
import com.d9tilov.moneymanager.core.util.currentDateTime
import com.d9tilov.moneymanager.core.util.toMillis
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class Currency(
    val id: Long = DataConstants.DEFAULT_DATA_ID,
    val code: String,
    val symbol: String,
    val value: BigDecimal,
    val lastUpdateTime: Long
) : Parcelable {
    companion object {
        val EMPTY = Currency(
            id = DataConstants.DEFAULT_DATA_ID,
            code = DEFAULT_CURRENCY_CODE,
            symbol = DEFAULT_CURRENCY_SYMBOL,
            value = BigDecimal.ZERO,
            lastUpdateTime = currentDateTime().toMillis()
        )
    }
}
