package com.d9tilov.moneymanager.currency.domain.entity

import android.os.Parcelable
import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.core.util.currentDateTime
import com.d9tilov.moneymanager.core.util.toMillis
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class DomainCurrency(
    val code: String,
    val symbol: String,
    val value: BigDecimal,
    val isBase: Boolean = false,
    val lastUpdateTime: Long
) : Parcelable {

    companion object {
        val EMPTY = DomainCurrency(
            code = DataConstants.DEFAULT_CURRENCY_CODE,
            symbol = DataConstants.DEFAULT_CURRENCY_SYMBOL,
            value = BigDecimal.ZERO,
            isBase = false,
            lastUpdateTime = currentDateTime().toMillis()
        )
    }
}
