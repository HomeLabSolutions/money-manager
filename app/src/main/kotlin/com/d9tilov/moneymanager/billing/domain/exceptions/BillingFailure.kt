package com.d9tilov.moneymanager.billing.domain.exceptions

sealed class BillingFailure(message: String) : Exception(message) {

    data class TagNotFoundException(private val productId: String) : BillingFailure("Billing tag for product: $productId not found")
    data class SubscriptionNotFoundException(private val productId: String) : BillingFailure("Subscriptions for product: $productId not found")
    data class PriceNotFoundException(private val productId: String) : BillingFailure("Price for product: $productId not found")
}
