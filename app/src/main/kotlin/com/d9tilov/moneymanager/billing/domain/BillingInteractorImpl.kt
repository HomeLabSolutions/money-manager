package com.d9tilov.moneymanager.billing.domain

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.billing.domain.entity.BillingSkuDetails
import com.d9tilov.moneymanager.billing.domain.entity.PremiumEmails
import com.d9tilov.moneymanager.billing.domain.entity.PremiumInfo
import com.d9tilov.android.interactor.model.DomainCurrency
import com.d9tilov.android.interactor_impl.mapper.CurrencyDomainMapper
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException

class BillingInteractorImpl(
    private val billingRepo: BillingRepo,
    private val currencyDomainMapper: com.d9tilov.android.interactor_impl.mapper.CurrencyDomainMapper
) : BillingInteractor {

    private val premiumEmailList = MutableStateFlow(listOf<String>())

    init {
        Firebase.remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val config = Firebase.remoteConfig.getValue("premium_list").asString()
                val moshi: Moshi = Moshi.Builder().build()
                val jsonAdapter: JsonAdapter<PremiumEmails> =
                    moshi.adapter(PremiumEmails::class.java)
                try {
                    val premiumConfig = jsonAdapter.fromJson(config)
                    premiumEmailList.value = premiumConfig?.emails ?: emptyList()
                } catch (ex: IOException) {
                    Timber.tag(App.TAG).e("Failed parsing remote config: $ex")
                }
            }
        }
    }

    override val currentPurchases: Flow<List<Purchase>> = billingRepo.currentPurchases
    override val productDetails: Flow<ProductDetails?> = billingRepo.premiumProductDetails
    override val isNewPurchaseAcknowledged: Flow<Boolean> = billingRepo.isNewPurchaseAcknowledged
    override val billingConnectionReady: Flow<Boolean> = billingRepo.billingConnectionReady
    override fun getPremiumInfo(): Flow<PremiumInfo> {
        val canPurchaseFlow = flowOf(true)
//            billingRepo.premiumProductDetails.map { productDetails -> productDetails != null }
        val minPriceFlow = flowOf(com.d9tilov.android.interactor.model.DomainCurrency.EMPTY)
//            getSkuDetails().map { list: List<BillingSkuDetails> ->
//            list.minOfWith({ t1, t2 -> t1.value.compareTo(t2.value) }) { it.price }
//        }.map { item: Currency -> currencyDomainMapper.toDomain(item, false) }
        return combine(
            canPurchaseFlow,
            isPremium(),
            billingRepo.hasRenewablePremium,
            minPriceFlow
        ) { canPurchase, isPremium, hasActiveSku, minPrice ->
            PremiumInfo(canPurchase, isPremium, hasActiveSku, minPrice)
        }
    }

    override fun startBillingConnection() = billingRepo.startBillingConnection()
    override fun terminateBillingConnection() = billingRepo.terminateBillingConnection()

    override fun buySku(
        tag: String,
        productDetails: ProductDetails?,
        currentPurchases: List<Purchase>,
        result: (billingClient: BillingClient, paramBuilder: BillingFlowParams) -> BillingResult
    ) {
        billingRepo.buySku(tag, productDetails, currentPurchases, result)
    }

    override fun isPremium(): Flow<Boolean> {
        val isPremiumEmailExists =
            premiumEmailList.map { it.contains(Firebase.auth.currentUser?.email) }
        return combine(
            isPremiumEmailExists,
            currentPurchases.map { it.isNotEmpty() }
        ) { isPremiumExists, isPremium -> isPremiumExists || isPremium }
    }

    override fun getSkuDetails(): Flow<List<BillingSkuDetails>> = billingRepo.getSkuDetails()

}
