package com.d9tilov.android.designsystem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * MoneyManager filled button with generic content slot. Wraps Material 3 [Button].
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param small Whether or not the size of the button should be small or regular.
 * @param colors [ButtonColors] that will be used to resolve the container and content color for
 * this button in different states. See [MoneyManagerButtonDefaults.filledButtonColors].
 * @param contentPadding The spacing values to apply internally between the container and the
 * content. See [MoneyManagerButtonDefaults.buttonContentPadding].
 * @param content The button content.
 */
@Composable
fun FilledButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    small: Boolean = false,
    colors: ButtonColors = MoneyManagerButtonDefaults.filledButtonColors(),
    contentPadding: PaddingValues = MoneyManagerButtonDefaults.buttonContentPadding(small = small),
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        modifier =
            if (small) {
                modifier.heightIn(min = MoneyManagerButtonDefaults.SmallButtonHeight)
            } else {
                modifier
            },
        enabled = enabled,
        colors = colors,
        contentPadding = contentPadding,
        content = {
            ProvideTextStyle(value = MaterialTheme.typography.labelLarge) {
                content()
            }
        },
    )
}

@Composable
fun BottomActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String = stringResource(R.string.save),
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier =
            modifier
                .padding(dimensionResource(R.dimen.padding_medium))
                .fillMaxWidth(),
        enabled = enabled,
        colors = MoneyManagerButtonDefaults.filledButtonColors(),
        contentPadding = MoneyManagerButtonDefaults.buttonContentPadding(small = false),
        content = {
            ProvideTextStyle(value = MaterialTheme.typography.labelLarge) {
                Text(text = text)
            }
        },
    )
}

/**
 * MoneyManager filled button with text and icon content slots.
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param small Whether or not the size of the button should be small or regular.
 * @param colors [ButtonColors] that will be used to resolve the container and content color for
 * this button in different states. See [MoneyManagerButtonDefaults.filledButtonColors].
 * @param text The button text label content.
 * @param leadingIcon The button leading icon content. Pass `null` here for no leading icon.
 * @param trailingIcon The button trailing icon content. Pass `null` here for no trailing icon.
 */
@Composable
fun FilledButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    small: Boolean = false,
    colors: ButtonColors = MoneyManagerButtonDefaults.filledButtonColors(),
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    FilledButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        small = small,
        colors = colors,
        contentPadding =
            MoneyManagerButtonDefaults.buttonContentPadding(
                small = small,
                leadingIcon = leadingIcon != null,
                trailingIcon = trailingIcon != null,
            ),
    ) {
        ButtonContent(
            text = text,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
        )
    }
}

/**
 * Money Manager outlined button with generic content slot. Wraps Material 3 [OutlinedButton].
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param small Whether or not the size of the button should be small or regular.
 * @param border Border to draw around the button. Pass `null` here for no border.
 * @param colors [ButtonColors] that will be used to resolve the container and content color for
 * this button in different states. See [MoneyManagerButtonDefaults.outlinedButtonColors].
 * @param contentPadding The spacing values to apply internally between the container and the
 * content. See [MoneyManagerButtonDefaults.buttonContentPadding].
 * @param content The button content.
 */
@Composable
fun OutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    small: Boolean = false,
    border: BorderStroke? = MoneyManagerButtonDefaults.outlinedButtonBorder(enabled = enabled),
    colors: ButtonColors = MoneyManagerButtonDefaults.outlinedButtonColors(),
    contentPadding: PaddingValues = MoneyManagerButtonDefaults.buttonContentPadding(small = small),
    content: @Composable RowScope.() -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        modifier =
            if (small) {
                modifier.heightIn(min = MoneyManagerButtonDefaults.SmallButtonHeight)
            } else {
                modifier
            },
        enabled = enabled,
        border = border,
        colors = colors,
        contentPadding = contentPadding,
        content = {
            ProvideTextStyle(value = MaterialTheme.typography.labelSmall) {
                content()
            }
        },
    )
}

