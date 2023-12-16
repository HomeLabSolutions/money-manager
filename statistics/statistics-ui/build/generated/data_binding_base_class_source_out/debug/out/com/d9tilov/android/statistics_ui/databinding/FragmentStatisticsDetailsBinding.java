// Generated by view binder compiler. Do not edit!
package com.d9tilov.android.statistics_ui.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.d9tilov.android.designsystem.databinding.CommonToolbarBinding;
import com.d9tilov.android.statistics_ui.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentStatisticsDetailsBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final RecyclerView statisticsDetailsRv;

  @NonNull
  public final CommonToolbarBinding statisticsDetailsToolbar;

  private FragmentStatisticsDetailsBinding(@NonNull ConstraintLayout rootView,
      @NonNull RecyclerView statisticsDetailsRv,
      @NonNull CommonToolbarBinding statisticsDetailsToolbar) {
    this.rootView = rootView;
    this.statisticsDetailsRv = statisticsDetailsRv;
    this.statisticsDetailsToolbar = statisticsDetailsToolbar;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentStatisticsDetailsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentStatisticsDetailsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_statistics_details, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentStatisticsDetailsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.statistics_details_rv;
      RecyclerView statisticsDetailsRv = ViewBindings.findChildViewById(rootView, id);
      if (statisticsDetailsRv == null) {
        break missingId;
      }

      id = R.id.statistics_details_toolbar;
      View statisticsDetailsToolbar = ViewBindings.findChildViewById(rootView, id);
      if (statisticsDetailsToolbar == null) {
        break missingId;
      }
      CommonToolbarBinding binding_statisticsDetailsToolbar = CommonToolbarBinding.bind(statisticsDetailsToolbar);

      return new FragmentStatisticsDetailsBinding((ConstraintLayout) rootView, statisticsDetailsRv,
          binding_statisticsDetailsToolbar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
