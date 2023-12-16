package com.d9tilov.android.incomeexpense.ui.vm;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\bv\u0018\u00002\u00020\u0001:\u0002\u0002\u0003\u0082\u0001\u0002\u0004\u0005\u00a8\u0006\u0006"}, d2 = {"Lcom/d9tilov/android/incomeexpense/ui/vm/TransactionSpendingTodayPrice;", "", "NORMAL", "OVERSPENDING", "Lcom/d9tilov/android/incomeexpense/ui/vm/TransactionSpendingTodayPrice$NORMAL;", "Lcom/d9tilov/android/incomeexpense/ui/vm/TransactionSpendingTodayPrice$OVERSPENDING;", "incomeexpense-ui_debug"})
public abstract interface TransactionSpendingTodayPrice {
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/d9tilov/android/incomeexpense/ui/vm/TransactionSpendingTodayPrice$NORMAL;", "Lcom/d9tilov/android/incomeexpense/ui/vm/TransactionSpendingTodayPrice;", "trSum", "Lcom/d9tilov/android/incomeexpense/ui/vm/Price;", "(Lcom/d9tilov/android/incomeexpense/ui/vm/Price;)V", "getTrSum", "()Lcom/d9tilov/android/incomeexpense/ui/vm/Price;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "incomeexpense-ui_debug"})
    public static final class NORMAL implements com.d9tilov.android.incomeexpense.ui.vm.TransactionSpendingTodayPrice {
        @org.jetbrains.annotations.NotNull
        private final com.d9tilov.android.incomeexpense.ui.vm.Price trSum = null;
        
        public NORMAL(@org.jetbrains.annotations.NotNull
        com.d9tilov.android.incomeexpense.ui.vm.Price trSum) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.d9tilov.android.incomeexpense.ui.vm.Price getTrSum() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.d9tilov.android.incomeexpense.ui.vm.Price component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.d9tilov.android.incomeexpense.ui.vm.TransactionSpendingTodayPrice.NORMAL copy(@org.jetbrains.annotations.NotNull
        com.d9tilov.android.incomeexpense.ui.vm.Price trSum) {
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
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/d9tilov/android/incomeexpense/ui/vm/TransactionSpendingTodayPrice$OVERSPENDING;", "Lcom/d9tilov/android/incomeexpense/ui/vm/TransactionSpendingTodayPrice;", "trSum", "Lcom/d9tilov/android/incomeexpense/ui/vm/Price;", "(Lcom/d9tilov/android/incomeexpense/ui/vm/Price;)V", "getTrSum", "()Lcom/d9tilov/android/incomeexpense/ui/vm/Price;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "incomeexpense-ui_debug"})
    public static final class OVERSPENDING implements com.d9tilov.android.incomeexpense.ui.vm.TransactionSpendingTodayPrice {
        @org.jetbrains.annotations.NotNull
        private final com.d9tilov.android.incomeexpense.ui.vm.Price trSum = null;
        
        public OVERSPENDING(@org.jetbrains.annotations.NotNull
        com.d9tilov.android.incomeexpense.ui.vm.Price trSum) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.d9tilov.android.incomeexpense.ui.vm.Price getTrSum() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.d9tilov.android.incomeexpense.ui.vm.Price component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.d9tilov.android.incomeexpense.ui.vm.TransactionSpendingTodayPrice.OVERSPENDING copy(@org.jetbrains.annotations.NotNull
        com.d9tilov.android.incomeexpense.ui.vm.Price trSum) {
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
    }
}