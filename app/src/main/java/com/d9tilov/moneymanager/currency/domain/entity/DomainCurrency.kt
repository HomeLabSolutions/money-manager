package com.d9tilov.moneymanager.currency.domain.entity

import android.os.Parcelable
import com.d9tilov.moneymanager.core.constants.DataConstants
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class DomainCurrency(
    val id: Long = DataConstants.DEFAULT_DATA_ID,
    val code: String,
    val symbol: String,
    val value: BigDecimal,
    val isBase: Boolean = false,
    val used: Boolean,
    val lastTimeUpdate: Long
) : Parcelable
