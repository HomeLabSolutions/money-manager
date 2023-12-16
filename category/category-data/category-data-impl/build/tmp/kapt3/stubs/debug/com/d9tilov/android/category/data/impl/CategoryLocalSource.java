package com.d9tilov.android.category.data.impl;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B)\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u0019\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000fJ\b\u0010\u0010\u001a\u00020\u0011H\u0002J\u0011\u0010\u0012\u001a\u00020\u0013H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0014J\u0011\u0010\u0015\u001a\u00020\u0013H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0014J\u0019\u0010\u0016\u001a\u00020\u00132\u0006\u0010\r\u001a\u00020\u000eH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000fJ\u0019\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u000eH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000fJ\u0019\u0010\u001a\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u000eH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000fJ\u0019\u0010\u001b\u001a\u00020\u000e2\u0006\u0010\u001c\u001a\u00020\fH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001dJ\u001c\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0 0\u001f2\u0006\u0010\u001c\u001a\u00020\fH\u0016J\u001c\u0010!\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0 0\u001f2\u0006\u0010\"\u001a\u00020#H\u0016J\u001c\u0010$\u001a\b\u0012\u0004\u0012\u00020\u000e0 2\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00110 H\u0002J\u0019\u0010&\u001a\u00020\u00132\u0006\u0010\r\u001a\u00020\u000eH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000fR\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\'"}, d2 = {"Lcom/d9tilov/android/category/data/impl/CategoryLocalSource;", "Lcom/d9tilov/android/category/data/contract/CategorySource;", "dispatcher", "Lkotlinx/coroutines/CoroutineDispatcher;", "preferencesStore", "Lcom/d9tilov/android/datastore/PreferencesStore;", "defaultCategoriesManager", "Lcom/d9tilov/android/category/data/contract/DefaultCategoriesManager;", "categoryDao", "Lcom/d9tilov/android/database/dao/CategoryDao;", "(Lkotlinx/coroutines/CoroutineDispatcher;Lcom/d9tilov/android/datastore/PreferencesStore;Lcom/d9tilov/android/category/data/contract/DefaultCategoriesManager;Lcom/d9tilov/android/database/dao/CategoryDao;)V", "create", "", "category", "Lcom/d9tilov/android/category/domain/model/Category;", "(Lcom/d9tilov/android/category/domain/model/Category;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createDummyModel", "Lcom/d9tilov/android/database/entity/CategoryDbModel;", "createExpenseDefaultCategories", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createIncomeDefaultCategories", "delete", "deleteFromGroup", "", "subCategory", "deleteSubcategory", "getById", "id", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getByParentId", "Lkotlinx/coroutines/flow/Flow;", "", "getCategoriesByType", "type", "Lcom/d9tilov/android/core/model/TransactionType;", "groupChildrenWithParent", "list", "update", "category-data-impl_debug"})
public final class CategoryLocalSource implements com.d9tilov.android.category.data.contract.CategorySource {
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.CoroutineDispatcher dispatcher = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.datastore.PreferencesStore preferencesStore = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.category.data.contract.DefaultCategoriesManager defaultCategoriesManager = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.database.dao.CategoryDao categoryDao = null;
    
    @javax.inject.Inject
    public CategoryLocalSource(@com.d9tilov.android.network.dispatchers.Dispatcher(moneyManagerDispatcher = com.d9tilov.android.network.dispatchers.MoneyManagerDispatchers.IO)
    @org.jetbrains.annotations.NotNull
    kotlinx.coroutines.CoroutineDispatcher dispatcher, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.datastore.PreferencesStore preferencesStore, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.data.contract.DefaultCategoriesManager defaultCategoriesManager, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.database.dao.CategoryDao categoryDao) {
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
    public java.lang.Object getById(long id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.d9tilov.android.category.domain.model.Category> $completion) {
        return null;
    }
    
    private final com.d9tilov.android.database.entity.CategoryDbModel createDummyModel() {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public kotlinx.coroutines.flow.Flow<java.util.List<com.d9tilov.android.category.domain.model.Category>> getByParentId(long id) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public kotlinx.coroutines.flow.Flow<java.util.List<com.d9tilov.android.category.domain.model.Category>> getCategoriesByType(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.core.model.TransactionType type) {
        return null;
    }
    
    private final java.util.List<com.d9tilov.android.category.domain.model.Category> groupChildrenWithParent(java.util.List<com.d9tilov.android.database.entity.CategoryDbModel> list) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object delete(@org.jetbrains.annotations.NotNull
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
}