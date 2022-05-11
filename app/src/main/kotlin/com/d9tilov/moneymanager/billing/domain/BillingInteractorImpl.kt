package com.d9tilov.moneymanager.billing.domain

import androidx.lifecycle.LifecycleObserver
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.d9tilov.moneymanager.billing.domain.entity.BillingSkuDetails
import com.d9tilov.moneymanager.currency.data.entity.Currency
import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import com.d9tilov.moneymanager.currency.domain.mapper.CurrencyDomainMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BillingInteractorImpl(private val billingRepo: BillingRepo, private val currencyDomainMapper: CurrencyDomainMapper) : BillingInteractor {

    override fun getObserver(): LifecycleObserver = billingRepo.getObserver()
    override fun isPremium(): Flow<Boolean> = billingRepo.isPremium()
    override fun getSkuDetails(): Flow<List<BillingSkuDetails>> = billingRepo.getSkuDetails()
    override fun getActiveSku(): Flow<BillingSkuDetails?> = billingRepo.getActiveSku()

    override fun getMinPrice(): Flow<DomainCurrency> = billingRepo.getMinPrice()
        .map { item: Currency -> currencyDomainMapper.toDomain(item, false) }

    override fun buySku(
        sku: String,
        result: (billingClient: BillingClient, paramBuilder: BillingFlowParams) -> BillingResult
    ) {
        billingRepo.buySku(sku, result)
    }

    override fun purchaseCompleted(): Flow<Boolean> = billingRepo.purchaseCompleted()

    override fun canPurchase(): Flow<Boolean> = billingRepo.canPurchase()
}
