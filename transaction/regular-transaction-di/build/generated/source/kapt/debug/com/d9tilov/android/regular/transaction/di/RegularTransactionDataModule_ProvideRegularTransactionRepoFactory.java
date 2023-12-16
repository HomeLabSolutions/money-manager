package com.d9tilov.android.regular.transaction.di;

import com.d9tilov.android.regular.transaction.data.contract.RegularTransactionSource;
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
public final class RegularTransactionDataModule_ProvideRegularTransactionRepoFactory implements Factory<RegularTransactionRepo> {
  private final Provider<RegularTransactionSource> regularTransactionSourceProvider;

  public RegularTransactionDataModule_ProvideRegularTransactionRepoFactory(
      Provider<RegularTransactionSource> regularTransactionSourceProvider) {
    this.regularTransactionSourceProvider = regularTransactionSourceProvider;
  }

  @Override
  public RegularTransactionRepo get() {
    return provideRegularTransactionRepo(regularTransactionSourceProvider.get());
  }

  public static RegularTransactionDataModule_ProvideRegularTransactionRepoFactory create(
      Provider<RegularTransactionSource> regularTransactionSourceProvider) {
    return new RegularTransactionDataModule_ProvideRegularTransactionRepoFactory(regularTransactionSourceProvider);
  }

  public static RegularTransactionRepo provideRegularTransactionRepo(
      RegularTransactionSource regularTransactionSource) {
    return Preconditions.checkNotNullFromProvides(RegularTransactionDataModule.INSTANCE.provideRegularTransactionRepo(regularTransactionSource));
  }
}
