package com.d9tilov.android.incomeexpense.income.ui;

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
public final class IncomeFragment_MembersInjector implements MembersInjector<IncomeFragment> {
  private final Provider<FirebaseAnalytics> firebaseAnalyticsProvider;

  public IncomeFragment_MembersInjector(Provider<FirebaseAnalytics> firebaseAnalyticsProvider) {
    this.firebaseAnalyticsProvider = firebaseAnalyticsProvider;
  }

  public static MembersInjector<IncomeFragment> create(
      Provider<FirebaseAnalytics> firebaseAnalyticsProvider) {
    return new IncomeFragment_MembersInjector(firebaseAnalyticsProvider);
  }

  @Override
  public void injectMembers(IncomeFragment instance) {
    injectFirebaseAnalytics(instance, firebaseAnalyticsProvider.get());
  }

  @InjectedFieldSignature("com.d9tilov.android.incomeexpense.income.ui.IncomeFragment.firebaseAnalytics")
  public static void injectFirebaseAnalytics(IncomeFragment instance,
      FirebaseAnalytics firebaseAnalytics) {
    instance.firebaseAnalytics = firebaseAnalytics;
  }
}
