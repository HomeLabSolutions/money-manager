package com.d9tilov.android.category.data.impl;

import android.content.Context;
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
public final class DefaultCategoriesManagerImpl_Factory implements Factory<DefaultCategoriesManagerImpl> {
  private final Provider<Context> contextProvider;

  public DefaultCategoriesManagerImpl_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public DefaultCategoriesManagerImpl get() {
    return newInstance(contextProvider.get());
  }

  public static DefaultCategoriesManagerImpl_Factory create(Provider<Context> contextProvider) {
    return new DefaultCategoriesManagerImpl_Factory(contextProvider);
  }

  public static DefaultCategoriesManagerImpl newInstance(Context context) {
    return new DefaultCategoriesManagerImpl(context);
  }
}
