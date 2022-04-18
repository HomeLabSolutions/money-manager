package com.d9tilov.moneymanager.transaction.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class TransactionLineChartModel(
    val currencyCode: String,
    val sum: BigDecimal
) : Parcelable
