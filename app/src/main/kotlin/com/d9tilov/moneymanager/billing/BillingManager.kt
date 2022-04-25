package com.d9tilov.moneymanager.billing

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode.OK
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.Purchase.PurchaseState.PURCHASED
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetailsParams
import com.android.billingclient.api.SkuDetailsResult
import com.android.billingclient.api.querySkuDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BillingManager(private val context: Context) {

    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult: BillingResult, purchases: MutableList<Purchase>? ->
            purchases?.let {
                if (billingResult.responseCode == OK) {
                    for (purchase in purchases) {
                        if (purchase.purchaseState == PURCHASED) {
                        }
                    }
                }
            }
        }

    private val billingClient: BillingClient =
        BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()

    suspend fun querySkuDetails() {
        val skuList = ArrayList<String>()
        skuList.add("premium_upgrade")
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
        val skuDetailsResult: SkuDetailsResult = withContext(Dispatchers.IO) {
            billingClient.querySkuDetails(params.build())
        }
    }
}
