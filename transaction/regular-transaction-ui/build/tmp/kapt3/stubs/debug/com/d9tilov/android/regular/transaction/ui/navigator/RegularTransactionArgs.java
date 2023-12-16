package com.d9tilov.android.regular.transaction.ui.navigator;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\b0\u0018\u00002\u00020\u0001:\u0002\u0003\u0004B\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0005"}, d2 = {"Lcom/d9tilov/android/regular/transaction/ui/navigator/RegularTransactionArgs;", "", "()V", "RegularTransactionCreationArgs", "RegularTransactionListArgs", "regular-transaction-ui_debug"})
public abstract class RegularTransactionArgs {
    
    private RegularTransactionArgs() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004B\u0015\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tR\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006\u000e"}, d2 = {"Lcom/d9tilov/android/regular/transaction/ui/navigator/RegularTransactionArgs$RegularTransactionCreationArgs;", "", "savedStateHandle", "Landroidx/lifecycle/SavedStateHandle;", "(Landroidx/lifecycle/SavedStateHandle;)V", "transactionType", "Lcom/d9tilov/android/core/model/TransactionType;", "transactionId", "", "(Lcom/d9tilov/android/core/model/TransactionType;J)V", "getTransactionId", "()J", "getTransactionType", "()Lcom/d9tilov/android/core/model/TransactionType;", "regular-transaction-ui_debug"})
    public static final class RegularTransactionCreationArgs {
        @org.jetbrains.annotations.NotNull
        private final com.d9tilov.android.core.model.TransactionType transactionType = null;
        private final long transactionId = 0L;
        
        public RegularTransactionCreationArgs(@org.jetbrains.annotations.NotNull
        com.d9tilov.android.core.model.TransactionType transactionType, long transactionId) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.d9tilov.android.core.model.TransactionType getTransactionType() {
            return null;
        }
        
        public final long getTransactionId() {
            return 0L;
        }
        
        public RegularTransactionCreationArgs(@org.jetbrains.annotations.NotNull
        androidx.lifecycle.SavedStateHandle savedStateHandle) {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004B\r\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t\u00a8\u0006\n"}, d2 = {"Lcom/d9tilov/android/regular/transaction/ui/navigator/RegularTransactionArgs$RegularTransactionListArgs;", "", "savedStateHandle", "Landroidx/lifecycle/SavedStateHandle;", "(Landroidx/lifecycle/SavedStateHandle;)V", "transactionType", "Lcom/d9tilov/android/core/model/TransactionType;", "(Lcom/d9tilov/android/core/model/TransactionType;)V", "getTransactionType", "()Lcom/d9tilov/android/core/model/TransactionType;", "regular-transaction-ui_debug"})
    public static final class RegularTransactionListArgs {
        @org.jetbrains.annotations.NotNull
        private final com.d9tilov.android.core.model.TransactionType transactionType = null;
        
        public RegularTransactionListArgs(@org.jetbrains.annotations.NotNull
        com.d9tilov.android.core.model.TransactionType transactionType) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.d9tilov.android.core.model.TransactionType getTransactionType() {
            return null;
        }
        
        public RegularTransactionListArgs(@org.jetbrains.annotations.NotNull
        androidx.lifecycle.SavedStateHandle savedStateHandle) {
            super();
        }
    }
}