package com.d9tilov.moneymanager.transaction.data.entity

import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.NO_ID
import com.d9tilov.moneymanager.transaction.TransactionType
import java.util.Date

data class TransactionDateDataModel(
    val clientId: String = NO_ID.toString(),
    val type: TransactionType,
    val date: Date
) : TransactionBaseDataModel
