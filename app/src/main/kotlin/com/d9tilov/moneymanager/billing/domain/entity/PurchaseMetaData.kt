package com.d9tilov.moneymanager.billing.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PurchaseMetaData(
    val purchaseTime: Long,
    val autoRenewing: Boolean,
    val acknowledged: Boolean
) : Parcelable
