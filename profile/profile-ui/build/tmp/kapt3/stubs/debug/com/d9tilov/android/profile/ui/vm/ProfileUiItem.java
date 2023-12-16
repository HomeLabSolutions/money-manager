package com.d9tilov.android.profile.ui.vm;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0006\u0003\u0004\u0005\u0006\u0007\bB\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0006\t\n\u000b\f\r\u000e\u00a8\u0006\u000f"}, d2 = {"Lcom/d9tilov/android/profile/ui/vm/ProfileUiItem;", "", "()V", "BudgetUiItem", "CurrencyUiItem", "Goals", "RegularExpenseUiItem", "RegularIncomeUiItem", "Settings", "Lcom/d9tilov/android/profile/ui/vm/ProfileUiItem$BudgetUiItem;", "Lcom/d9tilov/android/profile/ui/vm/ProfileUiItem$CurrencyUiItem;", "Lcom/d9tilov/android/profile/ui/vm/ProfileUiItem$Goals;", "Lcom/d9tilov/android/profile/ui/vm/ProfileUiItem$RegularExpenseUiItem;", "Lcom/d9tilov/android/profile/ui/vm/ProfileUiItem$RegularIncomeUiItem;", "Lcom/d9tilov/android/profile/ui/vm/ProfileUiItem$Settings;", "profile-ui_debug"})
public abstract class ProfileUiItem {
    
