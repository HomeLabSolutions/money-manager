package com.d9tilov.android.incomeexpense.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Velocity
import com.d9tilov.android.designsystem.MoneyManagerIcons
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun TransactionSearchLayout(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearchClosed: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    var searchVisible by rememberSaveable { mutableStateOf(query.isNotEmpty()) }
    val focusManager = LocalFocusManager.current
    val threshold = with(LocalDensity.current) { dimensionResource(R.dimen.transaction_search_pull_threshold).toPx() }
    val closeSearch by rememberUpdatedState {
        focusManager.clearFocus()
        onSearchClosed()
        searchVisible = false
    }
    val keyboardVisible by rememberUpdatedState(WindowInsets.isImeVisible)
    LaunchedEffect(searchVisible) {
        if (searchVisible) {
            snapshotFlow { keyboardVisible }
                .dropWhile { visible -> !visible }
                .first { visible -> !visible }
            closeSearch()
        }
    }
    val connection =
        remember(threshold) {
            SearchPullConnection(
                threshold = threshold,
                isSearchVisible = { searchVisible },
                onRevealSearch = { searchVisible = true },
            )
        }
    Column(modifier.nestedScroll(connection)) {
        AnimatedVisibility(visible = searchVisible) {
            TransactionSearchField(
                query = query,
                onQueryChanged = onQueryChanged,
                onClose = closeSearch,
            )
        }
        content()
    }
}

@Composable
private fun TransactionSearchField(
    query: String,
    onQueryChanged: (String) -> Unit,
    onClose: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }
    val verticalPadding = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_small)
    val horizontalPadding = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_medium)
    BasicTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding, vertical = verticalPadding)
                .height(dimensionResource(R.dimen.transaction_search_field_height))
                .focusRequester(focusRequester),
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        interactionSource = interactionSource,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
        decorationBox = { innerTextField ->
            OutlinedTextFieldDefaults.DecorationBox(
                value = query,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                placeholder = { Text(stringResource(R.string.transaction_search_hint)) },
                trailingIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            MoneyManagerIcons.Close,
                            contentDescription = stringResource(R.string.transaction_search_close),
                        )
                    }
                },
                contentPadding =
                    PaddingValues(
                        horizontal = horizontalPadding,
                        vertical = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_extra_small),
                    ),
                container = {},
            )
        },
    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

internal class SearchPullConnection(
    private val threshold: Float,
    private val isSearchVisible: () -> Boolean,
    private val onRevealSearch: () -> Unit,
) : NestedScrollConnection {
    private var pullDistance = 0f

    override fun onPreScroll(
        available: Offset,
        source: NestedScrollSource,
    ): Offset {
        if (available.y < 0f) pullDistance = 0f
        return Offset.Zero
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource,
    ): Offset {
        if (isSearchVisible() || source != NestedScrollSource.UserInput || available.y <= 0f) return Offset.Zero
        pullDistance += available.y
        if (pullDistance >= threshold) {
            pullDistance = 0f
            onRevealSearch()
        }
        return Offset(0f, available.y)
    }

    override suspend fun onPostFling(
        consumed: Velocity,
        available: Velocity,
    ): Velocity {
        pullDistance = 0f
        return Velocity.Zero
    }
}
