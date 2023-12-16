package com.d9tilov.android.common.android.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000N\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a \u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0002\u001a\u0018\u0010\b\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0002\u001a\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002\u001a(\u0010\r\u001a\u00020\u00052\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\fH\u0002\u001a\u0014\u0010\u0011\u001a\u00020\u0007*\u00020\u00032\u0006\u0010\u0012\u001a\u00020\nH\u0002\u001a2\u0010\u0013\u001a\u00020\u0001*\u00020\u00142\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\f0\u00162\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\f2\u0006\u0010\u0017\u001a\u00020\u0018H\u0002\u001a\"\u0010\u0019\u001a\u00020\u0001*\u00020\u00142\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\n0\u001b2\u0006\u0010\u0002\u001a\u00020\u0003H\u0002\u001a6\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001e0\u001d*\u00020\u00142\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\f0\u00162\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\f2\u0006\u0010\u0017\u001a\u00020\u0018\u00a8\u0006\u001f"}, d2 = {"attachNavHostFragment", "", "fragmentManager", "Landroidx/fragment/app/FragmentManager;", "navHostFragment", "Landroidx/navigation/fragment/NavHostFragment;", "isPrimaryNavFragment", "", "detachNavHostFragment", "getFragmentTag", "", "index", "", "obtainNavHostFragment", "fragmentTag", "navGraphId", "containerId", "isOnBackStack", "backStackName", "setupDeepLinks", "Lcom/google/android/material/bottomnavigation/BottomNavigationView;", "navGraphIds", "", "intent", "Landroid/content/Intent;", "setupItemReselected", "graphIdToTagMap", "Landroid/util/SparseArray;", "setupWithNavController", "Landroidx/lifecycle/LiveData;", "Landroidx/navigation/NavController;", "common-android_debug"})
public final class BottomNavigationViewExtensionsKt {
    
    /**
     * Manages the various graphs needed for a [BottomNavigationView].
     *
     * This sample is a workaround until the Navigation Component supports multiple back stacks.
     */
    @kotlin.Suppress(names = {"filename"})
    @org.jetbrains.annotations.NotNull
    public static final androidx.lifecycle.LiveData<androidx.navigation.NavController> setupWithNavController(@org.jetbrains.annotations.NotNull
    com.google.android.material.bottomnavigation.BottomNavigationView $this$setupWithNavController, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.Integer> navGraphIds, @org.jetbrains.annotations.NotNull
    androidx.fragment.app.FragmentManager fragmentManager, int containerId, @org.jetbrains.annotations.NotNull
    android.content.Intent intent) {
        return null;
    }
    
    private static final void setupDeepLinks(com.google.android.material.bottomnavigation.BottomNavigationView $this$setupDeepLinks, java.util.List<java.lang.Integer> navGraphIds, androidx.fragment.app.FragmentManager fragmentManager, int containerId, android.content.Intent intent) {
    }
    
    private static final void setupItemReselected(com.google.android.material.bottomnavigation.BottomNavigationView $this$setupItemReselected, android.util.SparseArray<java.lang.String> graphIdToTagMap, androidx.fragment.app.FragmentManager fragmentManager) {
    }
    
    private static final void detachNavHostFragment(androidx.fragment.app.FragmentManager fragmentManager, androidx.navigation.fragment.NavHostFragment navHostFragment) {
    }
    
    private static final void attachNavHostFragment(androidx.fragment.app.FragmentManager fragmentManager, androidx.navigation.fragment.NavHostFragment navHostFragment, boolean isPrimaryNavFragment) {
    }
    
    private static final androidx.navigation.fragment.NavHostFragment obtainNavHostFragment(androidx.fragment.app.FragmentManager fragmentManager, java.lang.String fragmentTag, int navGraphId, int containerId) {
        return null;
    }
    
    private static final boolean isOnBackStack(androidx.fragment.app.FragmentManager $this$isOnBackStack, java.lang.String backStackName) {
        return false;
    }
    
    private static final java.lang.String getFragmentTag(int index) {
        return null;
    }
}