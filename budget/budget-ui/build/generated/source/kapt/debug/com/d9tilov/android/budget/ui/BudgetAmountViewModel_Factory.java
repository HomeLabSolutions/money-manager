package com.d9tilov.android.budget.ui;

import com.d9tilov.android.budget.domain.contract.BudgetInteractor;
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
public final class BudgetAmountViewModel_Factory implements Factory<BudgetAmountViewModel> {
  private final Provider<BudgetInteractor> budgetInteractorProvider;

  public BudgetAmountViewModel_Factory(Provider<BudgetInteractor> budgetInteractorProvider) {
    this.budgetInteractorProvider = budgetInteractorProvider;
  }

  @Override
  public BudgetAmountViewModel get() {
    return newInstance(budgetInteractorProvider.get());
  }

  public static BudgetAmountViewModel_Factory create(
      Provider<BudgetInteractor> budgetInteractorProvider) {
    return new BudgetAmountViewModel_Factory(budgetInteractorProvider);
  }

  public static BudgetAmountViewModel newInstance(BudgetInteractor budgetInteractor) {
    return new BudgetAmountViewModel(budgetInteractor);
  }
}
