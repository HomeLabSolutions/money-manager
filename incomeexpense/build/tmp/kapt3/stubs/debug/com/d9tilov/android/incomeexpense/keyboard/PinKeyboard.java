package com.d9tilov.android.incomeexpense.keyboard;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001:\u0001\u0011B\u001b\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010R\u001c\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\f\u00a8\u0006\u0012"}, d2 = {"Lcom/d9tilov/android/incomeexpense/keyboard/PinKeyboard;", "Landroid/widget/TableLayout;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "clickPinButton", "Lcom/d9tilov/android/incomeexpense/keyboard/PinKeyboard$ClickPinButton;", "getClickPinButton", "()Lcom/d9tilov/android/incomeexpense/keyboard/PinKeyboard$ClickPinButton;", "setClickPinButton", "(Lcom/d9tilov/android/incomeexpense/keyboard/PinKeyboard$ClickPinButton;)V", "fireClickPinButton", "", "button", "Lcom/d9tilov/android/incomeexpense/keyboard/PinButton;", "ClickPinButton", "incomeexpense_debug"})
public final class PinKeyboard extends android.widget.TableLayout {
    @org.jetbrains.annotations.Nullable
    private com.d9tilov.android.incomeexpense.keyboard.PinKeyboard.ClickPinButton clickPinButton;
    
    @kotlin.jvm.JvmOverloads
    public PinKeyboard(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super(null);
    }
    
    @kotlin.jvm.JvmOverloads
    public PinKeyboard(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.Nullable
    android.util.AttributeSet attrs) {
        super(null);
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.d9tilov.android.incomeexpense.keyboard.PinKeyboard.ClickPinButton getClickPinButton() {
        return null;
    }
    
    public final void setClickPinButton(@org.jetbrains.annotations.Nullable
    com.d9tilov.android.incomeexpense.keyboard.PinKeyboard.ClickPinButton p0) {
    }
    
    public final void fireClickPinButton(@org.jetbrains.annotations.Nullable
    com.d9tilov.android.incomeexpense.keyboard.PinButton button) {
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H&\u00a8\u0006\u0006"}, d2 = {"Lcom/d9tilov/android/incomeexpense/keyboard/PinKeyboard$ClickPinButton;", "", "onPinClick", "", "button", "Lcom/d9tilov/android/incomeexpense/keyboard/PinButton;", "incomeexpense_debug"})
    public static abstract interface ClickPinButton {
        
        public abstract void onPinClick(@org.jetbrains.annotations.Nullable
        com.d9tilov.android.incomeexpense.keyboard.PinButton button);
    }
}