package com.d9tilov.android.designsystem

import androidx.annotation.DimenRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.TextUnit

@Composable
fun textDimensionResource(
    @DimenRes id: Int,
): TextUnit = with(LocalDensity.current) { dimensionResource(id).toSp() }
