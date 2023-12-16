package com.d9tilov.android.profile.ui.vm;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B7\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u00a2\u0006\u0002\u0010\u000eJ\u0006\u0010\u001e\u001a\u00020\u001fJ\u0014\u0010 \u001a\u00020\u001f2\f\u0010!\u001a\b\u0012\u0004\u0012\u00020\u001f0\"J\u0006\u0010\u0017\u001a\u00020\u001fR\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0017\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00110\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0016R\"\u0010\u0019\u001a\u0016\u0012\u0012\u0012\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u001c\u0012\u0004\u0012\u00020\u001d0\u001b0\u001aX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"Lcom/d9tilov/android/profile/ui/vm/ProfileViewModel;", "Landroidx/lifecycle/ViewModel;", "firebaseAnalytics", "Lcom/google/firebase/analytics/FirebaseAnalytics;", "userInfoInteractor", "Lcom/d9tilov/android/user/domain/contract/UserInteractor;", "currencyInteractor", "Lcom/d9tilov/android/currency/domain/contract/CurrencyInteractor;", "budgetInteractor", "Lcom/d9tilov/android/budget/domain/contract/BudgetInteractor;", "regularTransactionInteractor", "Lcom/d9tilov/android/regular/transaction/domain/contract/RegularTransactionInteractor;", "billingInteractor", "Lcom/d9tilov/android/billing/domain/contract/BillingInteractor;", "(Lcom/google/firebase/analytics/FirebaseAnalytics;Lcom/d9tilov/android/user/domain/contract/UserInteractor;Lcom/d9tilov/android/currency/domain/contract/CurrencyInteractor;Lcom/d9tilov/android/budget/domain/contract/BudgetInteractor;Lcom/d9tilov/android/regular/transaction/domain/contract/RegularTransactionInteractor;Lcom/d9tilov/android/billing/domain/contract/BillingInteractor;)V", "_showDialog", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "profileState", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/d9tilov/android/profile/ui/vm/ProfileUiState;", "getProfileState", "()Lkotlinx/coroutines/flow/StateFlow;", "showDialog", "getShowDialog", "userCurrencyPair", "Lkotlinx/coroutines/flow/Flow;", "Lkotlin/Pair;", "Lcom/d9tilov/android/user/domain/model/UserProfile;", "Lcom/d9tilov/android/currency/domain/model/CurrencyMetaData;", "dismissDialog", "", "logout", "navigateCallback", "Lkotlin/Function0;", "profile-ui_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class ProfileViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.google.firebase.analytics.FirebaseAnalytics firebaseAnalytics = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.user.domain.contract.UserInteractor userInfoInteractor = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _showDialog = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> showDialog = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.Flow<kotlin.Pair<com.d9tilov.android.user.domain.model.UserProfile, com.d9tilov.android.currency.domain.model.CurrencyMetaData>> userCurrencyPair = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.d9tilov.android.profile.ui.vm.ProfileUiState> profileState = null;
    
    @javax.inject.Inject
    public ProfileViewModel(@org.jetbrains.annotations.NotNull
    com.google.firebase.analytics.FirebaseAnalytics firebaseAnalytics, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.user.domain.contract.UserInteractor userInfoInteractor, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.currency.domain.contract.CurrencyInteractor currencyInteractor, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.budget.domain.contract.BudgetInteractor budgetInteractor, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionInteractor regularTransactionInteractor, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.billing.domain.contract.BillingInteractor billingInteractor) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getShowDialog() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.d9tilov.android.profile.ui.vm.ProfileUiState> getProfileState() {
        return null;
    }
    
    public final void showDialog() {
    }
    
    public final void dismissDialog() {
    }
    
    public final void logout(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> navigateCallback) {
    }
}