package com.d9tilov.android.category.ui.navigation;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0005\b0\u0018\u00002\u00020\u0001:\u0003\u0003\u0004\u0005B\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0006"}, d2 = {"Lcom/d9tilov/android/category/ui/navigation/CategoryArgs;", "", "()V", "CategoryCreationArgs", "CategoryIconsArgs", "CategoryListArgs", "category-ui_debug"})
public abstract class CategoryArgs {
    
    private CategoryArgs() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004B\u0015\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tR\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006\u000e"}, d2 = {"Lcom/d9tilov/android/category/ui/navigation/CategoryArgs$CategoryCreationArgs;", "", "savedStateHandle", "Landroidx/lifecycle/SavedStateHandle;", "(Landroidx/lifecycle/SavedStateHandle;)V", "categoryId", "", "transactionType", "Lcom/d9tilov/android/core/model/TransactionType;", "(JLcom/d9tilov/android/core/model/TransactionType;)V", "getCategoryId", "()J", "getTransactionType", "()Lcom/d9tilov/android/core/model/TransactionType;", "category-ui_debug"})
    public static final class CategoryCreationArgs {
        private final long categoryId = 0L;
        @org.jetbrains.annotations.NotNull
        private final com.d9tilov.android.core.model.TransactionType transactionType = null;
        
        public CategoryCreationArgs(long categoryId, @org.jetbrains.annotations.NotNull
        com.d9tilov.android.core.model.TransactionType transactionType) {
            super();
        }
        
        public final long getCategoryId() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.d9tilov.android.core.model.TransactionType getTransactionType() {
            return null;
        }
        
        public CategoryCreationArgs(@org.jetbrains.annotations.NotNull
        androidx.lifecycle.SavedStateHandle savedStateHandle) {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004B\r\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t\u00a8\u0006\n"}, d2 = {"Lcom/d9tilov/android/category/ui/navigation/CategoryArgs$CategoryIconsArgs;", "", "savedStateHandle", "Landroidx/lifecycle/SavedStateHandle;", "(Landroidx/lifecycle/SavedStateHandle;)V", "groupId", "Lcom/d9tilov/android/category/domain/model/CategoryGroup;", "(Lcom/d9tilov/android/category/domain/model/CategoryGroup;)V", "getGroupId", "()Lcom/d9tilov/android/category/domain/model/CategoryGroup;", "category-ui_debug"})
    public static final class CategoryIconsArgs {
        @org.jetbrains.annotations.NotNull
        private final com.d9tilov.android.category.domain.model.CategoryGroup groupId = null;
        
        public CategoryIconsArgs(@org.jetbrains.annotations.NotNull
        com.d9tilov.android.category.domain.model.CategoryGroup groupId) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.d9tilov.android.category.domain.model.CategoryGroup getGroupId() {
            return null;
        }
        
        public CategoryIconsArgs(@org.jetbrains.annotations.NotNull
        androidx.lifecycle.SavedStateHandle savedStateHandle) {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004B\u0017\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\u0002\u0010\tR\u0013\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006\u000e"}, d2 = {"Lcom/d9tilov/android/category/ui/navigation/CategoryArgs$CategoryListArgs;", "", "savedStateHandle", "Landroidx/lifecycle/SavedStateHandle;", "(Landroidx/lifecycle/SavedStateHandle;)V", "transactionType", "Lcom/d9tilov/android/core/model/TransactionType;", "destination", "Lcom/d9tilov/android/category/domain/model/CategoryDestination;", "(Lcom/d9tilov/android/core/model/TransactionType;Lcom/d9tilov/android/category/domain/model/CategoryDestination;)V", "getDestination", "()Lcom/d9tilov/android/category/domain/model/CategoryDestination;", "getTransactionType", "()Lcom/d9tilov/android/core/model/TransactionType;", "category-ui_debug"})
    public static final class CategoryListArgs {
        @org.jetbrains.annotations.NotNull
        private final com.d9tilov.android.core.model.TransactionType transactionType = null;
        @org.jetbrains.annotations.Nullable
        private final com.d9tilov.android.category.domain.model.CategoryDestination destination = null;
        
        public CategoryListArgs(@org.jetbrains.annotations.NotNull
        com.d9tilov.android.core.model.TransactionType transactionType, @org.jetbrains.annotations.Nullable
        com.d9tilov.android.category.domain.model.CategoryDestination destination) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.d9tilov.android.core.model.TransactionType getTransactionType() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final com.d9tilov.android.category.domain.model.CategoryDestination getDestination() {
            return null;
        }
        
        public CategoryListArgs(@org.jetbrains.annotations.NotNull
        androidx.lifecycle.SavedStateHandle savedStateHandle) {
            super();
        }
    }
}