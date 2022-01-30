package com.d9tilov.moneymanager.transaction.domain.entity

import android.os.Parcelable
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.NO_ID
import com.d9tilov.moneymanager.core.util.currentDateTime
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.domain.entity.BaseTransaction.Companion.ITEM
import kotlinx.datetime.LocalDateTime
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.math.BigDecimal

@Parcelize
data class Transaction(
    val id: Long = DataConstants.DEFAULT_DATA_ID,
    val clientId: String = NO_ID.toString(),
    val type: TransactionType,
    val category: Category,
    val currencyCode: String = DataConstants.DEFAULT_CURRENCY_CODE,
    val sum: BigDecimal,
    val usdSum: BigDecimal = BigDecimal.ZERO,
    val date: @RawValue LocalDateTime = currentDateTime(),
    val description: String = "",
    val qrCode: String? = null,
    val isRegular: Boolean = false,
    val inStatistics: Boolean = true,
    override val headerPosition: Int = 0
) : BaseTransaction, Parcelable {
    override val itemType: Int
        get() = ITEM

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Transaction) return false

        if (id != other.id) return false
        if (clientId != other.clientId) return false
        if (type != other.type) return false
        if (category.id != other.category.id) return false
        if (currencyCode != other.currencyCode) return false
        if (sum != other.sum) return false
        if (usdSum != other.usdSum) return false
        if (date != other.date) return false
        if (description != other.description) return false
        if (qrCode != other.qrCode) return false
        if (isRegular != other.isRegular) return false
        if (inStatistics != other.inStatistics) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + clientId.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + category.id.hashCode()
        result = 31 * result + currencyCode.hashCode()
        result = 31 * result + sum.hashCode()
        result = 31 * result + usdSum.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + (qrCode?.hashCode() ?: 0)
        result = 31 * result + isRegular.hashCode()
        result = 31 * result + inStatistics.hashCode()
        return result
    }
}
