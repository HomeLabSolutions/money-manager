package com.d9tilov.android.regular.transaction.ui.vm;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0086\b\u0018\u0000 \"2\u00020\u0001:\u0001\"B7\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\t\u0010\u0017\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\tH\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u000bH\u00c6\u0003J;\u0010\u001c\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000bH\u00c6\u0001J\u0013\u0010\u001d\u001a\u00020\u001e2\b\u0010\u001f\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010 \u001a\u00020\tH\u00d6\u0001J\t\u0010!\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016\u00a8\u0006#"}, d2 = {"Lcom/d9tilov/android/regular/transaction/ui/vm/RegularTransactionCreationUiState;", "", "amount", "", "curPeriodItem", "Lcom/d9tilov/android/regular/transaction/ui/vm/PeriodMenuItem;", "curDayInWeek", "Lcom/d9tilov/android/regular/transaction/ui/vm/DaysInWeek;", "curDayOfMonth", "", "transaction", "Lcom/d9tilov/android/regular/transaction/domain/model/RegularTransaction;", "(Ljava/lang/String;Lcom/d9tilov/android/regular/transaction/ui/vm/PeriodMenuItem;Lcom/d9tilov/android/regular/transaction/ui/vm/DaysInWeek;ILcom/d9tilov/android/regular/transaction/domain/model/RegularTransaction;)V", "getAmount", "()Ljava/lang/String;", "getCurDayInWeek", "()Lcom/d9tilov/android/regular/transaction/ui/vm/DaysInWeek;", "getCurDayOfMonth", "()I", "getCurPeriodItem", "()Lcom/d9tilov/android/regular/transaction/ui/vm/PeriodMenuItem;", "getTransaction", "()Lcom/d9tilov/android/regular/transaction/domain/model/RegularTransaction;", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "toString", "Companion", "regular-transaction-ui_debug"})
public final class RegularTransactionCreationUiState {
    @org.jetbrains.annotations.NotNull
    private final java.lang.String amount = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.regular.transaction.ui.vm.PeriodMenuItem curPeriodItem = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.regular.transaction.ui.vm.DaysInWeek curDayInWeek = null;
    private final int curDayOfMonth = 0;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.regular.transaction.domain.model.RegularTransaction transaction = null;
    @org.jetbrains.annotations.NotNull
    private static final com.d9tilov.android.regular.transaction.ui.vm.RegularTransactionCreationUiState EMPTY = null;
    @org.jetbrains.annotations.NotNull
    public static final com.d9tilov.android.regular.transaction.ui.vm.RegularTransactionCreationUiState.Companion Companion = null;
    
    public RegularTransactionCreationUiState(@org.jetbrains.annotations.NotNull
    java.lang.String amount, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.regular.transaction.ui.vm.PeriodMenuItem curPeriodItem, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.regular.transaction.ui.vm.DaysInWeek curDayInWeek, int curDayOfMonth, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.regular.transaction.domain.model.RegularTransaction transaction) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getAmount() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.regular.transaction.ui.vm.PeriodMenuItem getCurPeriodItem() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.regular.transaction.ui.vm.DaysInWeek getCurDayInWeek() {
        return null;
    }
    
    public final int getCurDayOfMonth() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.regular.transaction.domain.model.RegularTransaction getTransaction() {
        return null;
    }
    
    public RegularTransactionCreationUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.regular.transaction.ui.vm.PeriodMenuItem component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.regular.transaction.ui.vm.DaysInWeek component3() {
        return null;
    }
    
    public final int component4() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.regular.transaction.domain.model.RegularTransaction component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.regular.transaction.ui.vm.RegularTransactionCreationUiState copy(@org.jetbrains.annotations.NotNull
    java.lang.String amount, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.regular.transaction.ui.vm.PeriodMenuItem curPeriodItem, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.regular.transaction.ui.vm.DaysInWeek curDayInWeek, int curDayOfMonth, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.regular.transaction.domain.model.RegularTransaction transaction) {
        return null;
    }
    
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public java.lang.String toString() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/d9tilov/android/regular/transaction/ui/vm/RegularTransactionCreationUiState$Companion;", "", "()V", "EMPTY", "Lcom/d9tilov/android/regular/transaction/ui/vm/RegularTransactionCreationUiState;", "getEMPTY", "()Lcom/d9tilov/android/regular/transaction/ui/vm/RegularTransactionCreationUiState;", "regular-transaction-ui_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.d9tilov.android.regular.transaction.ui.vm.RegularTransactionCreationUiState getEMPTY() {
            return null;
        }
    }
}