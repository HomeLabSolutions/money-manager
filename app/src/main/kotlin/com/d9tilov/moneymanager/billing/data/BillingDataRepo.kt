package com.d9tilov.moneymanager.billing.data

import androidx.lifecycle.LifecycleObserver
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.billing.data.local.BillingSource
import com.d9tilov.moneymanager.billing.domain.BillingRepo
import com.d9tilov.moneymanager.billing.domain.entity.BillingSkuDetails
import com.d9tilov.moneymanager.billing.domain.entity.BillingSkuDetails.Companion.SKU_SUBSCRIPTION_ANNUAL
import com.d9tilov.moneymanager.billing.domain.entity.BillingSkuDetails.Companion.SKU_SUBSCRIPTION_QUARTERLY
import com.d9tilov.moneymanager.currency.data.entity.Currency
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

class BillingDataRepo(
    private val billingSource: BillingSource,
    private val defaultScope: CoroutineScope
) : BillingRepo {

    private val purchaseExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.tag(App.TAG).e("Billing flow completed with error: $exception")
    }

    private val skus = listOf(SKU_SUBSCRIPTION_QUARTERLY, SKU_SUBSCRIPTION_ANNUAL)
    private val purchaseCompleted: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        postMessagesFromBillingFlow()
    }

    override fun getObserver(): LifecycleObserver = billingSource

    override fun isPremium(): Flow<Boolean> {
        val flowList: List<Flow<Boolean>> = skus.map { billingSource.isPurchased(it) }
        return combine(flowList) { result -> result.firstOrNull { it } ?: false }
    }

    override fun getSkuDetails(): Flow<List<BillingSkuDetails>> {
        val flowList: List<Flow<BillingSkuDetails>> = skus.map { createSkuDetails(it) }
        return combine(flowList) { result: Array<BillingSkuDetails> -> result.toList() }
    }

    override fun getActiveSku(): Flow<BillingSkuDetails?> {
        val flowList: List<Flow<BillingSkuDetails>> = skus.map { createSkuDetails(it) }
        return combine(flowList) { result: Array<BillingSkuDetails> -> result.firstOrNull { it.isPurchased } }
    }

    override fun getMinPrice(): Flow<Currency> =
        getSkuDetails().map { list: List<BillingSkuDetails> ->
            list.minOfWith({ t1, t2 -> t1.value.compareTo(t2.value) }) { it.price }
        }

    private fun createSkuDetails(sku: String): Flow<BillingSkuDetails> = combine(
        billingSource.getSkuTitle(sku),
        billingSource.getSkuDescription(sku),
        billingSource.getSkuPrice(sku),
        billingSource.isPurchased(sku),
        billingSource.getSubscriptionMetaData(sku)
    ) { title, description, price, isPurchased, metaData ->
        BillingSkuDetails(
            sku,
            title,
            description,
            price,
            isPurchased,
            metaData
        )
    }

    override fun buySku(
        sku: String,
        result: (billingClient: BillingClient, paramBuilder: BillingFlowParams) -> BillingResult
    ) {
        var oldSku: String? = null
        when (sku) {
            SKU_SUBSCRIPTION_QUARTERLY -> oldSku = SKU_SUBSCRIPTION_ANNUAL
            SKU_SUBSCRIPTION_ANNUAL -> oldSku = SKU_SUBSCRIPTION_QUARTERLY
        }
        if (oldSku == null) billingSource.launchBillingFlow(sku, result)
        else billingSource.launchBillingFlow(sku, result, oldSku)
    }

    override fun purchaseCompleted(): Flow<Boolean> = purchaseCompleted

    override fun canPurchase(): Flow<Boolean> {
        val flowList: List<Flow<Boolean>> = skus.map { billingSource.canPurchase(it) }
        return combine(flowList) { result -> result.firstOrNull { it } ?: false }
    }

    private fun postMessagesFromBillingFlow() {
        defaultScope.launch(purchaseExceptionHandler) {
            billingSource.getNewPurchases().collect { list ->
                for (sku in list) {
                    when (sku) {
                        SKU_SUBSCRIPTION_QUARTERLY, SKU_SUBSCRIPTION_ANNUAL -> {
                            billingSource.refreshPurchases()
                            purchaseCompleted.emit(true)
                        }
                    }
                }
            }
        }
    }
}
