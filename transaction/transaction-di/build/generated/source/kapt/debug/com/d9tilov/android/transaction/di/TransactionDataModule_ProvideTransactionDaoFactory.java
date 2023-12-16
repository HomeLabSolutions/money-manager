package com.d9tilov.android.transaction.di;

import com.d9tilov.android.database.AppDatabase;
import com.d9tilov.android.database.dao.TransactionDao;
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
public final class TransactionDataModule_ProvideTransactionDaoFactory implements Factory<TransactionDao> {
  private final Provider<AppDatabase> appDatabaseProvider;

  public TransactionDataModule_ProvideTransactionDaoFactory(
      Provider<AppDatabase> appDatabaseProvider) {
    this.appDatabaseProvider = appDatabaseProvider;
  }

  @Override
  public TransactionDao get() {
    return provideTransactionDao(appDatabaseProvider.get());
  }

  public static TransactionDataModule_ProvideTransactionDaoFactory create(
      Provider<AppDatabase> appDatabaseProvider) {
    return new TransactionDataModule_ProvideTransactionDaoFactory(appDatabaseProvider);
  }

  public static TransactionDao provideTransactionDao(AppDatabase appDatabase) {
    return Preconditions.checkNotNullFromProvides(TransactionDataModule.INSTANCE.provideTransactionDao(appDatabase));
  }
}
