package com.d9tilov.moneymanager.transaction.data.entity

import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_DATA_ID
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.NO_ID
import com.d9tilov.moneymanager.core.util.currentDateTime
import com.d9tilov.moneymanager.transaction.TransactionType
import kotlinx.datetime.LocalDateTime
import java.math.BigDecimal

data class TransactionDataModel(
    val id: Long = DEFAULT_DATA_ID,
    val clientId: String = NO_ID.toString(),
    val type: TransactionType,
    val categoryId: Long,
    val currency: String,
    val sum: BigDecimal,
    val usdSum: BigDecimal,
    val date: LocalDateTime = currentDateTime(),
    val description: String = "",
    val qrCode: String? = null,
    val isRegular: Boolean,
    val inStatistics: Boolean
) : TransactionBaseDataModel
