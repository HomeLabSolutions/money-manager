package com.d9tilov.android.billing.data.impl

import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.d9tilov.android.billing.data.contract.BillingSource
import com.d9tilov.android.billing.domain.contract.BillingRepo
import com.d9tilov.android.billing.domain.model.BillingSkuDetails
import com.d9tilov.android.billing.domain.model.PremiumEmails
import com.d9tilov.android.billing.domain.model.exceptions.BillingFailure
import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.currency.domain.model.Currency
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.remoteconfig.remoteConfig
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import timber.log.Timber

@Inject
class BillingDataRepo(
    private val billingSource: BillingSource,
    private val json: Json,
) : BillingRepo {
    private val currentPurchases: Flow<List<Purchase>> = billingSource.purchases
    override val billingConnectionReady: Flow<Boolean> = billingSource.billingConnectionReady
    private val productDetails = billingSource.productWithProductDetails
    private val premiumEmailList = MutableStateFlow(emptyList<String>())

    // Set to true when a purchase is acknowledged.
    override val isNewPurchaseAcknowledged: Flow<Boolean> = billingSource.isNewPurchaseAcknowledged

    override val isPremiumEmail: Flow<Boolean> =
        premiumEmailList.map { it.contains(Firebase.auth.currentUser?.email) }

    override val hasPremiumPurchase: Flow<Boolean> = currentPurchases.map { it.isNotEmpty() }

    // Set to true when a returned purchases is an auto-renewing premium subscription.
    override val hasRenewablePremium: Flow<Boolean> =
        currentPurchases
            .map { purchaseList ->
                purchaseList.any { purchase ->
                    purchase.products.contains(PREMIUM_SUB) && purchase.isAutoRenewing
                }
            }

    init {
        Firebase.remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val config = Firebase.remoteConfig.getValue("premium_list").asString()
                runCatching { json.decodeFromString<PremiumEmails?>(config) }
                    .onSuccess { premiumEmailList.value = it?.emails.orEmpty() }
                    .onFailure { error -> Timber.e(error, "Unable to parse premium email list") }
            }
        }
    }

    override fun startBillingConnection() = billingSource.startBillingConnection()

    override fun terminateBillingConnection() = billingSource.terminateBillingConnection()

    override fun getSkuDetails(): Flow<List<BillingSkuDetails>> =
        productDetails
            .map { details: Map<String, ProductDetails> ->
                val list = details.toList()
                val item = list.firstOrNull()
                if (item == null) {
                    emptyList()
                } else {
                    val pair = Pair(item.first, item.second.subscriptionOfferDetails)
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
                        val currency =
                            Currency.EMPTY.copy(
                                code = currencyCode,
                                value = formattedPrice,
                                symbol = currencyCode.getSymbolByCode(),
                            )
                        BillingSkuDetails(
                            tag,
                            offerToken,
                            currency,
                        )
                    } ?: emptyList()
                }
            }

    companion object {
        // List of subscription product offerings
        private const val PREMIUM_SUB = "common_subs"
        private const val AMOUNT_DIVIDER = 1_000_000
    }
}
