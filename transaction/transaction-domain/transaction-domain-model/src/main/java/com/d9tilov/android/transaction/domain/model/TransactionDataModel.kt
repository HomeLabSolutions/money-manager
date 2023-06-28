package com.d9tilov.android.transaction.domain.model

import com.d9tilov.android.core.constants.DataConstants.DEFAULT_DATA_ID
import com.d9tilov.android.core.constants.DataConstants.NO_ID
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.currentDateTime
import java.math.BigDecimal
import kotlinx.datetime.LocalDateTime

data class TransactionDataModel(
    val id: Long = DEFAULT_DATA_ID,
    val clientId: String = NO_ID.toString(),
    val type: TransactionType,
    val categoryId: Long,
    val currencyCode: String,
    val sum: BigDecimal,
    val usdSum: BigDecimal,
    val date: LocalDateTime = currentDateTime(),
    val description: String = "",
    val qrCode: String? = null,
    val isRegular: Boolean,
    val inStatistics: Boolean,
    val latitude: Double,
    val longitude: Double,
    val photoUri: String? = null
)
