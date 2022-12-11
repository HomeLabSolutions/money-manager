package com.d9tilov.moneymanager.regular.domain.entity

import com.d9tilov.android.core.constants.DataConstants
import com.d9tilov.android.core.constants.DataConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.core.constants.DataConstants.DEFAULT_DATA_ID
import com.d9tilov.android.core.model.ExecutionPeriod
import com.d9tilov.android.core.utils.currentDate
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionType
import java.math.BigDecimal
import kotlinx.datetime.LocalDateTime

data class RegularTransaction(
    val id: Long,
    val clientId: String,
    val currencyCode: String,
    val type: TransactionType,
    val sum: BigDecimal,
    val category: Category,
    val createdDate: LocalDateTime,
    val executionPeriod: ExecutionPeriod,
    val description: String,
    val pushEnabled: Boolean,
    val autoAdd: Boolean
) {

    companion object {
        val EMPTY = RegularTransaction(
            id = DEFAULT_DATA_ID,
            clientId = DataConstants.NO_ID.toString(),
            currencyCode = DEFAULT_CURRENCY_CODE,
            type = TransactionType.EXPENSE,
            sum = BigDecimal.ZERO,
            category = Category.EMPTY_EXPENSE,
            createdDate = currentDateTime(),
            executionPeriod = ExecutionPeriod.EveryMonth(
                currentDate().dayOfMonth,
                currentDateTime()
            ),
            description = "",
            pushEnabled = true,
            autoAdd = true
        )
    }

    fun isValid(): Boolean = id != DEFAULT_DATA_ID
}
