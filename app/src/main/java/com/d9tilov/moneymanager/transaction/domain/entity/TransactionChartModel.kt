package com.d9tilov.moneymanager.transaction.domain.entity

import android.os.Parcelable
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.transaction.TransactionType
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class TransactionChartModel(
    val clientId: String = DataConstants.NO_ID.toString(),
    val type: TransactionType,
    val category: Category,
    val currencyCode: String = DataConstants.DEFAULT_CURRENCY_CODE,
    val sum: BigDecimal,
    val usdSum: BigDecimal = BigDecimal.ZERO,
    val inStatistics: Boolean = true
) : Parcelable
