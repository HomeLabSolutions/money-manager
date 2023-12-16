package com.d9tilov.android.category.di;

@dagger.Module
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bg\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\'J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\'J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\'\u00a8\u0006\u000e"}, d2 = {"Lcom/d9tilov/android/category/di/CategoryDataModule;", "", "provideCategoryLocalSource", "Lcom/d9tilov/android/category/data/contract/CategorySource;", "categoryLocalSource", "Lcom/d9tilov/android/category/data/impl/CategoryLocalSource;", "provideCategoryRepo", "Lcom/d9tilov/android/category/domain/contract/CategoryRepo;", "categoryRepoImpl", "Lcom/d9tilov/android/category/data/impl/CategoryRepoImpl;", "provideDefaultCategoriesManager", "Lcom/d9tilov/android/category/data/contract/DefaultCategoriesManager;", "defaultCategoriesManager", "Lcom/d9tilov/android/category/data/impl/DefaultCategoriesManagerImpl;", "category-di_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.android.components.ActivityRetainedComponent.class})
public abstract interface CategoryDataModule {
    
    @dagger.Binds
    @dagger.hilt.android.scopes.ActivityRetainedScoped
    @org.jetbrains.annotations.NotNull
    public abstract com.d9tilov.android.category.data.contract.DefaultCategoriesManager provideDefaultCategoriesManager(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.data.impl.DefaultCategoriesManagerImpl defaultCategoriesManager);
    
    @dagger.Binds
    @dagger.hilt.android.scopes.ActivityRetainedScoped
    @org.jetbrains.annotations.NotNull
    public abstract com.d9tilov.android.category.data.contract.CategorySource provideCategoryLocalSource(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.data.impl.CategoryLocalSource categoryLocalSource);
    
    @dagger.Binds
    @dagger.hilt.android.scopes.ActivityRetainedScoped
    @org.jetbrains.annotations.NotNull
    public abstract com.d9tilov.android.category.domain.contract.CategoryRepo provideCategoryRepo(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.data.impl.CategoryRepoImpl categoryRepoImpl);
}