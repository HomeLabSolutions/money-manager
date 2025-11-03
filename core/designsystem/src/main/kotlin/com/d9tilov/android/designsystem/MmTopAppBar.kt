package com.d9tilov.android.designsystem

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MmTopAppBar(
    @StringRes titleRes: Int,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector = MoneyManagerIcons.ArrowBack,
    actionIcon: ImageVector? = null,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    onNavigationClick: () -> Unit = {},
    onActionClick: () -> Unit = {},
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 3.dp,
        color = MaterialTheme.colorScheme.surface,
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = titleRes),
                    color = MaterialTheme.colorScheme.tertiary,
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = navigationIcon,
                        contentDescription = "Navigation Icon",
                        tint = MaterialTheme.colorScheme.tertiary,
                    )
                }
            },
            actions = {
                actionIcon?.let {
                    IconButton(onClick = onActionClick) {
                        Icon(
                            imageVector = actionIcon,
                            contentDescription = "Action Icon",
                            tint = MaterialTheme.colorScheme.tertiary,
                        )
                    }
                }
            },
            colors = colors,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview("Top App Bar")
@Composable
fun TopAppBarPreview() {
    MmTopAppBar(
        titleRes = android.R.string.untitled,
        navigationIcon = MoneyManagerIcons.ArrowBack,
        actionIcon = MoneyManagerIcons.ActionAdd,
    )
}
