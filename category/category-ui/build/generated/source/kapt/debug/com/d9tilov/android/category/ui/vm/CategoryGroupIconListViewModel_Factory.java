package com.d9tilov.android.category.ui.vm;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class CategoryGroupIconListViewModel_Factory implements Factory<CategoryGroupIconListViewModel> {
  @Override
  public CategoryGroupIconListViewModel get() {
    return newInstance();
  }

  public static CategoryGroupIconListViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CategoryGroupIconListViewModel newInstance() {
    return new CategoryGroupIconListViewModel();
  }

  private static final class InstanceHolder {
    private static final CategoryGroupIconListViewModel_Factory INSTANCE = new CategoryGroupIconListViewModel_Factory();
  }
}
