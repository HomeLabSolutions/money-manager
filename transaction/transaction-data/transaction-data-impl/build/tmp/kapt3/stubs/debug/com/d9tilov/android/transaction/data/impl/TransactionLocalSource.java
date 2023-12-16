package com.d9tilov.android.transaction.data.impl;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\u0018\u0000 ,2\u00020\u0001:\u0001,B\u001f\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0011\u0010\t\u001a\u00020\nH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000bJ\u001f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r2\u0006\u0010\u000f\u001a\u00020\u0010H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J<\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\r0\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001aH\u0016J\u001c\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u001d0\u00132\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J4\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\r0\u00132\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0019\u001a\u00020\u001aH\u0016J\u0016\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00132\u0006\u0010 \u001a\u00020!H\u0016J\u0019\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020%H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010&J\u0019\u0010\'\u001a\u00020\n2\u0006\u0010(\u001a\u00020\u000eH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010)J\u0019\u0010*\u001a\u00020\n2\u0006\u0010(\u001a\u00020\u000eH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010)J\u0019\u0010+\u001a\u00020\n2\u0006\u0010(\u001a\u00020\u000eH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010)R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006-"}, d2 = {"Lcom/d9tilov/android/transaction/data/impl/TransactionLocalSource;", "Lcom/d9tilov/android/transaction/data/contract/TransactionSource;", "dispatcher", "Lkotlinx/coroutines/CoroutineDispatcher;", "preferencesStore", "Lcom/d9tilov/android/datastore/PreferencesStore;", "transactionDao", "Lcom/d9tilov/android/database/dao/TransactionDao;", "(Lkotlinx/coroutines/CoroutineDispatcher;Lcom/d9tilov/android/datastore/PreferencesStore;Lcom/d9tilov/android/database/dao/TransactionDao;)V", "clearAll", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllByCategory", "", "Lcom/d9tilov/android/transaction/domain/model/TransactionDataModel;", "category", "Lcom/d9tilov/android/category/domain/model/Category;", "(Lcom/d9tilov/android/category/domain/model/Category;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllByTypeInPeriod", "Lkotlinx/coroutines/flow/Flow;", "from", "Lkotlinx/datetime/LocalDateTime;", "to", "transactionType", "Lcom/d9tilov/android/core/model/TransactionType;", "onlyInStatistics", "", "withRegular", "getAllByTypePaging", "Landroidx/paging/PagingData;", "getByCategoryInPeriod", "getById", "id", "", "getCountByCurrencyCode", "", "code", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insert", "transaction", "(Lcom/d9tilov/android/transaction/domain/model/TransactionDataModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "remove", "update", "Companion", "transaction-data-impl_debug"})
public final class TransactionLocalSource implements com.d9tilov.android.transaction.data.contract.TransactionSource {
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.CoroutineDispatcher dispatcher = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.datastore.PreferencesStore preferencesStore = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.database.dao.TransactionDao transactionDao = null;
    private static final int PAGE_SIZE = 10;
    @org.jetbrains.annotations.NotNull
    public static final com.d9tilov.android.transaction.data.impl.TransactionLocalSource.Companion Companion = null;
    
    public TransactionLocalSource(@com.d9tilov.android.network.dispatchers.Dispatcher(moneyManagerDispatcher = com.d9tilov.android.network.dispatchers.MoneyManagerDispatchers.IO)
    @org.jetbrains.annotations.NotNull
    kotlinx.coroutines.CoroutineDispatcher dispatcher, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.datastore.PreferencesStore preferencesStore, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.database.dao.TransactionDao transactionDao) {
        super();
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object insert(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.transaction.domain.model.TransactionDataModel transaction, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public kotlinx.coroutines.flow.Flow<com.d9tilov.android.transaction.domain.model.TransactionDataModel> getById(long id) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<com.d9tilov.android.transaction.domain.model.TransactionDataModel>> getAllByTypePaging(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.core.model.TransactionType transactionType) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public kotlinx.coroutines.flow.Flow<java.util.List<com.d9tilov.android.transaction.domain.model.TransactionDataModel>> getAllByTypeInPeriod(@org.jetbrains.annotations.NotNull
    kotlinx.datetime.LocalDateTime from, @org.jetbrains.annotations.NotNull
    kotlinx.datetime.LocalDateTime to, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.core.model.TransactionType transactionType, boolean onlyInStatistics, boolean withRegular) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object getAllByCategory(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.domain.model.Category category, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.d9tilov.android.transaction.domain.model.TransactionDataModel>> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public kotlinx.coroutines.flow.Flow<java.util.List<com.d9tilov.android.transaction.domain.model.TransactionDataModel>> getByCategoryInPeriod(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.domain.model.Category category, @org.jetbrains.annotations.NotNull
    kotlinx.datetime.LocalDateTime from, @org.jetbrains.annotations.NotNull
    kotlinx.datetime.LocalDateTime to, boolean onlyInStatistics) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object getCountByCurrencyCode(@org.jetbrains.annotations.NotNull
    java.lang.String code, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object update(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.transaction.domain.model.TransactionDataModel transaction, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object remove(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.transaction.domain.model.TransactionDataModel transaction, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object clearAll(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/d9tilov/android/transaction/data/impl/TransactionLocalSource$Companion;", "", "()V", "PAGE_SIZE", "", "transaction-data-impl_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}