package com.d9tilov.moneymanager.transaction.data.mapper

import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_DATA_ID
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.NO_ID
import com.d9tilov.moneymanager.core.mapper.Mapper
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDateDataModel
import com.d9tilov.moneymanager.transaction.data.local.entity.TransactionDbModel
import java.math.BigDecimal
import javax.inject.Inject

class TransactionDateDataMapper @Inject constructor() :
    Mapper<TransactionDbModel, TransactionDateDataModel> {

    override fun toDataModel(model: TransactionDbModel) = with(model) {
        TransactionDateDataModel(id = id, clientId = clientId, type = type, date = date, currency = currency)
    }

    override fun toDbModel(model: TransactionDateDataModel) = with(model) {
        TransactionDbModel(
            id = DEFAULT_DATA_ID,
            clientId = clientId,
            isDate = true,
            type = type,
            sum = BigDecimal.ZERO,
            categoryId = NO_ID,
            currency = currency,
            date = date,
            description = "",
            qrCode = null
        )
    }
}
