package com.d9tilov.android.category.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000<\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000e\n\u0000\u001aN\u0010\u0000\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u0018\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00010\u00052\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\fH\u0007\u001aT\u0010\r\u001a\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u000f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\t2\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\tH\u0007\u001a\b\u0010\u0014\u001a\u00020\u0001H\u0007\u001a\u0018\u0010\u0015\u001a\u00020\n2\u0006\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0018H\u0002\u00a8\u0006\u0019"}, d2 = {"CategoryListRoute", "", "viewModel", "Lcom/d9tilov/android/category/ui/vm/CategoryListViewModel;", "openCategory", "Lkotlin/Function2;", "", "Lcom/d9tilov/android/core/model/TransactionType;", "onCategoryClickAndBack", "Lkotlin/Function1;", "Lcom/d9tilov/android/category/domain/model/Category;", "clickBack", "Lkotlin/Function0;", "CategoryListScreen", "uiState", "Lcom/d9tilov/android/category/ui/vm/CategoryUiState;", "onBackClicked", "onCreateClicked", "onCategoryClicked", "onRemoveClicked", "DefaultCategoryListPreview", "mockCategory", "id", "name", "", "category-ui_debug"})
public final class CategoryListScreenKt {
    
    @androidx.compose.runtime.Composable
    public static final void CategoryListRoute(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.ui.vm.CategoryListViewModel viewModel, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super java.lang.Long, ? super com.d9tilov.android.core.model.TransactionType, kotlin.Unit> openCategory, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.d9tilov.android.category.domain.model.Category, kotlin.Unit> onCategoryClickAndBack, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> clickBack) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class, androidx.compose.foundation.ExperimentalFoundationApi.class})
    @androidx.compose.runtime.Composable
    public static final void CategoryListScreen(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.ui.vm.CategoryUiState uiState, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onBackClicked, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onCreateClicked, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.d9tilov.android.category.domain.model.Category, kotlin.Unit> onCategoryClicked, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.d9tilov.android.category.domain.model.Category, kotlin.Unit> onRemoveClicked) {
    }
    
    @androidx.compose.ui.tooling.preview.Preview(showBackground = true)
    @androidx.compose.runtime.Composable
    public static final void DefaultCategoryListPreview() {
    }
    
    private static final com.d9tilov.android.category.domain.model.Category mockCategory(long id, java.lang.String name) {
        return null;
    }
}