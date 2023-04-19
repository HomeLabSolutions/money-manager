package com.d9tilov.android.transaction.domain.model

import com.d9tilov.android.category.data.model.Category
import com.d9tilov.android.core.constants.DataConstants
import com.d9tilov.android.core.model.TransactionType
import java.math.BigDecimal

data class TransactionChartModel(
    val clientId: String = DataConstants.NO_ID.toString(),
    val type: TransactionType,
    val category: Category,
    val currencyCode: String,
    val sum: BigDecimal,
    val percent: BigDecimal
)
