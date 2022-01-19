package com.d9tilov.moneymanager.transaction.domain.entity

import android.os.Parcelable
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.NO_ID
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.domain.entity.BaseTransaction.Companion.ITEM
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.util.Date

@Parcelize
data class Transaction(
    val id: Long = DataConstants.DEFAULT_DATA_ID,
    val clientId: String = NO_ID.toString(),
    val type: TransactionType,
    val category: Category,
    val currencyCode: String = DataConstants.DEFAULT_CURRENCY_CODE,
    val sum: BigDecimal,
    val usdSum: BigDecimal = BigDecimal.ZERO,
    val date: Date = Date(),
    val description: String = "",
    val qrCode: String? = null,
    val isRegular: Boolean = false,
    val inStatistics: Boolean = true,
    override val headerPosition: Int = 0
) : BaseTransaction, Parcelable {
    override val itemType: Int
        get() = ITEM
}
