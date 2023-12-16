package com.d9tilov.android.settings.ui.vm;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005JF\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u001826\u0010\u0019\u001a2\u0012\u0013\u0012\u00110\u001b\u00a2\u0006\f\b\u001c\u0012\b\b\u001d\u0012\u0004\b\b(\u001e\u0012\u0013\u0012\u00110\u001f\u00a2\u0006\f\b\u001c\u0012\b\b\u001d\u0012\u0004\b\b( \u0012\u0004\u0012\u00020!0\u001aR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u001d\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\u00070\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000fR\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\""}, d2 = {"Lcom/d9tilov/android/settings/ui/vm/SettingsBillingIntroViewModel;", "Lcom/d9tilov/android/common/android/ui/base/BaseViewModel;", "Lcom/d9tilov/android/settings/ui/navigation/SettingsBillingNavigator;", "billingInteractor", "Lcom/d9tilov/android/billing/domain/contract/BillingInteractor;", "(Lcom/d9tilov/android/billing/domain/contract/BillingInteractor;)V", "currentPurchases", "", "Lcom/android/billingclient/api/Purchase;", "productDetails", "Lcom/android/billingclient/api/ProductDetails;", "purchaseCompleted", "Lkotlinx/coroutines/flow/StateFlow;", "", "getPurchaseCompleted", "()Lkotlinx/coroutines/flow/StateFlow;", "skuDetails", "Lcom/d9tilov/android/billing/domain/model/BillingSkuDetails;", "getSkuDetails", "skuExceptionHandler", "Lkotlinx/coroutines/CoroutineExceptionHandler;", "buySubscription", "", "tag", "", "result", "Lkotlin/Function2;", "Lcom/android/billingclient/api/BillingClient;", "Lkotlin/ParameterName;", "name", "billingClient", "Lcom/android/billingclient/api/BillingFlowParams;", "paramBuilder", "Lcom/android/billingclient/api/BillingResult;", "settings-ui_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class SettingsBillingIntroViewModel extends com.d9tilov.android.common.android.ui.base.BaseViewModel<com.d9tilov.android.settings.ui.navigation.SettingsBillingNavigator> {
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.billing.domain.contract.BillingInteractor billingInteractor = null;
    @org.jetbrains.annotations.NotNull
    private java.util.List<? extends com.android.billingclient.api.Purchase> currentPurchases;
    @org.jetbrains.annotations.Nullable
    private com.android.billingclient.api.ProductDetails productDetails;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.CoroutineExceptionHandler skuExceptionHandler = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.d9tilov.android.billing.domain.model.BillingSkuDetails>> skuDetails = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> purchaseCompleted = null;
    
    @javax.inject.Inject
    public SettingsBillingIntroViewModel(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.billing.domain.contract.BillingInteractor billingInteractor) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.d9tilov.android.billing.domain.model.BillingSkuDetails>> getSkuDetails() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getPurchaseCompleted() {
        return null;
    }
    
    public final void buySubscription(@org.jetbrains.annotations.NotNull
    java.lang.String tag, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super com.android.billingclient.api.BillingClient, ? super com.android.billingclient.api.BillingFlowParams, com.android.billingclient.api.BillingResult> result) {
    }
}