package com.d9tilov.android.category.di;

@dagger.Module
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2 = {"Lcom/d9tilov/android/category/di/CategoryDomainModule;", "", "()V", "provideCategoryInteractor", "Lcom/d9tilov/android/category/domain/contract/CategoryInteractor;", "categoryRepo", "Lcom/d9tilov/android/category/domain/contract/CategoryRepo;", "category-di_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.android.components.ActivityRetainedComponent.class})
public final class CategoryDomainModule {
    @org.jetbrains.annotations.NotNull
    public static final com.d9tilov.android.category.di.CategoryDomainModule INSTANCE = null;
    
    private CategoryDomainModule() {
        super();
    }
    
    @dagger.Provides
    @dagger.hilt.android.scopes.ActivityRetainedScoped
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.category.domain.contract.CategoryInteractor provideCategoryInteractor(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.domain.contract.CategoryRepo categoryRepo) {
        return null;
    }
}