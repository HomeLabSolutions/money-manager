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
import com.d9tilov.android.designsystem.CurrencyView;
import com.d9tilov.android.incomeexpense.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class LayoutExpenseInfoBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView expenseCanSpendTodayInfoTitle;

  @NonNull
  public final TextView expensePeriodInfoApproxSign;

  @NonNull
  public final CurrencyView expensePeriodInfoApproxSum;

  @NonNull
  public final TextView expensePeriodInfoTitle;

  @NonNull
  public final TextView expenseTodayInfoApproxSign;

  @NonNull
  public final CurrencyView expenseTodayInfoApproxSum;

  @NonNull
  public final TextView expenseTodayInfoTitle;

  @NonNull
  public final CurrencyView expenseTodayInfoValue;

  private LayoutExpenseInfoBinding(@NonNull ConstraintLayout rootView,
      @NonNull TextView expenseCanSpendTodayInfoTitle,
      @NonNull TextView expensePeriodInfoApproxSign,
      @NonNull CurrencyView expensePeriodInfoApproxSum, @NonNull TextView expensePeriodInfoTitle,
      @NonNull TextView expenseTodayInfoApproxSign, @NonNull CurrencyView expenseTodayInfoApproxSum,
      @NonNull TextView expenseTodayInfoTitle, @NonNull CurrencyView expenseTodayInfoValue) {
    this.rootView = rootView;
    this.expenseCanSpendTodayInfoTitle = expenseCanSpendTodayInfoTitle;
    this.expensePeriodInfoApproxSign = expensePeriodInfoApproxSign;
    this.expensePeriodInfoApproxSum = expensePeriodInfoApproxSum;
    this.expensePeriodInfoTitle = expensePeriodInfoTitle;
    this.expenseTodayInfoApproxSign = expenseTodayInfoApproxSign;
    this.expenseTodayInfoApproxSum = expenseTodayInfoApproxSum;
    this.expenseTodayInfoTitle = expenseTodayInfoTitle;
    this.expenseTodayInfoValue = expenseTodayInfoValue;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static LayoutExpenseInfoBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static LayoutExpenseInfoBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.layout_expense_info, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static LayoutExpenseInfoBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.expense_can_spend_today_info_title;
      TextView expenseCanSpendTodayInfoTitle = ViewBindings.findChildViewById(rootView, id);
      if (expenseCanSpendTodayInfoTitle == null) {
        break missingId;
      }

      id = R.id.expense_period_info_approx_sign;
      TextView expensePeriodInfoApproxSign = ViewBindings.findChildViewById(rootView, id);
      if (expensePeriodInfoApproxSign == null) {
        break missingId;
      }

      id = R.id.expense_period_info_approx_sum;
      CurrencyView expensePeriodInfoApproxSum = ViewBindings.findChildViewById(rootView, id);
      if (expensePeriodInfoApproxSum == null) {
        break missingId;
      }

      id = R.id.expense_period_info_title;
      TextView expensePeriodInfoTitle = ViewBindings.findChildViewById(rootView, id);
      if (expensePeriodInfoTitle == null) {
        break missingId;
      }

      id = R.id.expense_today_info_approx_sign;
      TextView expenseTodayInfoApproxSign = ViewBindings.findChildViewById(rootView, id);
      if (expenseTodayInfoApproxSign == null) {
        break missingId;
      }

      id = R.id.expense_today_info_approx_sum;
      CurrencyView expenseTodayInfoApproxSum = ViewBindings.findChildViewById(rootView, id);
      if (expenseTodayInfoApproxSum == null) {
        break missingId;
      }

      id = R.id.expense_today_info_title;
      TextView expenseTodayInfoTitle = ViewBindings.findChildViewById(rootView, id);
      if (expenseTodayInfoTitle == null) {
        break missingId;
      }

      id = R.id.expense_today_info_value;
      CurrencyView expenseTodayInfoValue = ViewBindings.findChildViewById(rootView, id);
      if (expenseTodayInfoValue == null) {
        break missingId;
      }

      return new LayoutExpenseInfoBinding((ConstraintLayout) rootView,
          expenseCanSpendTodayInfoTitle, expensePeriodInfoApproxSign, expensePeriodInfoApproxSum,
          expensePeriodInfoTitle, expenseTodayInfoApproxSign, expenseTodayInfoApproxSum,
          expenseTodayInfoTitle, expenseTodayInfoValue);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
