package com.d9tilov.android.incomeexpense.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class ScrollAnchor(
    val itemKey: String,
    val scrollOffset: Int,
    val snapshotHash: Int,
) : Parcelable
