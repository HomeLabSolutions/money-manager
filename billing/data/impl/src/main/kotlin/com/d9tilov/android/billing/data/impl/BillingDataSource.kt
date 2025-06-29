package com.d9tilov.android.billing.data.impl

import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.ProductDetailsResponseListener
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.d9tilov.android.billing.data.contract.BillingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject

class BillingDataSource
    @Inject
    constructor(
        context: Context,
    ) : PurchasesUpdatedListener,
        ProductDetailsResponseListener,
        BillingSource {
        // New Subscription ProductDetails
        private val _productWithProductDetails =
            MutableStateFlow<Map<String, ProductDetails>>(emptyMap())
        override val productWithProductDetails: Flow<Map<String, ProductDetails>> =
            _productWithProductDetails.asStateFlow()

        // Current Purchases
        private val _purchases = MutableStateFlow<List<Purchase>>(listOf())
        override val purchases: Flow<List<Purchase>> = _purchases.asStateFlow()

        // Tracks new purchases acknowledgement state.
        // Set to true when a purchase is acknowledged and false when not.
        private val _isNewPurchaseAcknowledged = MutableStateFlow(value = false)
        override val isNewPurchaseAcknowledged: Flow<Boolean> = _isNewPurchaseAcknowledged.asStateFlow()

        private val _billingConnectionReady = MutableStateFlow(false)
        override val billingConnectionReady: Flow<Boolean> = _billingConnectionReady.asStateFlow()

        // Initialize the BillingClient.
        private val billingClient =
            BillingClient
                .newBuilder(context)
                .setListener(this)
                .enablePendingPurchases()
                .build()

        // Establish a connection to Google Play.
        override fun startBillingConnection() {
            billingClient.startConnection(
                object : BillingClientStateListener {
                    override fun onBillingSetupFinished(billingResult: BillingResult) {
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            Timber.tag(TAG).d("Billing response OK")
                            // The BillingClient is ready. You can query purchases and product details here
                            queryPurchases()
                            queryProductDetails()
                            _billingConnectionReady.value = true
                        } else {
                            Timber.tag(TAG).e(billingResult.debugMessage)
                        }
                    }

                    override fun onBillingServiceDisconnected() {
                        Timber.tag(TAG).i("Billing connection disconnected")
                        startBillingConnection()
                    }
                },
            )
        }

        // End Billing connection.
        override fun terminateBillingConnection() {
            Timber.tag(TAG).i("Terminating connection")
            billingClient.endConnection()
        }

        // Query Google Play Billing for existing purchases.
        // New purchases will be provided to PurchasesUpdatedListener.onPurchasesUpdated().
        fun queryPurchases() {
            if (!billingClient.isReady) {
                Timber.tag(TAG).e("queryPurchases: BillingClient is not ready")
            }
            // Query for existing subscription products that have been purchased.
            billingClient.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build(),
            ) { billingResult, purchaseList ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    if (purchaseList.isNotEmpty()) {
                        _purchases.value = purchaseList
                    } else {
                        _purchases.value = emptyList()
                    }
                } else {
                    Timber.tag(TAG).e(billingResult.debugMessage)
                }
            }
        }

        // Query Google Play Billing for products available to sell and present them in the UI
        fun queryProductDetails() {
            val params = QueryProductDetailsParams.newBuilder()
            val productList = mutableListOf<QueryProductDetailsParams.Product>()
            for (product in LIST_OF_PRODUCTS) {
                productList.add(
                    QueryProductDetailsParams.Product
                        .newBuilder()
                        .setProductId(product)
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build(),
                )

                params.setProductList(productList).let { productDetailsParams ->
                    Timber.tag(TAG).i("queryProductDetailsAsync")
                    billingClient.queryProductDetailsAsync(productDetailsParams.build(), this)
                }
            }
        }

        // [ProductDetailsResponseListener] implementation
        // Listen to response back from [queryProductDetails] and emits the results
        // to [_productWithProductDetails].
        override fun onProductDetailsResponse(
            billingResult: BillingResult,
            productDetailsList: MutableList<ProductDetails>,
        ) {
            val responseCode = billingResult.responseCode
            val debugMessage = billingResult.debugMessage
            when (responseCode) {
                BillingClient.BillingResponseCode.OK -> {
                    var newMap = emptyMap<String, ProductDetails>()
                    if (productDetailsList.isEmpty()) {
                        Timber
                            .tag(TAG)
                            .d(
                                "onProductDetailsResponse: Found null or empty ProductDetails. Check to see if the Products you requested are correctly published in the Google Play Console.",
                            )
                    } else {
                        newMap =
                            productDetailsList.associateBy {
                                it.productId
                            }
                    }
                    _productWithProductDetails.value = newMap
                }
                else -> {
                    Timber.tag(TAG).i("onProductDetailsResponse: $responseCode $debugMessage")
                }
            }
        }

        // PurchasesUpdatedListener that helps handle new purchases returned from the API
        override fun onPurchasesUpdated(
            billingResult: BillingResult,
            purchases: List<Purchase>?,
        ) {
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && !purchases.isNullOrEmpty()) {
                // Post new purchase List to _purchases
                _purchases.value = purchases

                // Then, handle the purchases
                for (purchase in purchases) {
                    acknowledgePurchases(purchase)
                }
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle an error caused by a user cancelling the purchase flow.
                Timber.tag(TAG).e("User has cancelled")
            } else {
                // Handle any other error codes.
            }
        }

        // Perform new subscription purchases' acknowledgement client side.
        private fun acknowledgePurchases(purchase: Purchase?) {
            purchase?.let {
                if (!it.isAcknowledged) {
                    val params =
                        AcknowledgePurchaseParams
                            .newBuilder()
                            .setPurchaseToken(it.purchaseToken)
                            .build()

                    billingClient.acknowledgePurchase(
                        params,
                    ) { billingResult ->
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK &&
                            it.purchaseState == Purchase.PurchaseState.PURCHASED
                        ) {
                            _isNewPurchaseAcknowledged.value = true
                        }
                    }
                }
            }
        }

        override fun buySku(
            tag: String,
            productDetails: ProductDetails?,
            currentPurchases: List<Purchase>,
            result: (billingClient: BillingClient, paramBuilder: BillingFlowParams) -> BillingResult,
        ) {
            if (productDetails == null) return
            val offers =
                productDetails.subscriptionOfferDetails?.let {
                    retrieveEligibleOffers(
                        offerDetails = it,
                        tag = tag.lowercase(),
                    )
                }
            val offerToken = offers?.let { leastPricedOfferToken(it) }
            if (currentPurchases.isEmpty()) {
                val billingParams: BillingFlowParams.Builder? =
                    offerToken?.let {
                        billingFlowParamsBuilder(
                            productDetails = productDetails,
                            offerToken = it,
                        )
                    }
                billingParams?.let { params ->
                    if (billingClient.isReady) result(billingClient, params.build())
                }
            }
        }

        /**
         * BillingFlowParams Builder for normal purchases.
         *
         * @param productDetails ProductDetails object returned by the library.
         * @param offerToken the least priced offer's offer id token returned by
         * [leastPricedOfferToken].
         *
         * @return [BillingFlowParams] builder.
         */
        private fun billingFlowParamsBuilder(
            productDetails: ProductDetails,
            offerToken: String,
        ): BillingFlowParams.Builder =
            BillingFlowParams.newBuilder().setProductDetailsParamsList(
                listOf(
                    BillingFlowParams.ProductDetailsParams
                        .newBuilder()
                        .setProductDetails(productDetails)
                        .setOfferToken(offerToken)
                        .build(),
                ),
            )

        /**
         * Calculates the lowest priced offer amongst all eligible offers.
         * In this implementation the lowest price of all offers' pricing phases is returned.
         * It's possible the logic can be implemented differently.
         * For example, the lowest average price in terms of month could be returned instead.
         *
         * @param offerDetails List of of eligible offers and base plans.
         *
         * @return the offer id token of the lowest priced offer.
         */
        private fun leastPricedOfferToken(offerDetails: List<ProductDetails.SubscriptionOfferDetails>): String {
            var offerToken = String()
            var leastPricedOffer: ProductDetails.SubscriptionOfferDetails
            var lowestPrice = Int.MAX_VALUE

            for (offer in offerDetails) {
                for (price in offer.pricingPhases.pricingPhaseList) {
                    if (price.priceAmountMicros < lowestPrice) {
                        lowestPrice = price.priceAmountMicros.toInt()
                        leastPricedOffer = offer
                        offerToken = leastPricedOffer.offerToken
                    }
                }
            }
            return offerToken
        }

        /**
         * Retrieves all eligible base plans and offers using tags from ProductDetails.
         *
         * @param offerDetails offerDetails from a ProductDetails returned by the library.
         * @param tag string representing tags associated with offers and base plans.
         *
         * @return the eligible offers and base plans in a list.
         *
         */
        private fun retrieveEligibleOffers(
            offerDetails: MutableList<ProductDetails.SubscriptionOfferDetails>,
            tag: String,
        ): List<ProductDetails.SubscriptionOfferDetails> {
            val eligibleOffers = emptyList<ProductDetails.SubscriptionOfferDetails>().toMutableList()
            offerDetails.forEach { offerDetail ->
                if (offerDetail.offerTags.contains(tag)) {
                    eligibleOffers.add(offerDetail)
                }
            }

            return eligibleOffers
        }

        companion object {
            private const val TAG = "BillingClient"

            // List of subscription product offerings
            private const val PREMIUM_SUB = "common_subs"

            private val LIST_OF_PRODUCTS = listOf(PREMIUM_SUB)
        }
    }
