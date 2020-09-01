package com.d9tilov.moneymanager.transaction.data.entity

import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.NO_ID
import com.d9tilov.moneymanager.transaction.TransactionType
import java.util.Date

data class TransactionDateDataModel(
    val id: Long = DataConstants.DEFAULT_DATA_ID,
    val clientId: String = NO_ID.toString(),
    val currency: String,
    val type: TransactionType,
    val date: Date
) : TransactionBaseDataModel
