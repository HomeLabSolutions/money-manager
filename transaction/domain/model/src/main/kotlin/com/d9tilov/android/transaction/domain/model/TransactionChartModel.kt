package com.d9tilov.android.transaction.domain.model

import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.core.constants.DataConstants
import com.d9tilov.android.core.model.Decimal
import com.d9tilov.android.core.model.TransactionType

data class TransactionChartModel(
    val clientId: String = DataConstants.NO_ID.toString(),
    val type: TransactionType,
    val category: Category,
    val currencyCode: String,
    val sum: Decimal,
    val percent: Decimal,
)
