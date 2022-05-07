package com.d9tilov.moneymanager.billing.vm

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.d9tilov.moneymanager.billing.domain.BillingInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

//@HiltViewModel
class BillingViewModel /*@Inject*/ constructor(private val billingInteractor: BillingInteractor) {

    fun makePurchase(sku: String, result: (billingClient: BillingClient, paramBuilder: BillingFlowParams) -> BillingResult) {
        billingInteractor.buySku(sku, result)
    }
}