/**
 * Money Manager outlined button with text and icon content slots.
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param small Whether or not the size of the button should be small or regular.
 * @param border Border to draw around the button. Pass `null` here for no border.
 * @param colors [ButtonColors] that will be used to resolve the container and content color for
 * this button in different states. See [MoneyManagerButtonDefaults.outlinedButtonColors].
 * @param text The button text label content.
 * @param leadingIcon The button leading icon content. Pass `null` here for no leading icon.
 * @param trailingIcon The button trailing icon content. Pass `null` here for no trailing icon.
 */
@Composable
fun OutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    small: Boolean = false,
    border: BorderStroke? = MoneyManagerButtonDefaults.outlinedButtonBorder(enabled = enabled),
    colors: ButtonColors = MoneyManagerButtonDefaults.outlinedButtonColors(),
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        small = small,
        border = border,
        colors = colors,
        contentPadding =
            MoneyManagerButtonDefaults.buttonContentPadding(
                small = small,
                leadingIcon = leadingIcon != null,
                trailingIcon = trailingIcon != null,
            ),
    ) {
        ButtonContent(
            text = text,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
        )
    }
}

/**
 * Money Manager text button with generic content slot. Wraps Material 3 [TextButton].
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param small Whether or not the size of the button should be small or regular.
 * @param colors [ButtonColors] that will be used to resolve the container and content color for
 * this button in different states. See [MoneyManagerButtonDefaults.textButtonColors].
 * @param contentPadding The spacing values to apply internally between the container and the
 * content. See [MoneyManagerButtonDefaults.buttonContentPadding].
 * @param content The button content.
 */
@Composable
fun TextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    small: Boolean = false,
    colors: ButtonColors = MoneyManagerButtonDefaults.textButtonColors(),
    contentPadding: PaddingValues = MoneyManagerButtonDefaults.buttonContentPadding(small = small),
    content: @Composable RowScope.() -> Unit,
) {
    TextButton(
        onClick = onClick,
        modifier =
            if (small) {
                modifier.heightIn(min = MoneyManagerButtonDefaults.SmallButtonHeight)
            } else {
                modifier
            },
        enabled = enabled,
        colors = colors,
        contentPadding = contentPadding,
        content = {
            ProvideTextStyle(value = MaterialTheme.typography.labelSmall) {
                content()
            }
        },
    )
}

/**
 * Money Manager text button with text and icon content slots.
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param small Whether or not the size of the button should be small or regular.
 * @param colors [ButtonColors] that will be used to resolve the container and content color for
 * this button in different states. See [MoneyManagerButtonDefaults.textButtonColors].
 * @param text The button text label content.
 * @param leadingIcon The button leading icon content. Pass `null` here for no leading icon.
 * @param trailingIcon The button trailing icon content. Pass `null` here for no trailing icon.
 */
@Composable
fun TextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    small: Boolean = false,
    colors: ButtonColors = MoneyManagerButtonDefaults.textButtonColors(),
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        small = small,
        colors = colors,
        contentPadding =
            MoneyManagerButtonDefaults.buttonContentPadding(
                small = small,
                leadingIcon = leadingIcon != null,
                trailingIcon = trailingIcon != null,
            ),
    ) {
        ButtonContent(
            text = text,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
        )
    }
}

/**
 * Internal Money Manager button content layout for arranging the text label, leading icon and
 * trailing icon.
 *
 * @param text The button text label content.
 * @param leadingIcon The button leading icon content. Pass `null` here for no leading icon.
 * @param trailingIcon The button trailing icon content. Pass `null` here for no trailing icon.
 */
