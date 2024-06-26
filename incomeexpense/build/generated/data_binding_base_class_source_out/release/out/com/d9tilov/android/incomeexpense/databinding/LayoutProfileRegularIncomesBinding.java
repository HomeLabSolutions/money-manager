// Generated by view binder compiler. Do not edit!
package com.d9tilov.android.incomeexpense.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.d9tilov.android.incomeexpense.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class LayoutProfileRegularIncomesBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView profileItemRegularIncomeSubtitle;

  @NonNull
  public final TextView profileItemRegularIncomeTitle;

  @NonNull
  public final View profileRegularIncomeSeparator;

  @NonNull
  public final ConstraintLayout profileRegularIncomesLayout;

  private LayoutProfileRegularIncomesBinding(@NonNull ConstraintLayout rootView,
      @NonNull TextView profileItemRegularIncomeSubtitle,
      @NonNull TextView profileItemRegularIncomeTitle, @NonNull View profileRegularIncomeSeparator,
      @NonNull ConstraintLayout profileRegularIncomesLayout) {
    this.rootView = rootView;
    this.profileItemRegularIncomeSubtitle = profileItemRegularIncomeSubtitle;
    this.profileItemRegularIncomeTitle = profileItemRegularIncomeTitle;
    this.profileRegularIncomeSeparator = profileRegularIncomeSeparator;
    this.profileRegularIncomesLayout = profileRegularIncomesLayout;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static LayoutProfileRegularIncomesBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static LayoutProfileRegularIncomesBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.layout_profile_regular_incomes, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static LayoutProfileRegularIncomesBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.profile_item_regular_income_subtitle;
      TextView profileItemRegularIncomeSubtitle = ViewBindings.findChildViewById(rootView, id);
      if (profileItemRegularIncomeSubtitle == null) {
        break missingId;
      }

      id = R.id.profile_item_regular_income_title;
      TextView profileItemRegularIncomeTitle = ViewBindings.findChildViewById(rootView, id);
      if (profileItemRegularIncomeTitle == null) {
        break missingId;
      }

      id = R.id.profile_regular_income_separator;
      View profileRegularIncomeSeparator = ViewBindings.findChildViewById(rootView, id);
      if (profileRegularIncomeSeparator == null) {
        break missingId;
      }

      ConstraintLayout profileRegularIncomesLayout = (ConstraintLayout) rootView;

      return new LayoutProfileRegularIncomesBinding((ConstraintLayout) rootView,
          profileItemRegularIncomeSubtitle, profileItemRegularIncomeTitle,
          profileRegularIncomeSeparator, profileRegularIncomesLayout);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
