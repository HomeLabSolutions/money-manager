package com.d9tilov.moneymanager.budget.data.entity

import android.os.Parcelable
import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_CURRENCY_CODE
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.util.Date

@Parcelize
data class BudgetData(
    val id: Long = DataConstants.DEFAULT_DATA_ID,
    val clientId: String = DataConstants.NO_ID.toString(),
    val currencyCode: String = DEFAULT_CURRENCY_CODE,
    val sum: BigDecimal = BigDecimal.ZERO,
    val saveSum: BigDecimal = BigDecimal.ZERO,
    val createdDate: Date = Date()
) : Parcelable
