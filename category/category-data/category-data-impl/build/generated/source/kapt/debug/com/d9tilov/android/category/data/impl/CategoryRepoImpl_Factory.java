package com.d9tilov.android.category.data.impl;

import com.d9tilov.android.category.data.contract.CategorySource;
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
public final class CategoryRepoImpl_Factory implements Factory<CategoryRepoImpl> {
  private final Provider<CategorySource> categoryLocalSourceProvider;

  public CategoryRepoImpl_Factory(Provider<CategorySource> categoryLocalSourceProvider) {
    this.categoryLocalSourceProvider = categoryLocalSourceProvider;
  }

  @Override
  public CategoryRepoImpl get() {
    return newInstance(categoryLocalSourceProvider.get());
  }

  public static CategoryRepoImpl_Factory create(
      Provider<CategorySource> categoryLocalSourceProvider) {
    return new CategoryRepoImpl_Factory(categoryLocalSourceProvider);
  }

  public static CategoryRepoImpl newInstance(CategorySource categoryLocalSource) {
    return new CategoryRepoImpl(categoryLocalSource);
  }
}
