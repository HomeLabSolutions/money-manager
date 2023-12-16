package com.d9tilov.android.regular.transaction.di;

import com.d9tilov.android.category.domain.contract.CategoryInteractor;
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor;
import com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionInteractor;
import com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionRepo;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class RegularTransactionDomainModule_ProvideRegularTransactionInteractorFactory implements Factory<RegularTransactionInteractor> {
  private final Provider<RegularTransactionRepo> regularTransactionRepoProvider;

  private final Provider<CurrencyInteractor> currencyInteractorProvider;

  private final Provider<CategoryInteractor> categoryInteractorProvider;

  public RegularTransactionDomainModule_ProvideRegularTransactionInteractorFactory(
      Provider<RegularTransactionRepo> regularTransactionRepoProvider,
      Provider<CurrencyInteractor> currencyInteractorProvider,
      Provider<CategoryInteractor> categoryInteractorProvider) {
    this.regularTransactionRepoProvider = regularTransactionRepoProvider;
    this.currencyInteractorProvider = currencyInteractorProvider;
    this.categoryInteractorProvider = categoryInteractorProvider;
  }

  @Override
  public RegularTransactionInteractor get() {
    return provideRegularTransactionInteractor(regularTransactionRepoProvider.get(), currencyInteractorProvider.get(), categoryInteractorProvider.get());
  }

  public static RegularTransactionDomainModule_ProvideRegularTransactionInteractorFactory create(
      Provider<RegularTransactionRepo> regularTransactionRepoProvider,
      Provider<CurrencyInteractor> currencyInteractorProvider,
      Provider<CategoryInteractor> categoryInteractorProvider) {
    return new RegularTransactionDomainModule_ProvideRegularTransactionInteractorFactory(regularTransactionRepoProvider, currencyInteractorProvider, categoryInteractorProvider);
  }

  public static RegularTransactionInteractor provideRegularTransactionInteractor(
      RegularTransactionRepo regularTransactionRepo, CurrencyInteractor currencyInteractor,
      CategoryInteractor categoryInteractor) {
    return Preconditions.checkNotNullFromProvides(RegularTransactionDomainModule.INSTANCE.provideRegularTransactionInteractor(regularTransactionRepo, currencyInteractor, categoryInteractor));
  }
}
