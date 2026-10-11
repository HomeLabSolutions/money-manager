/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.d9tilov.android.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.d9tilov.android.designsystem.R
import com.d9tilov.android.designsystem.textDimensionResource

/**
 * Now in Android typography.
 */
internal val MmTypography: Typography
    @Composable get() =
        Typography(
            displayLarge =
                TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = textDimensionResource(R.dimen.typography_display_large_font_size),
                    lineHeight = textDimensionResource(R.dimen.typography_display_large_line_height),
                    letterSpacing = textDimensionResource(R.dimen.typography_display_large_letter_spacing),
                ),
            displayMedium =
                TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = textDimensionResource(R.dimen.typography_display_medium_font_size),
                    lineHeight = textDimensionResource(R.dimen.typography_display_medium_line_height),
                    letterSpacing = textDimensionResource(R.dimen.typography_display_medium_letter_spacing),
                ),
            displaySmall =
                TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = textDimensionResource(R.dimen.typography_display_small_font_size),
                    lineHeight = textDimensionResource(R.dimen.typography_display_small_line_height),
                    letterSpacing = textDimensionResource(R.dimen.typography_display_small_letter_spacing),
                ),
            headlineLarge =
                TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = textDimensionResource(R.dimen.typography_headline_large_font_size),
                    lineHeight = textDimensionResource(R.dimen.typography_headline_large_line_height),
                    letterSpacing = textDimensionResource(R.dimen.typography_headline_large_letter_spacing),
                ),
            headlineMedium =
                TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = textDimensionResource(R.dimen.typography_headline_medium_font_size),
                    lineHeight = textDimensionResource(R.dimen.typography_headline_medium_line_height),
                    letterSpacing = textDimensionResource(R.dimen.typography_headline_medium_letter_spacing),
                ),
            headlineSmall =
                TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = textDimensionResource(R.dimen.typography_headline_small_font_size),
                    lineHeight = textDimensionResource(R.dimen.typography_headline_small_line_height),
                    letterSpacing = textDimensionResource(R.dimen.typography_headline_small_letter_spacing),
                ),
            titleLarge =
                TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = textDimensionResource(R.dimen.typography_title_large_font_size),
                    lineHeight = textDimensionResource(R.dimen.typography_title_large_line_height),
                    letterSpacing = textDimensionResource(R.dimen.typography_title_large_letter_spacing),
                ),
            titleMedium =
                TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = textDimensionResource(R.dimen.typography_title_medium_font_size),
                    lineHeight = textDimensionResource(R.dimen.typography_title_medium_line_height),
                    letterSpacing = textDimensionResource(R.dimen.typography_title_medium_letter_spacing),
                ),
            titleSmall =
                TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = textDimensionResource(R.dimen.typography_title_small_font_size),
                    lineHeight = textDimensionResource(R.dimen.typography_title_small_line_height),
                    letterSpacing = textDimensionResource(R.dimen.typography_title_small_letter_spacing),
                ),
            bodyLarge =
                TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = textDimensionResource(R.dimen.typography_body_large_font_size),
                    lineHeight = textDimensionResource(R.dimen.typography_body_large_line_height),
                    letterSpacing = textDimensionResource(R.dimen.typography_body_large_letter_spacing),
                ),
            bodyMedium =
                TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = textDimensionResource(R.dimen.typography_body_medium_font_size),
                    lineHeight = textDimensionResource(R.dimen.typography_body_medium_line_height),
                    letterSpacing = textDimensionResource(R.dimen.typography_body_medium_letter_spacing),
                ),
            bodySmall =
                TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = textDimensionResource(R.dimen.typography_body_small_font_size),
                    lineHeight = textDimensionResource(R.dimen.typography_body_small_line_height),
                    letterSpacing = textDimensionResource(R.dimen.typography_body_small_letter_spacing),
                ),
            labelLarge =
                TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = textDimensionResource(R.dimen.typography_label_large_font_size),
                    lineHeight = textDimensionResource(R.dimen.typography_label_large_line_height),
                    letterSpacing = textDimensionResource(R.dimen.typography_label_large_letter_spacing),
                ),
            labelMedium =
                TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = textDimensionResource(R.dimen.typography_label_medium_font_size),
                    lineHeight = textDimensionResource(R.dimen.typography_label_medium_line_height),
                    letterSpacing = textDimensionResource(R.dimen.typography_label_medium_letter_spacing),
                ),
            labelSmall =
                TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = textDimensionResource(R.dimen.typography_label_small_font_size),
                    lineHeight = textDimensionResource(R.dimen.typography_label_small_line_height),
                    letterSpacing = textDimensionResource(R.dimen.typography_label_small_letter_spacing),
                ),
        )
