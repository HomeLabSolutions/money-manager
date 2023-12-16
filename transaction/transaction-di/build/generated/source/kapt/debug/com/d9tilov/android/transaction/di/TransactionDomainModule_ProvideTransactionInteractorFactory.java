package com.d9tilov.android.transaction.di;

import com.d9tilov.android.budget.domain.contract.BudgetInteractor;
import com.d9tilov.android.category.domain.contract.CategoryInteractor;
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor;
import com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionInteractor;
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor;
import com.d9tilov.android.transaction.domain.contract.TransactionRepo;
import com.d9tilov.android.user.domain.contract.UserInteractor;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("dagger.hilt.android.scopes.ActivityRetainedScoped")
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
public final class TransactionDomainModule_ProvideTransactionInteractorFactory implements Factory<TransactionInteractor> {
  private final Provider<TransactionRepo> transactionRepoProvider;

  private final Provider<RegularTransactionInteractor> regularTransactionInteractorProvider;

  private final Provider<CategoryInteractor> categoryInteractorProvider;

  private final Provider<UserInteractor> userInteractorProvider;

  private final Provider<CurrencyInteractor> currencyInteractorProvider;

  private final Provider<BudgetInteractor> budgetInteractorProvider;

  public TransactionDomainModule_ProvideTransactionInteractorFactory(
      Provider<TransactionRepo> transactionRepoProvider,
      Provider<RegularTransactionInteractor> regularTransactionInteractorProvider,
      Provider<CategoryInteractor> categoryInteractorProvider,
      Provider<UserInteractor> userInteractorProvider,
      Provider<CurrencyInteractor> currencyInteractorProvider,
      Provider<BudgetInteractor> budgetInteractorProvider) {
    this.transactionRepoProvider = transactionRepoProvider;
    this.regularTransactionInteractorProvider = regularTransactionInteractorProvider;
    this.categoryInteractorProvider = categoryInteractorProvider;
    this.userInteractorProvider = userInteractorProvider;
    this.currencyInteractorProvider = currencyInteractorProvider;
    this.budgetInteractorProvider = budgetInteractorProvider;
  }

  @Override
  public TransactionInteractor get() {
    return provideTransactionInteractor(transactionRepoProvider.get(), regularTransactionInteractorProvider.get(), categoryInteractorProvider.get(), userInteractorProvider.get(), currencyInteractorProvider.get(), budgetInteractorProvider.get());
  }

  public static TransactionDomainModule_ProvideTransactionInteractorFactory create(
      Provider<TransactionRepo> transactionRepoProvider,
      Provider<RegularTransactionInteractor> regularTransactionInteractorProvider,
      Provider<CategoryInteractor> categoryInteractorProvider,
      Provider<UserInteractor> userInteractorProvider,
      Provider<CurrencyInteractor> currencyInteractorProvider,
      Provider<BudgetInteractor> budgetInteractorProvider) {
    return new TransactionDomainModule_ProvideTransactionInteractorFactory(transactionRepoProvider, regularTransactionInteractorProvider, categoryInteractorProvider, userInteractorProvider, currencyInteractorProvider, budgetInteractorProvider);
  }

  public static TransactionInteractor provideTransactionInteractor(TransactionRepo transactionRepo,
      RegularTransactionInteractor regularTransactionInteractor,
      CategoryInteractor categoryInteractor, UserInteractor userInteractor,
      CurrencyInteractor currencyInteractor, BudgetInteractor budgetInteractor) {
    return Preconditions.checkNotNullFromProvides(TransactionDomainModule.INSTANCE.provideTransactionInteractor(transactionRepo, regularTransactionInteractor, categoryInteractor, userInteractor, currencyInteractor, budgetInteractor));
  }
}
