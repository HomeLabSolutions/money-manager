// Generated by view binder compiler. Do not edit!
package com.d9tilov.android.statistics_ui.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.d9tilov.android.statistics_ui.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class LayoutEmptyStatisticsPlaceholderBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageView statisticsEmptyPlaceholderIcon;

  @NonNull
  public final TextView statisticsEmptyPlaceholderTitle;

  private LayoutEmptyStatisticsPlaceholderBinding(@NonNull ConstraintLayout rootView,
      @NonNull ImageView statisticsEmptyPlaceholderIcon,
      @NonNull TextView statisticsEmptyPlaceholderTitle) {
    this.rootView = rootView;
    this.statisticsEmptyPlaceholderIcon = statisticsEmptyPlaceholderIcon;
    this.statisticsEmptyPlaceholderTitle = statisticsEmptyPlaceholderTitle;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static LayoutEmptyStatisticsPlaceholderBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static LayoutEmptyStatisticsPlaceholderBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.layout_empty_statistics_placeholder, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static LayoutEmptyStatisticsPlaceholderBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.statistics_empty_placeholder_icon;
      ImageView statisticsEmptyPlaceholderIcon = ViewBindings.findChildViewById(rootView, id);
      if (statisticsEmptyPlaceholderIcon == null) {
        break missingId;
      }

      id = R.id.statistics_empty_placeholder_title;
      TextView statisticsEmptyPlaceholderTitle = ViewBindings.findChildViewById(rootView, id);
      if (statisticsEmptyPlaceholderTitle == null) {
        break missingId;
      }

      return new LayoutEmptyStatisticsPlaceholderBinding((ConstraintLayout) rootView,
          statisticsEmptyPlaceholderIcon, statisticsEmptyPlaceholderTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
