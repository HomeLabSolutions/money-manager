package com.d9tilov.moneymanager.transaction.data.entity

import android.location.Location
import com.d9tilov.android.core.constants.DataConstants.DEFAULT_DATA_ID
import com.d9tilov.android.core.constants.DataConstants.NO_ID
import com.d9tilov.android.common_android.utils.currentDateTime
import com.d9tilov.android.core.model.TransactionType
import kotlinx.datetime.LocalDateTime
import java.math.BigDecimal

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
    val location: Location? = null,
    val photoUri: String? = null
)
