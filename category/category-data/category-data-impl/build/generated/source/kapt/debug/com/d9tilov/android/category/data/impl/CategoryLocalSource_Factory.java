package com.d9tilov.android.category.data.impl;

import com.d9tilov.android.category.data.contract.DefaultCategoriesManager;
import com.d9tilov.android.database.dao.CategoryDao;
import com.d9tilov.android.datastore.PreferencesStore;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import kotlinx.coroutines.CoroutineDispatcher;

@ScopeMetadata
@QualifierMetadata("com.d9tilov.android.network.dispatchers.Dispatcher")
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
public final class CategoryLocalSource_Factory implements Factory<CategoryLocalSource> {
  private final Provider<CoroutineDispatcher> dispatcherProvider;

  private final Provider<PreferencesStore> preferencesStoreProvider;

  private final Provider<DefaultCategoriesManager> defaultCategoriesManagerProvider;

  private final Provider<CategoryDao> categoryDaoProvider;

  public CategoryLocalSource_Factory(Provider<CoroutineDispatcher> dispatcherProvider,
      Provider<PreferencesStore> preferencesStoreProvider,
      Provider<DefaultCategoriesManager> defaultCategoriesManagerProvider,
      Provider<CategoryDao> categoryDaoProvider) {
    this.dispatcherProvider = dispatcherProvider;
    this.preferencesStoreProvider = preferencesStoreProvider;
    this.defaultCategoriesManagerProvider = defaultCategoriesManagerProvider;
    this.categoryDaoProvider = categoryDaoProvider;
  }

  @Override
  public CategoryLocalSource get() {
    return newInstance(dispatcherProvider.get(), preferencesStoreProvider.get(), defaultCategoriesManagerProvider.get(), categoryDaoProvider.get());
  }

  public static CategoryLocalSource_Factory create(Provider<CoroutineDispatcher> dispatcherProvider,
      Provider<PreferencesStore> preferencesStoreProvider,
      Provider<DefaultCategoriesManager> defaultCategoriesManagerProvider,
      Provider<CategoryDao> categoryDaoProvider) {
    return new CategoryLocalSource_Factory(dispatcherProvider, preferencesStoreProvider, defaultCategoriesManagerProvider, categoryDaoProvider);
  }

  public static CategoryLocalSource newInstance(CoroutineDispatcher dispatcher,
      PreferencesStore preferencesStore, DefaultCategoriesManager defaultCategoriesManager,
      CategoryDao categoryDao) {
    return new CategoryLocalSource(dispatcher, preferencesStore, defaultCategoriesManager, categoryDao);
  }
}
