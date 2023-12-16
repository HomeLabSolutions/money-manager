package com.d9tilov.android.category.di;

import com.d9tilov.android.category.domain.contract.CategoryInteractor;
import com.d9tilov.android.category.domain.contract.CategoryRepo;
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
public final class CategoryDomainModule_ProvideCategoryInteractorFactory implements Factory<CategoryInteractor> {
  private final Provider<CategoryRepo> categoryRepoProvider;

  public CategoryDomainModule_ProvideCategoryInteractorFactory(
      Provider<CategoryRepo> categoryRepoProvider) {
    this.categoryRepoProvider = categoryRepoProvider;
  }

  @Override
  public CategoryInteractor get() {
    return provideCategoryInteractor(categoryRepoProvider.get());
  }

  public static CategoryDomainModule_ProvideCategoryInteractorFactory create(
      Provider<CategoryRepo> categoryRepoProvider) {
    return new CategoryDomainModule_ProvideCategoryInteractorFactory(categoryRepoProvider);
  }

  public static CategoryInteractor provideCategoryInteractor(CategoryRepo categoryRepo) {
    return Preconditions.checkNotNullFromProvides(CategoryDomainModule.INSTANCE.provideCategoryInteractor(categoryRepo));
  }
}
