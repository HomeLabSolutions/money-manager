package com.d9tilov.android.transaction.regular.domain.model

import com.d9tilov.android.core.constants.DataConstants
import com.d9tilov.android.core.model.ExecutionPeriod
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.currentDateTime
import kotlinx.datetime.LocalDateTime
import java.math.BigDecimal

data class RegularTransactionData(
    val id: Long = DataConstants.DEFAULT_DATA_ID,
    val clientId: String = DataConstants.NO_ID.toString(),
    val type: TransactionType,
    val sum: BigDecimal,
    val categoryId: Long,
    val currencyCode: String,
    val createdDate: LocalDateTime = currentDateTime(),
    val executionPeriod: ExecutionPeriod,
    val description: String,
    val pushEnable: Boolean = true,
    val autoAdd: Boolean = false,
)
