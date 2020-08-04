package com.d9tilov.moneymanager.transaction.domain.entity

import android.os.Parcelable
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.NO_ID
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.domain.entity.BaseTransaction.Companion.ITEM
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal
import java.util.Date

@Parcelize
data class Transaction(
    val id: Long = DataConstants.DEFAULT_DATA_ID,
    val clientId: String = NO_ID.toString(),
    val type: TransactionType,
    val sum: BigDecimal,
    val category: Category,
    val currency: String? = null,
    val date: Date = Date(),
    val description: String = "",
    val qrCode: String? = null,
    override val headerPosition: Int = 0
) : BaseTransaction, Parcelable {
    override val itemType: Int
        get() = ITEM
}