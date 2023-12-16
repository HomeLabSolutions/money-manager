package com.d9tilov.android.transaction.ui.navigation;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\b0\u0018\u00002\u00020\u0001:\u0001\u0003B\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0004"}, d2 = {"Lcom/d9tilov/android/transaction/ui/navigation/TransactionArgs;", "", "()V", "TransactionCreationArgs", "transaction-ui_debug"})
public abstract class TransactionArgs {
    
    private TransactionArgs() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004B\r\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t\u00a8\u0006\n"}, d2 = {"Lcom/d9tilov/android/transaction/ui/navigation/TransactionArgs$TransactionCreationArgs;", "", "savedStateHandle", "Landroidx/lifecycle/SavedStateHandle;", "(Landroidx/lifecycle/SavedStateHandle;)V", "transactionId", "", "(J)V", "getTransactionId", "()J", "transaction-ui_debug"})
    public static final class TransactionCreationArgs {
        private final long transactionId = 0L;
        
        public TransactionCreationArgs(long transactionId) {
            super();
        }
        
        public final long getTransactionId() {
            return 0L;
        }
        
        public TransactionCreationArgs(@org.jetbrains.annotations.NotNull
        androidx.lifecycle.SavedStateHandle savedStateHandle) {
            super();
        }
    }
}