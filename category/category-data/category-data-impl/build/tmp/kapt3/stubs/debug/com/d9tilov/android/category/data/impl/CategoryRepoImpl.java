package com.d9tilov.android.category.data.impl;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0019\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\tJ\u0016\u0010\n\u001a\u00020\b2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\b0\fH\u0016J\u0011\u0010\r\u001a\u00020\u000eH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000fJ\u0011\u0010\u0010\u001a\u00020\u000eH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000fJ\u0019\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0007\u001a\u00020\bH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\tJ\u0019\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\bH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\tJ\u0019\u0010\u0015\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\bH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\tJ\u001c\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\f0\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J\u0019\u0010\u001a\u001a\u00020\b2\u0006\u0010\u001b\u001a\u00020\u0006H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001cJ\u001c\u0010\u001d\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\f0\u00172\u0006\u0010\u001e\u001a\u00020\bH\u0016J\u0019\u0010\u001f\u001a\u00020\u000e2\u0006\u0010\u0007\u001a\u00020\bH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006 "}, d2 = {"Lcom/d9tilov/android/category/data/impl/CategoryRepoImpl;", "Lcom/d9tilov/android/category/domain/contract/CategoryRepo;", "categoryLocalSource", "Lcom/d9tilov/android/category/data/contract/CategorySource;", "(Lcom/d9tilov/android/category/data/contract/CategorySource;)V", "create", "", "category", "Lcom/d9tilov/android/category/domain/model/Category;", "(Lcom/d9tilov/android/category/domain/model/Category;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createAllItemsFolder", "categories", "", "createExpenseDefaultCategories", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createIncomeDefaultCategories", "deleteCategory", "deleteFromGroup", "", "subCategory", "deleteSubcategory", "getCategoriesByType", "Lkotlinx/coroutines/flow/Flow;", "type", "Lcom/d9tilov/android/core/model/TransactionType;", "getCategoryById", "id", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getChildrenByParent", "parentCategory", "update", "category-data-impl_debug"})
public final class CategoryRepoImpl implements com.d9tilov.android.category.domain.contract.CategoryRepo {
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.category.data.contract.CategorySource categoryLocalSource = null;
    
    @javax.inject.Inject
    public CategoryRepoImpl(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.data.contract.CategorySource categoryLocalSource) {
        super();
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object createExpenseDefaultCategories(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object createIncomeDefaultCategories(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object create(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.domain.model.Category category, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object update(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.domain.model.Category category, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object getCategoryById(long id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.d9tilov.android.category.domain.model.Category> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public kotlinx.coroutines.flow.Flow<java.util.List<com.d9tilov.android.category.domain.model.Category>> getCategoriesByType(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.core.model.TransactionType type) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public kotlinx.coroutines.flow.Flow<java.util.List<com.d9tilov.android.category.domain.model.Category>> getChildrenByParent(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.domain.model.Category parentCategory) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object deleteCategory(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.domain.model.Category category, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object deleteSubcategory(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.domain.model.Category subCategory, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object deleteFromGroup(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.domain.model.Category subCategory, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public com.d9tilov.android.category.domain.model.Category createAllItemsFolder(@org.jetbrains.annotations.NotNull
    java.util.List<com.d9tilov.android.category.domain.model.Category> categories) {
        return null;
    }
}