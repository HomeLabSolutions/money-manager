package com.d9tilov.moneymanager.transaction.domain.entity

import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.transaction.TransactionType
import java.math.BigDecimal
import java.util.Date

data class Transaction(
    val id: Long = DataConstants.DEFAULT_DATA_ID,
    val type: TransactionType,
    val sum: BigDecimal,
    val category: Category,
    val currency: String? = null,
    val date: Date = Date(),
    val description: String = "",
    val qrCode: String? = null
)