@Composable
private fun RowScope.ButtonContent(
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
) {
    if (leadingIcon != null) {
        Box(Modifier.sizeIn(maxHeight = MoneyManagerButtonDefaults.ButtonIconSize)) {
            leadingIcon()
        }
    }
    Box(
        Modifier
            .padding(
                start =
                    if (leadingIcon != null) {
                        MoneyManagerButtonDefaults.ButtonContentSpacing
                    } else {
                        0.dp
                    },
                end =
                    if (trailingIcon != null) {
                        MoneyManagerButtonDefaults.ButtonContentSpacing
                    } else {
                        0.dp
                    },
            ),
    ) {
        text()
    }
    if (trailingIcon != null) {
        Box(Modifier.sizeIn(maxHeight = MoneyManagerButtonDefaults.ButtonIconSize)) {
            trailingIcon()
        }
    }
}

/**
 * Money Manager button default values.
 */
object MoneyManagerButtonDefaults {
    private const val DISABLE_BUTTON_CONTAINER_ALPHA = 0.12f
    private const val DISABLE_BUTTON_CONTENT_ALPHA = 0.38f
    private val ButtonHorizontalPadding = 24.dp
    private val ButtonHorizontalIconPadding = 16.dp
    private val ButtonVerticalPadding = 8.dp
    private val SmallButtonHorizontalPadding = 16.dp
    private val SmallButtonHorizontalIconPadding = 12.dp
    private val SmallButtonVerticalPadding = 7.dp
    val SmallButtonHeight = 32.dp
    val ButtonContentSpacing = 8.dp
    val ButtonIconSize = 18.dp

    fun buttonContentPadding(
        small: Boolean,
        leadingIcon: Boolean = false,
        trailingIcon: Boolean = false,
    ): PaddingValues =
        PaddingValues(
            start =
                when {
                    small && leadingIcon -> SmallButtonHorizontalIconPadding
                    small -> SmallButtonHorizontalPadding
                    leadingIcon -> ButtonHorizontalIconPadding
                    else -> ButtonHorizontalPadding
                },
            top = if (small) SmallButtonVerticalPadding else ButtonVerticalPadding,
            end =
                when {
                    small && trailingIcon -> SmallButtonHorizontalIconPadding
                    small -> SmallButtonHorizontalPadding
                    trailingIcon -> ButtonHorizontalIconPadding
                    else -> ButtonHorizontalPadding
                },
            bottom = if (small) SmallButtonVerticalPadding else ButtonVerticalPadding,
        )

    @Composable
    fun filledButtonColors(
        containerColor: Color = MaterialTheme.colorScheme.primary,
        contentColor: Color = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor: Color =
            MaterialTheme.colorScheme.onBackground.copy(
                alpha = DISABLE_BUTTON_CONTAINER_ALPHA,
            ),
        disabledContentColor: Color =
            MaterialTheme.colorScheme.onBackground.copy(
                alpha = DISABLE_BUTTON_CONTENT_ALPHA,
            ),
    ) = ButtonDefaults.buttonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
    )

    @Composable
    fun outlinedButtonBorder(
        enabled: Boolean,
        width: Dp = 1.dp,
        color: Color = MaterialTheme.colorScheme.onBackground,
        disabledColor: Color =
            MaterialTheme.colorScheme.onBackground.copy(
                alpha = DISABLE_BUTTON_CONTAINER_ALPHA,
            ),
    ): BorderStroke =
        BorderStroke(
            width = width,
            color = if (enabled) color else disabledColor,
        )

    @Composable
    fun outlinedButtonColors(
        containerColor: Color = Color.Transparent,
        contentColor: Color = MaterialTheme.colorScheme.onBackground,
        disabledContainerColor: Color = Color.Transparent,
        disabledContentColor: Color =
            MaterialTheme.colorScheme.onBackground.copy(
                alpha = DISABLE_BUTTON_CONTENT_ALPHA,
            ),
    ) = ButtonDefaults.outlinedButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
    )

    @Composable
    fun textButtonColors(
        containerColor: Color = Color.Transparent,
        contentColor: Color = MaterialTheme.colorScheme.onBackground,
        disabledContainerColor: Color = Color.Transparent,
        disabledContentColor: Color =
            MaterialTheme.colorScheme.onBackground.copy(
                alpha = DISABLE_BUTTON_CONTENT_ALPHA,
            ),
    ) = ButtonDefaults.textButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
    )
}
