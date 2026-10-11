package com.d9tilov.android.incomeexpense.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.core.utils.KeyPress
import com.d9tilov.android.designsystem.ComposeCurrencyView
import com.d9tilov.android.designsystem.MoneyManagerIcons
import com.d9tilov.android.incomeexpense.ui.vm.MainPrice
import kotlinx.coroutines.delay

private const val KEYBOARD_BUTTON_ANIMATION_DELAY_MS = 150L
private const val KEYBOARD_COLUMN_COUNT = 3
private const val AMOUNT_SURFACE_HIGHLIGHT = 0.12f
private const val AMOUNT_SURFACE_SHADE = 0.16f
private const val AMOUNT_BORDER_HIGHLIGHT = 0.36f
private const val AMOUNT_BORDER_SHADE = 0.32f

@Composable
fun MainPriceInput(
    price: MainPrice,
    modifier: Modifier,
    onCurrencyClicked: () -> Unit,
) {
    val surfaceColor = MaterialTheme.colorScheme.surfaceContainer
    val borderColor = MaterialTheme.colorScheme.primary
    Surface(
        modifier = modifier,
        onClick = onCurrencyClicked,
        border =
            BorderStroke(
                dimensionResource(com.d9tilov.android.designsystem.R.dimen.border_width_medium),
                Brush.verticalGradient(
                    colors =
                        listOf(
                            lerp(borderColor, Color.White, AMOUNT_BORDER_HIGHLIGHT),
                            borderColor,
                            lerp(borderColor, Color.Black, AMOUNT_BORDER_SHADE),
                        ),
                ),
            ),
        shape =
            RoundedCornerShape(
                dimensionResource(com.d9tilov.android.designsystem.R.dimen.keyboard_key_corner_radius),
            ),
        tonalElevation = dimensionResource(com.d9tilov.android.designsystem.R.dimen.keyboard_tonal_elevation),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors =
                                listOf(
                                    lerp(surfaceColor, Color.White, AMOUNT_SURFACE_HIGHLIGHT),
                                    surfaceColor,
                                    lerp(surfaceColor, Color.Black, AMOUNT_SURFACE_SHADE),
                                ),
                        ),
                    ),
            contentAlignment = Alignment.Center,
        ) {
            ComposeCurrencyView(
                modifier =
                    Modifier.padding(
                        horizontal = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_medium),
                        vertical = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_small),
                    ),
                symbol = price.currencyCode.getSymbolByCode(),
                value = price.value,
            )
        }
    }
}

@Composable
fun KeyBoardLayout(
    modifier: Modifier,
    onKeyboardClicked: () -> Unit,
    onNumberClicked: (KeyPress) -> Unit,
) {
    Box(
        modifier = modifier.padding(bottom = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_small)),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_extra_large),
                    ),
            verticalArrangement =
                Arrangement.spacedBy(dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_medium)),
        ) {
            KeyPress.entries.chunked(KEYBOARD_COLUMN_COUNT).forEach { keys ->
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    keys.forEach { keyPress ->
                        AnimatedKeyboardButton(
                            keyPress = keyPress,
                            item = keyPress.value,
                            onNumberClicked = onNumberClicked,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }
        Icon(
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_expanded))
                    .clickable { onKeyboardClicked() },
            imageVector = MoneyManagerIcons.HideKeyboard,
            tint = MaterialTheme.colorScheme.secondary,
            contentDescription = "HideKeyboard",
        )
    }
}

@Composable
fun AnimatedKeyboardButton(
    keyPress: KeyPress,
    item: String,
    onNumberClicked: (KeyPress) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.2f else 1f,
        animationSpec =
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow,
            ),
        label = "keyboard_button_scale",
    )

    Surface(
        modifier =
            modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication =
                        ripple(
                            bounded = false,
                            radius =
                                dimensionResource(
                                    com.d9tilov.android.designsystem.R.dimen.keyboard_key_ripple_radius,
                                ),
                        ),
                ) {
                    isPressed = true
                    onNumberClicked(keyPress)
                },
        shape = CircleShape,
        color = Color.Transparent,
    ) {
        if (keyPress == KeyPress.Del) {
            Icon(
                imageVector = MoneyManagerIcons.BackSpace,
                contentDescription = "BackSpace",
                tint = MaterialTheme.colorScheme.primary,
            )
        } else {
            Text(
                text = item,
                style =
                    MaterialTheme.typography.headlineMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                    ),
            )
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(KEYBOARD_BUTTON_ANIMATION_DELAY_MS)
            isPressed = false
        }
    }
}
