package com.d9tilov.moneymanager.transaction.data.entity

import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.transaction.TransactionType
import java.util.Date

data class TransactionDateDataModel(
    val clientId: String = DataConstants.NO_ID.toString(),
    val type: TransactionType,
    val date: Date
) : TransactionBaseDataModel
