package com.d9tilov.android.transaction.di;

import com.d9tilov.android.transaction.data.contract.TransactionSource;
import com.d9tilov.android.transaction.domain.contract.TransactionRepo;
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
public final class TransactionDataModule_ProvideTransactionRepoFactory implements Factory<TransactionRepo> {
  private final Provider<TransactionSource> transactionSourceProvider;

  public TransactionDataModule_ProvideTransactionRepoFactory(
      Provider<TransactionSource> transactionSourceProvider) {
    this.transactionSourceProvider = transactionSourceProvider;
  }

  @Override
  public TransactionRepo get() {
    return provideTransactionRepo(transactionSourceProvider.get());
  }

  public static TransactionDataModule_ProvideTransactionRepoFactory create(
      Provider<TransactionSource> transactionSourceProvider) {
    return new TransactionDataModule_ProvideTransactionRepoFactory(transactionSourceProvider);
  }

  public static TransactionRepo provideTransactionRepo(TransactionSource transactionSource) {
    return Preconditions.checkNotNullFromProvides(TransactionDataModule.INSTANCE.provideTransactionRepo(transactionSource));
  }
}