    private ProfileUiItem() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u000f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/d9tilov/android/profile/ui/vm/ProfileUiItem$BudgetUiItem;", "Lcom/d9tilov/android/profile/ui/vm/ProfileUiItem;", "budgetData", "Lcom/d9tilov/android/budget/domain/model/BudgetData;", "(Lcom/d9tilov/android/budget/domain/model/BudgetData;)V", "getBudgetData", "()Lcom/d9tilov/android/budget/domain/model/BudgetData;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "profile-ui_debug"})
    public static final class BudgetUiItem extends com.d9tilov.android.profile.ui.vm.ProfileUiItem {
        @org.jetbrains.annotations.NotNull
        private final com.d9tilov.android.budget.domain.model.BudgetData budgetData = null;
        
        public BudgetUiItem(@org.jetbrains.annotations.NotNull
        com.d9tilov.android.budget.domain.model.BudgetData budgetData) {
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.d9tilov.android.budget.domain.model.BudgetData getBudgetData() {
            return null;
        }
        
        public BudgetUiItem() {
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.d9tilov.android.budget.domain.model.BudgetData component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.d9tilov.android.profile.ui.vm.ProfileUiItem.BudgetUiItem copy(@org.jetbrains.annotations.NotNull
        com.d9tilov.android.budget.domain.model.BudgetData budgetData) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u000f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lcom/d9tilov/android/profile/ui/vm/ProfileUiItem$CurrencyUiItem;", "Lcom/d9tilov/android/profile/ui/vm/ProfileUiItem;", "currencyCode", "", "(Ljava/lang/String;)V", "getCurrencyCode", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "profile-ui_debug"})
    public static final class CurrencyUiItem extends com.d9tilov.android.profile.ui.vm.ProfileUiItem {
        @org.jetbrains.annotations.NotNull
        private final java.lang.String currencyCode = null;
        
        public CurrencyUiItem(@org.jetbrains.annotations.NotNull
        java.lang.String currencyCode) {
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getCurrencyCode() {
            return null;
        }
        
        public CurrencyUiItem() {
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.d9tilov.android.profile.ui.vm.ProfileUiItem.CurrencyUiItem copy(@org.jetbrains.annotations.NotNull
        java.lang.String currencyCode) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/d9tilov/android/profile/ui/vm/ProfileUiItem$Goals;", "Lcom/d9tilov/android/profile/ui/vm/ProfileUiItem;", "()V", "profile-ui_debug"})
    public static final class Goals extends com.d9tilov.android.profile.ui.vm.ProfileUiItem {
        @org.jetbrains.annotations.NotNull
        public static final com.d9tilov.android.profile.ui.vm.ProfileUiItem.Goals INSTANCE = null;
        
        private Goals() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0005J\u000f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\u0019\u0010\t\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0001J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u00d6\u0003J\t\u0010\u000e\u001a\u00020\u000fH\u00d6\u0001J\t\u0010\u0010\u001a\u00020\u0011H\u00d6\u0001R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\u0012"}, d2 = {"Lcom/d9tilov/android/profile/ui/vm/ProfileUiItem$RegularExpenseUiItem;", "Lcom/d9tilov/android/profile/ui/vm/ProfileUiItem;", "regularExpenses", "", "Lcom/d9tilov/android/regular/transaction/domain/model/RegularTransaction;", "(Ljava/util/List;)V", "getRegularExpenses", "()Ljava/util/List;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "profile-ui_debug"})
    public static final class RegularExpenseUiItem extends com.d9tilov.android.profile.ui.vm.ProfileUiItem {
        @org.jetbrains.annotations.NotNull
        private final java.util.List<com.d9tilov.android.regular.transaction.domain.model.RegularTransaction> regularExpenses = null;
        
        public RegularExpenseUiItem(@org.jetbrains.annotations.NotNull
        java.util.List<com.d9tilov.android.regular.transaction.domain.model.RegularTransaction> regularExpenses) {
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.util.List<com.d9tilov.android.regular.transaction.domain.model.RegularTransaction> getRegularExpenses() {
            return null;
        }
        
        public RegularExpenseUiItem() {
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.util.List<com.d9tilov.android.regular.transaction.domain.model.RegularTransaction> component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.d9tilov.android.profile.ui.vm.ProfileUiItem.RegularExpenseUiItem copy(@org.jetbrains.annotations.NotNull
        java.util.List<com.d9tilov.android.regular.transaction.domain.model.RegularTransaction> regularExpenses) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0005J\u000f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\u0019\u0010\t\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0001J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u00d6\u0003J\t\u0010\u000e\u001a\u00020\u000fH\u00d6\u0001J\t\u0010\u0010\u001a\u00020\u0011H\u00d6\u0001R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\u0012"}, d2 = {"Lcom/d9tilov/android/profile/ui/vm/ProfileUiItem$RegularIncomeUiItem;", "Lcom/d9tilov/android/profile/ui/vm/ProfileUiItem;", "regularIncomes", "", "Lcom/d9tilov/android/regular/transaction/domain/model/RegularTransaction;", "(Ljava/util/List;)V", "getRegularIncomes", "()Ljava/util/List;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "profile-ui_debug"})
    public static final class RegularIncomeUiItem extends com.d9tilov.android.profile.ui.vm.ProfileUiItem {
        @org.jetbrains.annotations.NotNull
        private final java.util.List<com.d9tilov.android.regular.transaction.domain.model.RegularTransaction> regularIncomes = null;
        
        public RegularIncomeUiItem(@org.jetbrains.annotations.NotNull
        java.util.List<com.d9tilov.android.regular.transaction.domain.model.RegularTransaction> regularIncomes) {
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.util.List<com.d9tilov.android.regular.transaction.domain.model.RegularTransaction> getRegularIncomes() {
            return null;
        }
        
        public RegularIncomeUiItem() {
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.util.List<com.d9tilov.android.regular.transaction.domain.model.RegularTransaction> component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.d9tilov.android.profile.ui.vm.ProfileUiItem.RegularIncomeUiItem copy(@org.jetbrains.annotations.NotNull
        java.util.List<com.d9tilov.android.regular.transaction.domain.model.RegularTransaction> regularIncomes) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u000f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0006\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\u0007\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\b\u001a\u00020\u00032\b\u0010\t\u001a\u0004\u0018\u00010\nH\u00d6\u0003J\t\u0010\u000b\u001a\u00020\fH\u00d6\u0001J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0005\u00a8\u0006\u000f"}, d2 = {"Lcom/d9tilov/android/profile/ui/vm/ProfileUiItem$Settings;", "Lcom/d9tilov/android/profile/ui/vm/ProfileUiItem;", "isPremium", "", "(Z)V", "()Z", "component1", "copy", "equals", "other", "", "hashCode", "", "toString", "", "profile-ui_debug"})
    public static final class Settings extends com.d9tilov.android.profile.ui.vm.ProfileUiItem {
        private final boolean isPremium = false;
        
        public Settings(boolean isPremium) {
        }
        
        public final boolean isPremium() {
            return false;
        }
        
        public Settings() {
        }
        
        public final boolean component1() {
            return false;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.d9tilov.android.profile.ui.vm.ProfileUiItem.Settings copy(boolean isPremium) {
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