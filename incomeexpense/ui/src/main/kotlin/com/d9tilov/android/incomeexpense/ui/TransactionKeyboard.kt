package com.d9tilov.android.incomeexpense.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.core.utils.KeyPress
import com.d9tilov.android.designsystem.ComposeCurrencyView
import com.d9tilov.android.designsystem.MoneyManagerIcons
import com.d9tilov.android.incomeexpense.ui.vm.MainPrice
import kotlinx.coroutines.delay

private const val KEYBOARD_BUTTON_ANIMATION_DELAY_MS = 150L
private const val KEYBOARD_COLUMN_COUNT = 3

@Composable
fun MainPriceInput(
    price: MainPrice,
    modifier: Modifier,
    onCurrencyClicked: () -> Unit,
) {
    Surface(
        modifier = modifier,
        onClick = onCurrencyClicked,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 1.dp,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            ComposeCurrencyView(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
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
    Box(modifier = modifier.padding(bottom = 8.dp)) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(KEYBOARD_COLUMN_COUNT),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            userScrollEnabled = false,
            contentPadding = PaddingValues(start = 40.dp, end = 40.dp),
        ) {
            items(KeyPress.entries, key = { it.value.hashCode() }) { keyPress ->
                AnimatedKeyboardButton(
                    keyPress = keyPress,
                    item = keyPress.value,
                    onNumberClicked = onNumberClicked,
                )
            }
        }
        Icon(
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 24.dp)
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
            Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = false, radius = 20.dp),
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
