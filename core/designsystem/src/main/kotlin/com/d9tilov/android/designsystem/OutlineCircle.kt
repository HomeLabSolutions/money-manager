package com.d9tilov.android.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.d9tilov.android.designsystem.theme.MoneyManagerTheme

@Composable
fun OutlineCircle(
    modifier: Modifier,
    color: Color,
    size: Dp,
    showOutline: Boolean = true,
    onClick: (Color) -> Unit,
) {
    Box(
        modifier =
            modifier
                .size(size)
                .padding(1.dp)
                .clip(CircleShape)
                .background(color)
                .clickable { onClick(color) }
                .then(
                    if (showOutline) {
                        Modifier
                            .border(2.dp, MaterialTheme.colorScheme.secondary, CircleShape)
                    } else {
                        Modifier
                    },
                ),
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultCategoryCreationPreview() {
    MoneyManagerTheme { OutlineCircle(Modifier, Color.Red, 100.dp, true) {} }
}
