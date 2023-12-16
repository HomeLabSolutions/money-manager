package com.d9tilov.android.regular.transaction.data.impl;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0019\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\tJ\u001c\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\f0\u000b2\u0006\u0010\r\u001a\u00020\u000eH\u0016J\u001f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\b0\f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0012J\u0019\u0010\u0013\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00020\u0015H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0016J\u0019\u0010\u0017\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\tJ\u0019\u0010\u0018\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0019"}, d2 = {"Lcom/d9tilov/android/regular/transaction/data/impl/RegularTransactionDataRepo;", "Lcom/d9tilov/android/regular/transaction/domain/contract/RegularTransactionRepo;", "regularTransactionSource", "Lcom/d9tilov/android/regular/transaction/data/contract/RegularTransactionSource;", "(Lcom/d9tilov/android/regular/transaction/data/contract/RegularTransactionSource;)V", "delete", "", "regularTransactionData", "Lcom/d9tilov/android/regular/transaction/domain/model/RegularTransactionData;", "(Lcom/d9tilov/android/regular/transaction/domain/model/RegularTransactionData;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAll", "Lkotlinx/coroutines/flow/Flow;", "", "type", "Lcom/d9tilov/android/core/model/TransactionType;", "getAllByCategory", "category", "Lcom/d9tilov/android/category/domain/model/Category;", "(Lcom/d9tilov/android/category/domain/model/Category;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getById", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insert", "update", "regular-transaction-data-impl_debug"})
public final class RegularTransactionDataRepo implements com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionRepo {
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.regular.transaction.data.contract.RegularTransactionSource regularTransactionSource = null;
    
    public RegularTransactionDataRepo(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.regular.transaction.data.contract.RegularTransactionSource regularTransactionSource) {
        super();
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object insert(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.regular.transaction.domain.model.RegularTransactionData regularTransactionData, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public kotlinx.coroutines.flow.Flow<java.util.List<com.d9tilov.android.regular.transaction.domain.model.RegularTransactionData>> getAll(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.core.model.TransactionType type) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object getAllByCategory(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.domain.model.Category category, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.d9tilov.android.regular.transaction.domain.model.RegularTransactionData>> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object getById(long id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.d9tilov.android.regular.transaction.domain.model.RegularTransactionData> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object update(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.regular.transaction.domain.model.RegularTransactionData regularTransactionData, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object delete(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.regular.transaction.domain.model.RegularTransactionData regularTransactionData, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}