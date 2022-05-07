package com.d9tilov.moneymanager.billing.data.local

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LifecycleOwner
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesResult
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsParams
import com.android.billingclient.api.acknowledgePurchase
import com.android.billingclient.api.queryPurchasesAsync
import com.android.billingclient.api.querySkuDetails
import com.d9tilov.moneymanager.billing.Security
import com.d9tilov.moneymanager.billing.domain.entity.PurchaseMetaData
import com.d9tilov.moneymanager.billing.domain.entity.PurchaseRemote
import com.d9tilov.moneymanager.billing.domain.mapper.toPurchaseMetaData
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_CURRENCY_CODE
import com.d9tilov.moneymanager.currency.data.entity.Currency
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import java.math.BigDecimal
import java.util.LinkedList
import kotlin.math.min

/**
 * The BillingDataSource implements all billing functionality for our test application.
 * Purchases can happen while in the app or at any time while out of the app, so the
 * BillingDataSource has to account for that.
 *
 * Since every SKU (Product ID) can have an individual state, all SKUs have an associated StateFlow
 * to allow their state to be observed.
 *
 * This BillingDataSource knows nothing about the application; all necessary information is either
 * passed into the constructor, exported as observable Flows, or exported through callbacks.
 * This code can be reused in a variety of apps.
 *
 * Beginning a purchase flow involves passing an Activity into the Billing Library, but we merely
 * pass it along to the API.
 *
 * This data source has a few automatic features:
 * 1) It checks for a valid signature on all purchases before attempting to acknowledge them.
 * 2) It automatically acknowledges all known SKUs for non-consumables, and doesn't set the state
 * to purchased until the acknowledgement is complete.
 * 3) The data source will automatically consume skus that are set in knownAutoConsumeSKUs. As
 * SKUs are consumed, a Flow will emit.
 * 4) If the BillingService is disconnected, it will attempt to reconnect with exponential
 * fallback.
 *
 * This data source attempts to keep billing library specific knowledge confined to this file;
 * The only thing that clients of the BillingDataSource need to know are the SKUs used by their
 * application.
 *
 * The BillingClient needs access to the Application context in order to bind the remote billing
 * service.
 *
 * The BillingDataSource can also act as a LifecycleObserver for an Activity; this allows it to
 * refresh purchases during onResume.
 */

private const val RECONNECT_TIMER_START_MILLISECONDS = 1L * 1000L
private const val RECONNECT_TIMER_MAX_TIME_MILLISECONDS = 1000L * 60L * 15L // 15 minutes
private const val SKU_DETAILS_REQUERY_TIME = 1000L * 60L * 60L * 4L // 4 hours

/**
 * Our constructor.  Since we are a singleton, this is only used internally.
 * @param application Android application class.
 * @param knownSubscriptionSKUs SKUs of subscriptions the source should know about
 */
