package com.d9tilov.moneymanager.transaction.domain.entity

import com.d9tilov.android.core.constants.DataConstants
import com.d9tilov.android.database.model.TransactionType
import com.d9tilov.moneymanager.category.data.entity.Category
import java.math.BigDecimal

data class TransactionChartModel(
    val clientId: String = DataConstants.NO_ID.toString(),
    val type: TransactionType,
    val category: Category,
    val currencyCode: String,
    val sum: BigDecimal,
    val percent: BigDecimal
)
