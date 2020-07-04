package com.d9tilov.moneymanager.transaction.data.mapper

import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_DATA_ID
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.NO_ID
import com.d9tilov.moneymanager.core.mapper.Mapper
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDateDataModel
import com.d9tilov.moneymanager.transaction.data.local.entity.TransactionDbModel
import java.math.BigDecimal
import javax.inject.Inject

class TransactionDateDataMapper @Inject constructor() :
    Mapper<TransactionDbModel, TransactionDateDataModel> {

    override fun toDataModel(model: TransactionDbModel) = with(model) {
        TransactionDateDataModel(clientId = clientId, type = type, date = date)
    }

    override fun toDbModel(model: TransactionDateDataModel) = with(model) {
        TransactionDbModel(
            id = DEFAULT_DATA_ID,
            clientId = NO_ID.toString(),
            isDate = true,
            type = TransactionType.INCOME,
            sum = BigDecimal.ZERO,
            categoryId = NO_ID,
            currency = null,
            date = date,
            description = "",
            qrCode = null
        )
    }
}