class BillingDataSource constructor(
    application: Application,
    private val defaultScope: CoroutineScope,
    private val knownSubscriptionSKUs: List<String>
) : PurchasesUpdatedListener, BillingClientStateListener, BillingSource {

    private val handler = Handler(Looper.getMainLooper())
    private val billingClient: BillingClient = BillingClient.newBuilder(application)
        .setListener(this)
        .enablePendingPurchases()
        .build()

    // how long before the data source tries to reconnect to Google play
    private var reconnectMilliseconds = RECONNECT_TIMER_START_MILLISECONDS

    // when was the last successful SkuDetailsResponse?
    private var skuDetailsResponseTime = -SKU_DETAILS_REQUERY_TIME

    private enum class SkuState {
        SKU_STATE_UNPURCHASED, SKU_STATE_PENDING, SKU_STATE_PURCHASED, SKU_STATE_PURCHASED_AND_ACKNOWLEDGED
    }

    // Flows that are mostly maintained so they can be transformed into observables.
    private val skuStateMap: MutableMap<String, MutableStateFlow<SkuState>> = HashMap()
    private val skuDetailsMap: MutableMap<String, MutableStateFlow<SkuDetails?>> = HashMap()
    private val purchaseJsonMap: MutableMap<String, MutableStateFlow<String>> = HashMap()

    private val newPurchaseFlow = MutableSharedFlow<List<String>>(extraBufferCapacity = 1)
    private val billingFlowInProcess = MutableStateFlow(false)

    init {
        initializeFlows()
        billingClient.startConnection(this)
    }

    override fun onBillingSetupFinished(billingResult: BillingResult) {
        val responseCode = billingResult.responseCode
        val debugMessage = billingResult.debugMessage
        Timber.tag(PURCHASE_TAG).d("onBillingSetupFinished: $responseCode $debugMessage")
        when (responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                // The billing client is ready. You can query purchases here.
                // This doesn't mean that your app is set up correctly in the console -- it just
                // means that you have a connection to the Billing service.
                reconnectMilliseconds = RECONNECT_TIMER_START_MILLISECONDS
                defaultScope.launch {
                    querySkuDetailsAsync()
                    refreshPurchases()
                }
            }
            else -> retryBillingServiceConnectionWithExponentialBackoff()
        }
    }

    /**
     * This is a pretty unusual occurrence. It happens primarily if the Google Play Store
     * self-upgrades or is force closed.
     */
    override fun onBillingServiceDisconnected() {
        retryBillingServiceConnectionWithExponentialBackoff()
    }

    /**
     * Retries the billing service connection with exponential backoff, maxing out at the time
     * specified by RECONNECT_TIMER_MAX_TIME_MILLISECONDS.
     */
    private fun retryBillingServiceConnectionWithExponentialBackoff() {
        handler.postDelayed(
            { billingClient.startConnection(this@BillingDataSource) },
            reconnectMilliseconds
        )
        reconnectMilliseconds = min(
            reconnectMilliseconds * 2,
            RECONNECT_TIMER_MAX_TIME_MILLISECONDS
        )
    }

    /**
     * Called by initializeFlows to create the various Flow objects we're planning to emit.
     * @param skuList a List<String> of SKUs representing purchases and subscriptions.
    </String> */
    private fun addSkuFlows(skuList: List<String>) {
        for (sku in skuList) {
            val skuState = MutableStateFlow(SkuState.SKU_STATE_UNPURCHASED)
            val details = MutableStateFlow<SkuDetails?>(null)
            val jsonFlow = MutableStateFlow("{}")
            details.subscriptionCount.map { count -> count > 0 } // map count into active/inactive flag
                .distinctUntilChanged() // only react to true<->false changes
                .onEach { isActive -> // configure an action
                    if (isActive && (SystemClock.elapsedRealtime() - skuDetailsResponseTime > SKU_DETAILS_REQUERY_TIME)) {
                        skuDetailsResponseTime = SystemClock.elapsedRealtime()
                        Timber.tag(PURCHASE_TAG).v("Skus not fresh, requiring")
                        querySkuDetailsAsync()
                    }
                }
                .launchIn(defaultScope) // launch it
            Timber.tag(PURCHASE_TAG).d("set skuState: ${skuState.value}")
            skuStateMap[sku] = skuState
            skuDetailsMap[sku] = details
            purchaseJsonMap[sku] = jsonFlow
        }
    }

    /**
     * Creates a Flow object for every known SKU so the state and SKU details can be observed
     * in other layers. The repository is responsible for mapping this data in ways that are more
     * useful for the application.
     */
    private fun initializeFlows() {
        addSkuFlows(knownSubscriptionSKUs)
    }

    override fun getNewPurchases() = newPurchaseFlow.asSharedFlow()

    /**
     * Returns whether or not the user has purchased a SKU. It does this by returning
     * a Flow that returns true if the SKU is in the PURCHASED state and
     * the Purchase has been acknowledged.
     * @return a Flow that observes the SKUs purchase state
     */
    override fun isPurchased(sku: String): Flow<Boolean> {
        val skuStateFLow = skuStateMap[sku]!!
        return skuStateFLow.map { skuState ->
            Timber.tag(PURCHASE_TAG).d("skuState: $skuState")
            skuState == SkuState.SKU_STATE_PURCHASED_AND_ACKNOWLEDGED
        }
    }

    /**
     * Returns whether or not the user can purchase a SKU. It does this by returning
     * a Flow combine transformation that returns true if the SKU is in the UNSPECIFIED state, as
     * well as if we have skuDetails for the SKU. (SKUs cannot be purchased without valid
     * SkuDetails.)
     * @return a Flow that observes the SKUs purchase state
     */
    override fun canPurchase(sku: String): Flow<Boolean> {
        val skuDetailsFlow = skuDetailsMap[sku]!!
        val skuStateFlow = skuStateMap[sku]!!

        return skuStateFlow.combine(skuDetailsFlow) { skuState, skuDetails ->
            skuState == SkuState.SKU_STATE_UNPURCHASED && skuDetails != null
        }
    }


    // There's lots of information in SkuDetails, but our app only needs a few things, since our
    // goods never go on sale, have introductory pricing, etc. You can add to this for your app,
    // or create your own class to pass the information across.
    /**
     * The title of our SKU from SkuDetails.
     * @param sku to get the title from
     * @return title of the requested SKU as an observable Flow<String>
    </String> */
    override fun getSkuTitle(sku: String): Flow<String> {
        val skuDetailsFlow = skuDetailsMap[sku]!!
        return skuDetailsFlow.mapNotNull { skuDetails ->
            Timber.tag(PURCHASE_TAG).d("getSkuTitle: $skuDetails")
            skuDetails?.title
        }
    }

    override fun getSkuPrice(sku: String): Flow<Currency> {
        val skuDetailsFlow = skuDetailsMap[sku]!!
        return skuDetailsFlow.mapNotNull { skuDetails ->
            Currency.EMPTY.copy(
                code = skuDetails?.priceCurrencyCode ?: DEFAULT_CURRENCY_CODE,
                value = skuDetails?.priceAmountMicros?.toBigDecimal()?.divide(BigDecimal(1_000_000))
                    ?: BigDecimal.ZERO
            )
        }
    }

    override fun getSubscriptionMetaData(sku: String): Flow<PurchaseMetaData?> {
        val jsonFlow = purchaseJsonMap[sku]!!
        return jsonFlow.mapNotNull { purchaseJson: String ->
            val moshi: Moshi = Moshi.Builder().build()
            val jsonAdapter: JsonAdapter<PurchaseRemote> = moshi.adapter(PurchaseRemote::class.java)
            val purchaseRemote = jsonAdapter.fromJson(purchaseJson)
            purchaseRemote?.toPurchaseMetaData()
        }
    }

    override fun getSkuDescription(sku: String): Flow<String> {
        val skuDetailsFlow = skuDetailsMap[sku]!!
        return skuDetailsFlow.mapNotNull { skuDetails ->
            skuDetails?.description
        }
    }

    /**
     * Receives the result from [.querySkuDetailsAsync]}.
     *
     * Store the SkuDetails and post them in the [.skuDetailsMap]. This allows other
     * parts of the app to use the [SkuDetails] to show SKU information and make purchases.
     */
    private fun onSkuDetailsResponse(
        billingResult: BillingResult,
        skuDetailsList: List<SkuDetails>?
    ) {
        val responseCode = billingResult.responseCode
        val debugMessage = billingResult.debugMessage
        when (responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                Timber.tag(PURCHASE_TAG).i("onSkuDetailsResponse: $responseCode $debugMessage")
                if (skuDetailsList == null || skuDetailsList.isEmpty()) {
                    Timber.tag(PURCHASE_TAG)
                        .e(
                            "onSkuDetailsResponse: Found null or empty SkuDetails. " +
                                    "Check to see if the SKUs you requested are correctly published in the Google Play Console."
                        )
                } else {
                    for (skuDetails in skuDetailsList) {
                        val sku = skuDetails.sku
                        val detailsMutableFlow = skuDetailsMap[sku]
                        detailsMutableFlow?.tryEmit(skuDetails)
                            ?: Timber.tag(PURCHASE_TAG).e("Unknown sku: $sku")
                    }
                }
            }
            BillingClient.BillingResponseCode.SERVICE_DISCONNECTED,
            BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE,
            BillingClient.BillingResponseCode.BILLING_UNAVAILABLE,
            BillingClient.BillingResponseCode.ITEM_UNAVAILABLE,
            BillingClient.BillingResponseCode.DEVELOPER_ERROR,
            BillingClient.BillingResponseCode.ERROR ->
                Timber.tag(PURCHASE_TAG).e("onSkuDetailsResponse: $responseCode $debugMessage")
            BillingClient.BillingResponseCode.USER_CANCELED ->
                Timber.tag(PURCHASE_TAG).i("onSkuDetailsResponse: $responseCode $debugMessage")
            BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED,
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED,
            BillingClient.BillingResponseCode.ITEM_NOT_OWNED ->
                Timber.tag(PURCHASE_TAG).wtf("onSkuDetailsResponse: $responseCode $debugMessage")
            else -> Timber.tag(PURCHASE_TAG)
                .wtf("onSkuDetailsResponse: $responseCode $debugMessage")
        }
        skuDetailsResponseTime =
            if (responseCode == BillingClient.BillingResponseCode.OK) SystemClock.elapsedRealtime()
            else -SKU_DETAILS_REQUERY_TIME
    }

    /**
     * Calls the billing client functions to query sku details for both the inapp and subscription
     * SKUs. SKU details are useful for displaying item names and price lists to the user, and are
     * required to make a purchase.
     */
    private suspend fun querySkuDetailsAsync() {
        if (!knownSubscriptionSKUs.isNullOrEmpty()) {
            val skuDetailsResult = billingClient.querySkuDetails(
                SkuDetailsParams.newBuilder()
                    .setType(BillingClient.SkuType.SUBS)
                    .setSkusList(knownSubscriptionSKUs)
                    .build()
            )
            onSkuDetailsResponse(skuDetailsResult.billingResult, skuDetailsResult.skuDetailsList)
        }
    }

    /*
      GPBLv3 now queries purchases synchronously, simplifying this flow. This only gets active
      purchases.
   */
    override suspend fun refreshPurchases() {
        Timber.tag(PURCHASE_TAG).d("Refreshing purchases.")
        val purchasesResult: PurchasesResult =
            billingClient.queryPurchasesAsync(BillingClient.SkuType.SUBS)
        Timber.tag(PURCHASE_TAG).d("Refreshing purchases result: $purchasesResult")
        val billingResult = purchasesResult.billingResult
        if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
            Timber.tag(PURCHASE_TAG)
                .e("Problem getting subscriptions: ${billingResult.debugMessage}")
        } else {
            processPurchaseList(purchasesResult.purchasesList, knownSubscriptionSKUs)
        }
        Timber.tag(PURCHASE_TAG).d("Refreshing purchases finished.")
    }

    /**
     * Used internally to get purchases from a requested set of SKUs. This is particularly
     * important when changing subscriptions, as onPurchasesUpdated won't update the purchase state
     * of a subscription that has been upgraded from.
     *
     * @param skus skus to get purchase information for
     * @param skuType sku type, inapp or subscription, to get purchase information for.
     * @return purchases
     */
    private suspend fun getPurchases(skus: List<String>, skuType: String): List<Purchase> {
        val purchasesResult = billingClient.queryPurchasesAsync(skuType)
        val br = purchasesResult.billingResult
        val returnPurchasesList: MutableList<Purchase> = LinkedList()
        if (br.responseCode != BillingClient.BillingResponseCode.OK) {
            Timber.tag(PURCHASE_TAG).e("Problem getting purchases: ${br.debugMessage}")
        } else {
            val purchasesList = purchasesResult.purchasesList
            for (purchase in purchasesList) {
                for (sku in skus) {
                    for (purchaseSku in purchase.skus) {
                        if (purchaseSku == sku) {
                            returnPurchasesList.add(purchase)
                        }
                    }
                }
            }
        }
        return returnPurchasesList
    }

    /**
     * Calling this means that we have the most up-to-date information for a Sku in a purchase
     * object. This uses the purchase state (Pending, Unspecified, Purchased) along with the
     * acknowledged state.
     * @param purchase an up-to-date object to set the state for the Sku
     */
    private fun setSkuStateFromPurchase(purchase: Purchase) {
        for (purchaseSku in purchase.skus) {
            val skuStateFlow = skuStateMap[purchaseSku]
            if (null == skuStateFlow) {
                Timber.tag(PURCHASE_TAG)
                    .e("Unknown SKU $purchaseSku. Check to make sure SKU matches SKUS in the Play developer console.")
            } else {
                when (purchase.purchaseState) {
                    Purchase.PurchaseState.PENDING -> skuStateFlow.tryEmit(SkuState.SKU_STATE_PENDING)
                    Purchase.PurchaseState.UNSPECIFIED_STATE -> skuStateFlow.tryEmit(SkuState.SKU_STATE_UNPURCHASED)
                    Purchase.PurchaseState.PURCHASED -> if (purchase.isAcknowledged) {
                        skuStateFlow.tryEmit(SkuState.SKU_STATE_PURCHASED_AND_ACKNOWLEDGED)
                    } else {
                        skuStateFlow.tryEmit(SkuState.SKU_STATE_PURCHASED)
                    }
                    else -> Timber.tag(PURCHASE_TAG)
                        .e("Purchase in unknown state: ${purchase.purchaseState}")
                }
            }
        }
    }

    /**
     * Since we (mostly) are getting sku states when we actually make a purchase or update
     * purchases, we keep some internal state when we do things like acknowledge or consume.
     * @param sku product ID to change the state of
     * @param newSkuState the new state of the sku.
     */
    private fun setSkuState(sku: String, newSkuState: SkuState) {
        val skuStateFlow = skuStateMap[sku]
        skuStateFlow?.tryEmit(newSkuState)
            ?: Timber.tag(PURCHASE_TAG)
                .e("Unknown SKU $sku. Check to make sure SKU matches SKUS in the Play developer console.")
    }

    /**
     * Goes through each purchase and makes sure that the purchase state is processed and the state
     * is available through Flows. Verifies signature and acknowledges purchases. PURCHASED isn't
     * returned until the purchase is acknowledged.
     *
     * https://developer.android.com/google/play/billing/billing_library_releases_notes#2_0_acknowledge
     *
     * Developers can choose to acknowledge purchases from a server using the
     * Google Play Developer API. The server has direct access to the user database,
     * so using the Google Play Developer API for acknowledgement might be more reliable.
     *
     * If the purchase token is not acknowledged within 3 days,
     * then Google Play will automatically refund and revoke the purchase.
     * This behavior helps ensure that users are not charged unless the user has successfully
     * received access to the content.
     * This eliminates a category of issues where users complain to developers
     * that they paid for something that the app is not giving to them.
     *
     * If a skusToUpdate list is passed-into this method, any purchases not in the list of
     * purchases will have their state set to UNPURCHASED.
     *
     * @param purchases the List of purchases to process.
     * @param skusToUpdate a list of skus that we want to update the state from --- this allows us
     * to set the state of non-returned SKUs to UNPURCHASED.
     */
    private fun processPurchaseList(purchases: List<Purchase>?, skusToUpdate: List<String>?) {
        Timber.tag(PURCHASE_TAG).d("ProcessPurchaseList. Purchases: $purchases")
        val updatedSkus = HashMap<String, String>()
        if (null != purchases) {
            for (purchase in purchases) {
                for (sku in purchase.skus) {
                    val skuStateFlow = skuStateMap[sku]
                    if (null == skuStateFlow) {
                        Timber.tag(PURCHASE_TAG)
                            .e("Unknown SKU $sku. Check to make sure SKU matches SKUS in the Play developer console.")
                        continue
                    }
                    updatedSkus[sku] = purchase.originalJson
                }
                // Global check to make sure all purchases are signed correctly.
                // This check is best performed on your server.
                val purchaseState = purchase.purchaseState
                if (purchaseState == Purchase.PurchaseState.PURCHASED) {
                    if (!isSignatureValid(purchase)) {
                        Timber.tag(PURCHASE_TAG)
                            .e("Invalid signature. Check to make sure your public key is correct.")
                        continue
                    }
                    // only set the purchased state after we've validated the signature.
                    setSkuStateFromPurchase(purchase)
                    for (sku in purchase.skus) {
                        val json: String = updatedSkus[sku]!!
                        val jsonStatFlow = purchaseJsonMap[sku]!!
                        jsonStatFlow.tryEmit(json)
                    }
                    defaultScope.launch {
                        if (!purchase.isAcknowledged) {
                            // acknowledge everything --- new purchases are ones not yet acknowledged
                            val billingResult = billingClient.acknowledgePurchase(
                                AcknowledgePurchaseParams.newBuilder()
                                    .setPurchaseToken(purchase.purchaseToken)
                                    .build()
                            )
                            if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
                                Timber.tag(PURCHASE_TAG)
                                    .e("Error acknowledging purchase: ${purchase.skus}")
                            } else {
                                // purchase acknowledged
                                for (sku in purchase.skus) {
                                    setSkuState(sku, SkuState.SKU_STATE_PURCHASED_AND_ACKNOWLEDGED)
                                }
                            }
                            newPurchaseFlow.tryEmit(purchase.skus)
                        }
                    }
                } else setSkuStateFromPurchase(purchase)
            }
        } else Timber.tag(PURCHASE_TAG).d("Empty purchase list.")
        // Clear purchase state of anything that didn't come with this purchase list if this is
        // part of a refresh.
        if (null != skusToUpdate) {
            for (sku in skusToUpdate) {
                if (!updatedSkus.contains(sku)) {
                    setSkuState(sku, SkuState.SKU_STATE_UNPURCHASED)
                }
            }
        }
    }

    /**
     * Launch the billing flow. This will launch an external Activity for a result, so it requires
     * an Activity reference. For subscriptions, it supports upgrading from one SKU type to another
     * by passing in SKUs to be upgraded.
     *
     * @param sku SKU (Product ID) to be purchased
     * @param upgradeSkusVarargs SKUs that the subscription can be upgraded from
     * @return true if launch is successful
     */
    override fun launchBillingFlow(
        sku: String,
        result: (billingClient: BillingClient, paramBuilder: BillingFlowParams) -> BillingResult,
        vararg upgradeSkusVarargs: String
    ) {
        val skuDetails = skuDetailsMap[sku]?.value
        if (null != skuDetails) {
            val billingFlowParamsBuilder = BillingFlowParams.newBuilder()
            billingFlowParamsBuilder.setSkuDetails(skuDetails)
            val upgradeSkus = listOf(*upgradeSkusVarargs)
            defaultScope.launch {
                val heldSubscriptions = getPurchases(upgradeSkus, BillingClient.SkuType.SUBS)
                when (heldSubscriptions.size) {
                    1 -> {
                        val purchase = heldSubscriptions[0]
                        billingFlowParamsBuilder.setSubscriptionUpdateParams(
                            BillingFlowParams.SubscriptionUpdateParams.newBuilder()
                                .setOldSkuPurchaseToken(purchase.purchaseToken)
                                .build()
                        )
                    }
                    0 -> {}
                    else -> Timber.tag(PURCHASE_TAG).e(
                        "%s subscriptions subscribed to. Upgrade not possible.",
                        heldSubscriptions.size.toString()
                    )
                }
                val br = result.invoke(billingClient, billingFlowParamsBuilder.build())
                if (br.responseCode == BillingClient.BillingResponseCode.OK)
                    billingFlowInProcess.emit(true)
                else Timber.tag(PURCHASE_TAG).e("Billing failed: + ${br.debugMessage}")
            }
        } else Timber.tag(PURCHASE_TAG).e("SkuDetails not found for: $sku")
    }

    /**
     * Called by the BillingLibrary when new purchases are detected; typically in response to a
     * launchBillingFlow.
     * @param billingResult result of the purchase flow.
     * @param list of new purchases.
     */
    override fun onPurchasesUpdated(billingResult: BillingResult, list: List<Purchase>?) {
        Timber.tag(PURCHASE_TAG).d("onPurchasesUpdated. list: $list result: $billingResult")
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> if (null != list) {
                processPurchaseList(list, null)
                return
            } else Timber.tag(PURCHASE_TAG).d("Null Purchase List Returned from OK response!")
            BillingClient.BillingResponseCode.USER_CANCELED -> Timber.tag(PURCHASE_TAG)
                .i("onPurchasesUpdated: User canceled the purchase")
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> Timber.tag(PURCHASE_TAG)
                .i("onPurchasesUpdated: The user already owns this item")
            BillingClient.BillingResponseCode.DEVELOPER_ERROR -> Timber.tag(PURCHASE_TAG)
                .e("onPurchasesUpdated: Developer error means that Google Play does not recognize the configuration. If you are just getting started, make sure you have configured the application correctly in the Google Play Console. The SKU product ID must match and the APK you are using must be signed with release keys.")
            else -> Timber.tag(PURCHASE_TAG)
                .d("BillingResult [ ${billingResult.responseCode} ]: ${billingResult.debugMessage}")
        }
        defaultScope.launch {
            billingFlowInProcess.emit(false)
        }
    }

    /**
     * Ideally your implementation will comprise a secure server, rendering this check
     * unnecessary. @see [Security]
     */
    private fun isSignatureValid(purchase: Purchase): Boolean {
        return Security.verifyPurchase(purchase.originalJson, purchase.signature)
    }

    /**
     * It's recommended to requery purchases during onResume.
     */
    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Timber.tag(PURCHASE_TAG).d("ON_RESUME")
        // this just avoids an extra purchase refresh after we finish a billing flow
        if (!billingFlowInProcess.value) {
            if (billingClient.isReady) {
                defaultScope.launch {
                    refreshPurchases()
                }
            }
        }
    }

    companion object {
        const val SKU_SUBSCRIPTION_QUARTERLY = "premium_quarterly"
        const val SKU_SUBSCRIPTION_ANNUAL = "premium_annual"
        private const val PURCHASE_TAG = "[Purchase]"
    }
}
