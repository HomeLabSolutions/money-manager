package com.d9tilov.android.billing.domain.impl

import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.billing.domain.contract.BillingRepo
import com.d9tilov.android.billing.domain.model.BillingSkuDetails
import com.d9tilov.android.billing.domain.model.PremiumInfo
import com.d9tilov.android.currency.domain.contract.mapper.toDomain
import com.d9tilov.android.currency.domain.model.DomainCurrency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class BillingInteractorImpl(
    private val billingRepo: BillingRepo,
) : BillingInteractor {
    override val isNewPurchaseAcknowledged: Flow<Boolean> = billingRepo.isNewPurchaseAcknowledged
    override val billingConnectionReady: Flow<Boolean> = billingRepo.billingConnectionReady

    override fun getPremiumInfo(): Flow<PremiumInfo> {
        val canPurchaseFlow = flowOf(true)
        val minPriceFlow =
            getSkuDetails()
                .map { list: List<BillingSkuDetails> ->
                    if (list.isEmpty()) {
                        DomainCurrency.EMPTY
                    } else {
                        list.minOfWith({ t1, t2 -> t1.value.compareTo(t2.value) }) { it.price }.toDomain(false)
                    }
                }
        return combine(
            canPurchaseFlow,
            isPremium(),
            billingRepo.hasRenewablePremium,
            minPriceFlow,
        ) { canPurchase, isPremium, hasActiveSku, minPrice ->
            PremiumInfo(
                canPurchase,
                isPremium,
                hasActiveSku,
                minPrice,
            )
        }
    }

    override fun startBillingConnection() = billingRepo.startBillingConnection()

    override fun terminateBillingConnection() = billingRepo.terminateBillingConnection()

    override fun isPremium(): Flow<Boolean> {
        return combine(
            billingRepo.isPremiumEmail,
            billingRepo.hasPremiumPurchase,
        ) { isPremiumExists, isPremium -> true }
    }

    override fun getSkuDetails(): Flow<List<BillingSkuDetails>> = billingRepo.getSkuDetails()
}
