package com.d9tilov.moneymanager.billing.data

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.d9tilov.moneymanager.billing.data.local.BillingSource
import com.d9tilov.moneymanager.billing.domain.BillingRepo
import com.d9tilov.moneymanager.billing.domain.entity.BillingSkuDetails
import com.d9tilov.moneymanager.billing.domain.exceptions.BillingFailure
import com.d9tilov.moneymanager.core.util.CurrencyUtils.getSymbolByCode
import com.d9tilov.moneymanager.currency.data.entity.Currency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class BillingDataRepo(
    private val billingSource: BillingSource,
) : BillingRepo {

    override val currentPurchases: Flow<List<Purchase>> = billingSource.purchases
    override val billingConnectionReady: Flow<Boolean> = billingSource.billingConnectionReady

    // ProductDetails for the premium subscription.
    override val premiumProductDetails: Flow<ProductDetails?> =
        billingSource.productWithProductDetails.filter {
            it.containsKey(PREMIUM_SUB)
        }.map { it[PREMIUM_SUB] }

    // Set to true when a purchase is acknowledged.
    override val isNewPurchaseAcknowledged: Flow<Boolean> = billingSource.isNewPurchaseAcknowledged

    override fun startBillingConnection() = billingSource.startBillingConnection()
    override fun terminateBillingConnection() = billingSource.terminateBillingConnection()
    override fun getSkuDetails(): Flow<List<BillingSkuDetails>> {
        return billingSource.productWithProductDetails.map { details: Map<String, ProductDetails> -> details.toList() }
            .map { list: List<Pair<String, ProductDetails>> -> list.first() }
            .map { item ->
                Pair<String, List<ProductDetails.SubscriptionOfferDetails>?>(
                    item.first,
                    item.second.subscriptionOfferDetails
                )
            }
            .map { pair: Pair<String, List<ProductDetails.SubscriptionOfferDetails>?> ->
                val product: String = pair.first
                pair.second?.map { details: ProductDetails.SubscriptionOfferDetails ->
                    val tag =
                        details.offerTags.first() ?: throw BillingFailure.TagNotFoundException(
                            product
                        )
                    val offerToken = details.offerToken
                    val price = details.pricingPhases.pricingPhaseList.first()
                        ?: throw BillingFailure.PriceNotFoundException(product)
                    val formattedPrice =
                        price.priceAmountMicros.toBigDecimal().divide(AMOUNT_DIVIDER.toBigDecimal())
                    val currencyCode = price.priceCurrencyCode
                    val currency = Currency.EMPTY.copy(
                        code = currencyCode,
                        value = formattedPrice,
                        symbol = currencyCode.getSymbolByCode()
                    )
                    BillingSkuDetails(tag, offerToken, currency)
                } ?: throw BillingFailure.SubscriptionNotFoundException(product)
            }
    }

    override fun buySku(
        tag: String,
        productDetails: ProductDetails?,
        currentPurchases: List<Purchase>,
        result: (billingClient: BillingClient, paramBuilder: BillingFlowParams) -> BillingResult
    ) {
        billingSource.buySku(tag, productDetails, currentPurchases, result)
    }

    // Set to true when a returned purchases is an auto-renewing premium subscription.
    override val hasRenewablePremium: Flow<Boolean> = billingSource.purchases.map { purchaseList ->
        purchaseList.any { purchase ->
            purchase.products.contains(PREMIUM_SUB) && purchase.isAutoRenewing
        }
    }

    companion object {
        // List of subscription product offerings
        private const val PREMIUM_SUB = "common_subs"
        private const val AMOUNT_DIVIDER = 1_000_000
    }
}
