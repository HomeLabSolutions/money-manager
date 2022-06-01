package com.d9tilov.moneymanager.billing.domain

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.d9tilov.moneymanager.billing.domain.entity.BillingSkuDetails
import com.d9tilov.moneymanager.currency.data.entity.Currency
import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import com.d9tilov.moneymanager.currency.domain.mapper.CurrencyDomainMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class BillingInteractorImpl(private val billingRepo: BillingRepo, private val currencyDomainMapper: CurrencyDomainMapper) : BillingInteractor {

    override val currentPurchases: Flow<List<Purchase>> = billingRepo.currentPurchases
    override val productDetails: Flow<ProductDetails?> = billingRepo.premiumProductDetails
    override val isNewPurchaseAcknowledged: Flow<Boolean> = billingRepo.isNewPurchaseAcknowledged
    override val hasRenewablePremium: Flow<Boolean> = billingRepo.hasRenewablePremium

    override fun startBillingConnection(billingConnectionState: MutableStateFlow<Boolean>) {
        billingRepo.startBillingConnection(billingConnectionState)
    }

    override fun terminateBillingConnection() {
        billingRepo.terminateBillingConnection()
    }

    override fun canPurchase(): Flow<Boolean> =
        billingRepo.premiumProductDetails.map { productDetails -> productDetails != null }

    override fun buySku(
        tag: String,
        productDetails: ProductDetails?,
        currentPurchases: List<Purchase>,
        result: (billingClient: BillingClient, paramBuilder: BillingFlowParams) -> BillingResult
    ) {
        billingRepo.buySku(tag, productDetails, currentPurchases, result)
    }

    override fun isPremium(): Flow<Boolean> = currentPurchases.map { it.isNotEmpty() }
    override fun getSkuDetails(): Flow<List<BillingSkuDetails>> = billingRepo.getSkuDetails()
    override fun getMinPrice(): Flow<DomainCurrency> =
        getSkuDetails().map { list: List<BillingSkuDetails> ->
            list.minOfWith({ t1, t2 -> t1.value.compareTo(t2.value) }) { it.price }
        }.map { item: Currency -> currencyDomainMapper.toDomain(item, false) }
}
