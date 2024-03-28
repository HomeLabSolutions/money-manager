package com.d9tilov.android.incomeexpense.expense.ui;

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
public final class ExpenseFragment_MembersInjector implements MembersInjector<ExpenseFragment> {
  private final Provider<FirebaseAnalytics> firebaseAnalyticsProvider;

  public ExpenseFragment_MembersInjector(Provider<FirebaseAnalytics> firebaseAnalyticsProvider) {
    this.firebaseAnalyticsProvider = firebaseAnalyticsProvider;
  }

  public static MembersInjector<ExpenseFragment> create(
      Provider<FirebaseAnalytics> firebaseAnalyticsProvider) {
    return new ExpenseFragment_MembersInjector(firebaseAnalyticsProvider);
  }

  @Override
  public void injectMembers(ExpenseFragment instance) {
    injectFirebaseAnalytics(instance, firebaseAnalyticsProvider.get());
  }

  @InjectedFieldSignature("com.d9tilov.android.incomeexpense.expense.ui.ExpenseFragment.firebaseAnalytics")
  public static void injectFirebaseAnalytics(ExpenseFragment instance,
      FirebaseAnalytics firebaseAnalytics) {
    instance.firebaseAnalytics = firebaseAnalytics;
  }
}
