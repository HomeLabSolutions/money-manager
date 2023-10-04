package com.d9tilov.android.transaction.domain.model

import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.core.constants.DataConstants
import com.d9tilov.android.core.constants.DataConstants.NO_ID
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.transaction.domain.model.BaseTransaction.Companion.ITEM
import kotlinx.datetime.LocalDateTime
import java.math.BigDecimal

data class Transaction(
    val id: Long,
    val clientId: String,
    val type: TransactionType,
    val category: Category,
    val currencyCode: String,
    val sum: BigDecimal,
    val usdSum: BigDecimal,
    override val date: LocalDateTime,
    val description: String,
    val qrCode: String?,
    val isRegular: Boolean,
    val inStatistics: Boolean,
    val latitude: Double,
    val longitude: Double,
    val photoUri: String?
) : BaseTransaction {

    companion object {
        val EMPTY = Transaction(
            id = DataConstants.DEFAULT_DATA_ID,
            clientId = NO_ID.toString(),
            type = TransactionType.EXPENSE,
            category = Category.EMPTY_EXPENSE,
            currencyCode = DEFAULT_CURRENCY_CODE,
            sum = BigDecimal.ZERO,
            usdSum = BigDecimal.ZERO,
            date = currentDateTime(),
            description = "",
            qrCode = null,
            isRegular = false,
            inStatistics = true,
            latitude = 0.0,
            longitude = 0.0,
            photoUri = null
        )
    }

    override val itemType: Int = ITEM
}
