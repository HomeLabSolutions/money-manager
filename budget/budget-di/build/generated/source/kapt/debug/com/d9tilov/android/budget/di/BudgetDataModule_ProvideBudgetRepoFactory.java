package com.d9tilov.android.budget.di;

import com.d9tilov.android.budget.data.contract.BudgetSource;
import com.d9tilov.android.budget.domain.contract.BudgetRepo;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class BudgetDataModule_ProvideBudgetRepoFactory implements Factory<BudgetRepo> {
  private final Provider<BudgetSource> budgetSourceProvider;

  public BudgetDataModule_ProvideBudgetRepoFactory(Provider<BudgetSource> budgetSourceProvider) {
    this.budgetSourceProvider = budgetSourceProvider;
  }

  @Override
  public BudgetRepo get() {
    return provideBudgetRepo(budgetSourceProvider.get());
  }

  public static BudgetDataModule_ProvideBudgetRepoFactory create(
      Provider<BudgetSource> budgetSourceProvider) {
    return new BudgetDataModule_ProvideBudgetRepoFactory(budgetSourceProvider);
  }

  public static BudgetRepo provideBudgetRepo(BudgetSource budgetSource) {
    return Preconditions.checkNotNullFromProvides(BudgetDataModule.INSTANCE.provideBudgetRepo(budgetSource));
  }
}
