package com.d9tilov.android.billing.data.impl

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.d9tilov.android.billing.data.contract.BillingSource
import com.d9tilov.android.billing.domain.contract.BillingRepo
import com.d9tilov.android.billing.domain.model.BillingSkuDetails
import com.d9tilov.android.billing.domain.model.exceptions.BillingFailure
import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.currency.domain.model.Currency
import com.d9tilov.android.network.dispatchers.Dispatcher
import com.d9tilov.android.network.dispatchers.MoneyManagerDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BillingDataRepo @Inject constructor(
    @Dispatcher(MoneyManagerDispatchers.IO) private val dispatcher: CoroutineDispatcher,
    private val billingSource: BillingSource
) : BillingRepo {

    override val currentPurchases: Flow<List<Purchase>> = billingSource.purchases
    override val billingConnectionReady: Flow<Boolean> = billingSource.billingConnectionReady


    // ProductDetails for the premium subscription.
    override val premiumProductDetails: Flow<ProductDetails?> =
        billingSource.productWithProductDetails.filter {
            it.containsKey(PREMIUM_SUB)
        }.map { it[PREMIUM_SUB] }
            .flowOn(dispatcher)

    // Set to true when a purchase is acknowledged.
    override val isNewPurchaseAcknowledged: Flow<Boolean> = billingSource.isNewPurchaseAcknowledged

    override fun startBillingConnection() = billingSource.startBillingConnection()
    override fun terminateBillingConnection() = billingSource.terminateBillingConnection()
    override fun getSkuDetails(): Flow<List<BillingSkuDetails>> {
        return billingSource.productWithProductDetails.map { details: Map<String, ProductDetails> -> details.toList() }
            .filter { it.isNotEmpty() }
            .map { list: List<Pair<String, ProductDetails>> -> list.firstOrNull() }
            .filterNotNull()
            .map { item ->
                Pair<String, List<ProductDetails.SubscriptionOfferDetails>?>(
                    item.first,
                    item.second.subscriptionOfferDetails
                )
            }
            .map { pair: Pair<String, List<ProductDetails.SubscriptionOfferDetails>?> ->
                val product: String = pair.first
                pair.second?.map { details: ProductDetails.SubscriptionOfferDetails ->
                    val offerTags = details.offerTags
                    if (offerTags.isEmpty()) throw BillingFailure.TagNotFoundException(product)
                    val tag = checkNotNull(offerTags.firstOrNull())
                    val offerToken = details.offerToken
                    val pricingList = details.pricingPhases.pricingPhaseList
                    if (pricingList.isEmpty()) throw BillingFailure.PriceNotFoundException(product)
                    val price = checkNotNull(pricingList.firstOrNull())
                    val formattedPrice =
                        price.priceAmountMicros.toBigDecimal().divide(AMOUNT_DIVIDER.toBigDecimal())
                    val currencyCode = price.priceCurrencyCode
                    val currency = Currency.EMPTY.copy(
                        code = currencyCode,
                        value = formattedPrice,
                        symbol = currencyCode.getSymbolByCode()
                    )
                    BillingSkuDetails(
                        tag,
                        offerToken,
                        currency
                    )
                } ?: throw BillingFailure.SubscriptionNotFoundException(product)
            }
            .flowOn(dispatcher)
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
        .flowOn(dispatcher)

    companion object {
        // List of subscription product offerings
        private const val PREMIUM_SUB = "common_subs"
        private const val AMOUNT_DIVIDER = 1_000_000
    }
}
