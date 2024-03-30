package com.d9tilov.android.incomeexpense.ui;

import com.google.firebase.analytics.FirebaseAnalytics;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class IncomeExpenseFragment_MembersInjector implements MembersInjector<IncomeExpenseFragment> {
  private final Provider<FirebaseAnalytics> firebaseAnalyticsProvider;

  public IncomeExpenseFragment_MembersInjector(
      Provider<FirebaseAnalytics> firebaseAnalyticsProvider) {
    this.firebaseAnalyticsProvider = firebaseAnalyticsProvider;
  }

  public static MembersInjector<IncomeExpenseFragment> create(
      Provider<FirebaseAnalytics> firebaseAnalyticsProvider) {
    return new IncomeExpenseFragment_MembersInjector(firebaseAnalyticsProvider);
  }

  @Override
  public void injectMembers(IncomeExpenseFragment instance) {
    injectFirebaseAnalytics(instance, firebaseAnalyticsProvider.get());
  }

  @InjectedFieldSignature("com.d9tilov.android.incomeexpense.ui.IncomeExpenseFragment.firebaseAnalytics")
  public static void injectFirebaseAnalytics(IncomeExpenseFragment instance,
      FirebaseAnalytics firebaseAnalytics) {
    instance.firebaseAnalytics = firebaseAnalytics;
  }
}
