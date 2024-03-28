// Generated by view binder compiler. Do not edit!
package com.d9tilov.android.incomeexpense.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.d9tilov.android.incomeexpense.R;
import com.d9tilov.android.incomeexpense.keyboard.PinButton;
import com.d9tilov.android.incomeexpense.keyboard.PinKeyboard;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class KeyboardLayoutBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageView btnHideKeyboard;

  @NonNull
  public final PinButton keyboardPin0;

  @NonNull
  public final PinButton keyboardPin1;

  @NonNull
  public final PinButton keyboardPin2;

  @NonNull
  public final PinButton keyboardPin3;

  @NonNull
  public final PinButton keyboardPin4;

  @NonNull
  public final PinButton keyboardPin5;

  @NonNull
  public final PinButton keyboardPin6;

  @NonNull
  public final PinButton keyboardPin7;

  @NonNull
  public final PinButton keyboardPin8;

  @NonNull
  public final PinButton keyboardPin9;

  @NonNull
  public final PinButton keyboardPinDot;

  @NonNull
  public final PinButton keyboardPinRemove;

  @NonNull
  public final PinKeyboard pinKeyboard;

  private KeyboardLayoutBinding(@NonNull ConstraintLayout rootView,
      @NonNull ImageView btnHideKeyboard, @NonNull PinButton keyboardPin0,
      @NonNull PinButton keyboardPin1, @NonNull PinButton keyboardPin2,
      @NonNull PinButton keyboardPin3, @NonNull PinButton keyboardPin4,
      @NonNull PinButton keyboardPin5, @NonNull PinButton keyboardPin6,
      @NonNull PinButton keyboardPin7, @NonNull PinButton keyboardPin8,
      @NonNull PinButton keyboardPin9, @NonNull PinButton keyboardPinDot,
      @NonNull PinButton keyboardPinRemove, @NonNull PinKeyboard pinKeyboard) {
    this.rootView = rootView;
    this.btnHideKeyboard = btnHideKeyboard;
    this.keyboardPin0 = keyboardPin0;
    this.keyboardPin1 = keyboardPin1;
    this.keyboardPin2 = keyboardPin2;
    this.keyboardPin3 = keyboardPin3;
    this.keyboardPin4 = keyboardPin4;
    this.keyboardPin5 = keyboardPin5;
    this.keyboardPin6 = keyboardPin6;
    this.keyboardPin7 = keyboardPin7;
    this.keyboardPin8 = keyboardPin8;
    this.keyboardPin9 = keyboardPin9;
    this.keyboardPinDot = keyboardPinDot;
    this.keyboardPinRemove = keyboardPinRemove;
    this.pinKeyboard = pinKeyboard;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static KeyboardLayoutBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static KeyboardLayoutBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.keyboard_layout, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static KeyboardLayoutBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_hide_keyboard;
      ImageView btnHideKeyboard = ViewBindings.findChildViewById(rootView, id);
      if (btnHideKeyboard == null) {
        break missingId;
      }

      id = R.id.keyboard_pin_0;
      PinButton keyboardPin0 = ViewBindings.findChildViewById(rootView, id);
      if (keyboardPin0 == null) {
        break missingId;
      }

      id = R.id.keyboard_pin_1;
      PinButton keyboardPin1 = ViewBindings.findChildViewById(rootView, id);
      if (keyboardPin1 == null) {
        break missingId;
      }

      id = R.id.keyboard_pin_2;
      PinButton keyboardPin2 = ViewBindings.findChildViewById(rootView, id);
      if (keyboardPin2 == null) {
        break missingId;
      }

      id = R.id.keyboard_pin_3;
      PinButton keyboardPin3 = ViewBindings.findChildViewById(rootView, id);
      if (keyboardPin3 == null) {
        break missingId;
      }

      id = R.id.keyboard_pin_4;
      PinButton keyboardPin4 = ViewBindings.findChildViewById(rootView, id);
      if (keyboardPin4 == null) {
        break missingId;
      }

      id = R.id.keyboard_pin_5;
      PinButton keyboardPin5 = ViewBindings.findChildViewById(rootView, id);
      if (keyboardPin5 == null) {
        break missingId;
      }

      id = R.id.keyboard_pin_6;
      PinButton keyboardPin6 = ViewBindings.findChildViewById(rootView, id);
      if (keyboardPin6 == null) {
        break missingId;
      }

      id = R.id.keyboard_pin_7;
      PinButton keyboardPin7 = ViewBindings.findChildViewById(rootView, id);
      if (keyboardPin7 == null) {
        break missingId;
      }

      id = R.id.keyboard_pin_8;
      PinButton keyboardPin8 = ViewBindings.findChildViewById(rootView, id);
      if (keyboardPin8 == null) {
        break missingId;
      }

      id = R.id.keyboard_pin_9;
      PinButton keyboardPin9 = ViewBindings.findChildViewById(rootView, id);
      if (keyboardPin9 == null) {
        break missingId;
      }

      id = R.id.keyboard_pin_dot;
      PinButton keyboardPinDot = ViewBindings.findChildViewById(rootView, id);
      if (keyboardPinDot == null) {
        break missingId;
      }

      id = R.id.keyboard_pin_remove;
      PinButton keyboardPinRemove = ViewBindings.findChildViewById(rootView, id);
      if (keyboardPinRemove == null) {
        break missingId;
      }

      id = R.id.pin_keyboard;
      PinKeyboard pinKeyboard = ViewBindings.findChildViewById(rootView, id);
      if (pinKeyboard == null) {
        break missingId;
      }

      return new KeyboardLayoutBinding((ConstraintLayout) rootView, btnHideKeyboard, keyboardPin0,
          keyboardPin1, keyboardPin2, keyboardPin3, keyboardPin4, keyboardPin5, keyboardPin6,
          keyboardPin7, keyboardPin8, keyboardPin9, keyboardPinDot, keyboardPinRemove, pinKeyboard);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
