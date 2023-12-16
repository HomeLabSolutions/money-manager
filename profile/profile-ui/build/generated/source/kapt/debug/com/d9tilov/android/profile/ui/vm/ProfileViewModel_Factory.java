package com.d9tilov.android.profile.ui.vm;

import com.d9tilov.android.billing.domain.contract.BillingInteractor;
import com.d9tilov.android.budget.domain.contract.BudgetInteractor;
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor;
import com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionInteractor;
import com.d9tilov.android.user.domain.contract.UserInteractor;
import com.google.firebase.analytics.FirebaseAnalytics;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class ProfileViewModel_Factory implements Factory<ProfileViewModel> {
  private final Provider<FirebaseAnalytics> firebaseAnalyticsProvider;

  private final Provider<UserInteractor> userInfoInteractorProvider;

  private final Provider<CurrencyInteractor> currencyInteractorProvider;

  private final Provider<BudgetInteractor> budgetInteractorProvider;

  private final Provider<RegularTransactionInteractor> regularTransactionInteractorProvider;

  private final Provider<BillingInteractor> billingInteractorProvider;

  public ProfileViewModel_Factory(Provider<FirebaseAnalytics> firebaseAnalyticsProvider,
      Provider<UserInteractor> userInfoInteractorProvider,
      Provider<CurrencyInteractor> currencyInteractorProvider,
      Provider<BudgetInteractor> budgetInteractorProvider,
      Provider<RegularTransactionInteractor> regularTransactionInteractorProvider,
      Provider<BillingInteractor> billingInteractorProvider) {
    this.firebaseAnalyticsProvider = firebaseAnalyticsProvider;
    this.userInfoInteractorProvider = userInfoInteractorProvider;
    this.currencyInteractorProvider = currencyInteractorProvider;
    this.budgetInteractorProvider = budgetInteractorProvider;
    this.regularTransactionInteractorProvider = regularTransactionInteractorProvider;
    this.billingInteractorProvider = billingInteractorProvider;
  }

  @Override
  public ProfileViewModel get() {
    return newInstance(firebaseAnalyticsProvider.get(), userInfoInteractorProvider.get(), currencyInteractorProvider.get(), budgetInteractorProvider.get(), regularTransactionInteractorProvider.get(), billingInteractorProvider.get());
  }

  public static ProfileViewModel_Factory create(
      Provider<FirebaseAnalytics> firebaseAnalyticsProvider,
      Provider<UserInteractor> userInfoInteractorProvider,
      Provider<CurrencyInteractor> currencyInteractorProvider,
      Provider<BudgetInteractor> budgetInteractorProvider,
      Provider<RegularTransactionInteractor> regularTransactionInteractorProvider,
      Provider<BillingInteractor> billingInteractorProvider) {
    return new ProfileViewModel_Factory(firebaseAnalyticsProvider, userInfoInteractorProvider, currencyInteractorProvider, budgetInteractorProvider, regularTransactionInteractorProvider, billingInteractorProvider);
  }

  public static ProfileViewModel newInstance(FirebaseAnalytics firebaseAnalytics,
      UserInteractor userInfoInteractor, CurrencyInteractor currencyInteractor,
      BudgetInteractor budgetInteractor, RegularTransactionInteractor regularTransactionInteractor,
      BillingInteractor billingInteractor) {
    return new ProfileViewModel(firebaseAnalytics, userInfoInteractor, currencyInteractor, budgetInteractor, regularTransactionInteractor, billingInteractor);
  }
}
