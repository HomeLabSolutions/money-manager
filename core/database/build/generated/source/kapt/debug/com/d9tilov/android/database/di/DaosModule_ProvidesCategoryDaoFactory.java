package com.d9tilov.android.database.di;

import com.d9tilov.android.database.AppDatabase;
import com.d9tilov.android.database.dao.CategoryDao;
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
public final class DaosModule_ProvidesCategoryDaoFactory implements Factory<CategoryDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DaosModule_ProvidesCategoryDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public CategoryDao get() {
    return providesCategoryDao(databaseProvider.get());
  }

  public static DaosModule_ProvidesCategoryDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DaosModule_ProvidesCategoryDaoFactory(databaseProvider);
  }

  public static CategoryDao providesCategoryDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DaosModule.INSTANCE.providesCategoryDao(database));
  }
}
