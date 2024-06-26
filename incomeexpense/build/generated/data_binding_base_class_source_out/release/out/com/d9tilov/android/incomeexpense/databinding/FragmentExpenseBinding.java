// Generated by view binder compiler. Do not edit!
package com.d9tilov.android.incomeexpense.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.d9tilov.android.designsystem.databinding.LayoutEmptyListPlaceholderBinding;
import com.d9tilov.android.incomeexpense.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentExpenseBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final RecyclerView expenseCategoryRvList;

  @NonNull
  public final LayoutExpenseInfoBinding expenseInfoLayoutInclude;

  @NonNull
  public final LayoutEmptyListPlaceholderBinding expenseTransactionEmptyPlaceholderInclude;

  @NonNull
  public final LayoutExpenseTransactionsBinding expenseTransactionLayoutInclude;

  @NonNull
  public final ConstraintLayout transactionMainContent;

  private FragmentExpenseBinding(@NonNull ConstraintLayout rootView,
      @NonNull RecyclerView expenseCategoryRvList,
      @NonNull LayoutExpenseInfoBinding expenseInfoLayoutInclude,
      @NonNull LayoutEmptyListPlaceholderBinding expenseTransactionEmptyPlaceholderInclude,
      @NonNull LayoutExpenseTransactionsBinding expenseTransactionLayoutInclude,
      @NonNull ConstraintLayout transactionMainContent) {
    this.rootView = rootView;
    this.expenseCategoryRvList = expenseCategoryRvList;
    this.expenseInfoLayoutInclude = expenseInfoLayoutInclude;
    this.expenseTransactionEmptyPlaceholderInclude = expenseTransactionEmptyPlaceholderInclude;
    this.expenseTransactionLayoutInclude = expenseTransactionLayoutInclude;
    this.transactionMainContent = transactionMainContent;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentExpenseBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentExpenseBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_expense, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentExpenseBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.expense_category_rv_list;
      RecyclerView expenseCategoryRvList = ViewBindings.findChildViewById(rootView, id);
      if (expenseCategoryRvList == null) {
        break missingId;
      }

      id = R.id.expense_info_layout_include;
      View expenseInfoLayoutInclude = ViewBindings.findChildViewById(rootView, id);
      if (expenseInfoLayoutInclude == null) {
        break missingId;
      }
      LayoutExpenseInfoBinding binding_expenseInfoLayoutInclude = LayoutExpenseInfoBinding.bind(expenseInfoLayoutInclude);

      id = R.id.expense_transaction_empty_placeholder_include;
      View expenseTransactionEmptyPlaceholderInclude = ViewBindings.findChildViewById(rootView, id);
      if (expenseTransactionEmptyPlaceholderInclude == null) {
        break missingId;
      }
      LayoutEmptyListPlaceholderBinding binding_expenseTransactionEmptyPlaceholderInclude = LayoutEmptyListPlaceholderBinding.bind(expenseTransactionEmptyPlaceholderInclude);

      id = R.id.expense_transaction_layout_include;
      View expenseTransactionLayoutInclude = ViewBindings.findChildViewById(rootView, id);
      if (expenseTransactionLayoutInclude == null) {
        break missingId;
      }
      LayoutExpenseTransactionsBinding binding_expenseTransactionLayoutInclude = LayoutExpenseTransactionsBinding.bind(expenseTransactionLayoutInclude);

      ConstraintLayout transactionMainContent = (ConstraintLayout) rootView;

      return new FragmentExpenseBinding((ConstraintLayout) rootView, expenseCategoryRvList,
          binding_expenseInfoLayoutInclude, binding_expenseTransactionEmptyPlaceholderInclude,
          binding_expenseTransactionLayoutInclude, transactionMainContent);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
