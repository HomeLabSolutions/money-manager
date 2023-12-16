package com.d9tilov.android.category.data.impl;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J>\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000b2\b\b\u0001\u0010\f\u001a\u00020\r2\b\b\u0001\u0010\u000e\u001a\u00020\r2\b\b\u0001\u0010\u000f\u001a\u00020\rH\u0002J\u000e\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00060\u0011H\u0016J\u000e\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00060\u0011H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/d9tilov/android/category/data/impl/DefaultCategoriesManagerImpl;", "Lcom/d9tilov/android/category/data/contract/DefaultCategoriesManager;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "createCategory", "Lcom/d9tilov/android/category/domain/model/DefaultCategory;", "id", "", "parentId", "type", "Lcom/d9tilov/android/core/model/TransactionType;", "name", "", "icon", "color", "createDefaultExpenseCategories", "", "createDefaultIncomeCategories", "category-data-impl_debug"})
public final class DefaultCategoriesManagerImpl implements com.d9tilov.android.category.data.contract.DefaultCategoriesManager {
    @org.jetbrains.annotations.NotNull
    private final android.content.Context context = null;
    
    @javax.inject.Inject
    public DefaultCategoriesManagerImpl(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super();
    }
    
    @java.lang.Override
    @kotlin.Suppress(names = {"MagicNumber"})
    @org.jetbrains.annotations.NotNull
    public java.util.List<com.d9tilov.android.category.domain.model.DefaultCategory> createDefaultExpenseCategories() {
        return null;
    }
    
    @java.lang.Override
    @kotlin.Suppress(names = {"MagicNumber"})
    @org.jetbrains.annotations.NotNull
    public java.util.List<com.d9tilov.android.category.domain.model.DefaultCategory> createDefaultIncomeCategories() {
        return null;
    }
    
    private final com.d9tilov.android.category.domain.model.DefaultCategory createCategory(long id, long parentId, com.d9tilov.android.core.model.TransactionType type, @androidx.annotation.StringRes
    int name, @androidx.annotation.DrawableRes
    int icon, @androidx.annotation.ColorRes
    int color) {
        return null;
    }
}