package com.d9tilov.android.transaction.data.impl;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0019\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\tJ\u0011\u0010\n\u001a\u00020\u0006H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000bJ\u001f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\b0\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0010J4\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\r0\u00122\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u0017H\u0016J\u0019\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001cJ\u0016\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001e\u001a\u00020\u001fH\u0016J\u001c\u0010 \u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0!0\u00122\u0006\u0010\"\u001a\u00020#H\u0016J<\u0010$\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\r0\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00142\u0006\u0010\"\u001a\u00020#2\u0006\u0010%\u001a\u00020\u00172\u0006\u0010&\u001a\u00020\u0017H\u0016J\u0019\u0010\'\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\tJ\u0019\u0010(\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006)"}, d2 = {"Lcom/d9tilov/android/transaction/data/impl/TransactionDataRepo;", "Lcom/d9tilov/android/transaction/domain/contract/TransactionRepo;", "transactionSource", "Lcom/d9tilov/android/transaction/data/contract/TransactionSource;", "(Lcom/d9tilov/android/transaction/data/contract/TransactionSource;)V", "addTransaction", "", "transaction", "Lcom/d9tilov/android/transaction/domain/model/TransactionDataModel;", "(Lcom/d9tilov/android/transaction/domain/model/TransactionDataModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clearAll", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllByCategory", "", "category", "Lcom/d9tilov/android/category/domain/model/Category;", "(Lcom/d9tilov/android/category/domain/model/Category;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getByCategoryInPeriod", "Lkotlinx/coroutines/flow/Flow;", "from", "Lkotlinx/datetime/LocalDateTime;", "to", "inStatistics", "", "getCountByCurrencyCode", "", "code", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTransactionById", "id", "", "getTransactionsByType", "Landroidx/paging/PagingData;", "transactionType", "Lcom/d9tilov/android/core/model/TransactionType;", "getTransactionsByTypeInPeriod", "onlyInStatistics", "withRegular", "removeTransaction", "update", "transaction-data-impl_debug"})
public final class TransactionDataRepo implements com.d9tilov.android.transaction.domain.contract.TransactionRepo {
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.transaction.data.contract.TransactionSource transactionSource = null;
    
    public TransactionDataRepo(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.transaction.data.contract.TransactionSource transactionSource) {
        super();
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object addTransaction(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.transaction.domain.model.TransactionDataModel transaction, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public kotlinx.coroutines.flow.Flow<com.d9tilov.android.transaction.domain.model.TransactionDataModel> getTransactionById(long id) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<com.d9tilov.android.transaction.domain.model.TransactionDataModel>> getTransactionsByType(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.core.model.TransactionType transactionType) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public kotlinx.coroutines.flow.Flow<java.util.List<com.d9tilov.android.transaction.domain.model.TransactionDataModel>> getTransactionsByTypeInPeriod(@org.jetbrains.annotations.NotNull
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
    kotlinx.datetime.LocalDateTime to, boolean inStatistics) {
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
    public java.lang.Object removeTransaction(@org.jetbrains.annotations.NotNull
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
}