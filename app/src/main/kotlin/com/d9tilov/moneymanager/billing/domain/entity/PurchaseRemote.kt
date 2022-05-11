package com.d9tilov.moneymanager.billing.domain.entity

import com.squareup.moshi.Json

data class PurchaseRemote(
    @field:Json(name = "orderId")
    val orderId: String?,
    @field:Json(name = "packageName")
    val packageName: String?,
    @field:Json(name = "productId")
    val productId: String?,
    @field:Json(name = "purchaseTime")
    val purchaseTime: Long?,
    @field:Json(name = "purchaseState")
    val purchaseState: Int?,
    @field:Json(name = "purchaseToken")
    val purchaseToken: String?,
    @field:Json(name = "quantity")
    val quantity: Int?,
    @field:Json(name = "autoRenewing")
    val autoRenewing: Boolean?,
    @field:Json(name = "acknowledged")
    val acknowledged: Boolean?
)
