package com.d9tilov.android.category.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d9tilov.android.category.data.impl.color.ColorManager
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.category.domain.model.exception.CategoryException
import com.d9tilov.android.category.ui.vm.CategoryCreationUiState
import com.d9tilov.android.category.ui.vm.CategoryCreationViewModel
import com.d9tilov.android.category.ui.vm.CategorySharedViewModel
import com.d9tilov.android.category_ui.R
import com.d9tilov.android.core.constants.DataConstants
import com.d9tilov.android.core.model.ItemState
import com.d9tilov.android.designsystem.BottomActionButton
import com.d9tilov.android.designsystem.MmTopAppBar
import com.d9tilov.android.designsystem.MoneyManagerIcons
import com.d9tilov.android.designsystem.OutlineCircle
import com.d9tilov.android.designsystem.theme.MoneyManagerTheme
import kotlinx.coroutines.launch

@Composable
fun CategoryCreationRoute(
    sharedViewModel: CategorySharedViewModel,
    viewModel: CategoryCreationViewModel = hiltViewModel(),
    openCategoryGroupIconList: () -> Unit,
    openCategoryIconGrid: () -> Unit,
    clickBack: () -> Unit,
) {
    val state: CategoryCreationUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val id: Int by sharedViewModel.categoryIconId.collectAsStateWithLifecycle()
    if (id != DataConstants.NO_RES_ID) viewModel.updateCategory(state.category.copy(icon = id))
    CategoryCreationScreen(
        uiState = state,
        onBackClicked = clickBack,
        onSaveClicked = viewModel::save,
        clickOnCategoryIcon = {
            if (viewModel.uiState.value.isPremium) openCategoryGroupIconList.invoke()
            else openCategoryIconGrid.invoke()
        },
        onCategoryUpdated = viewModel::updateCategory,
        onHideError = viewModel::hideError
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCreationScreen(
    uiState: CategoryCreationUiState,
    onCategoryUpdated: (Category) -> Unit,
    clickOnCategoryIcon: () -> Unit,
    onHideError: () -> Unit,
    onBackClicked: () -> Unit,
    onSaveClicked: () -> Unit,
) {
    val context = LocalContext.current
    var colorListShow by remember { mutableStateOf(false) }
    val currentState = LocalLifecycleOwner.current.lifecycle.currentState
    if (currentState.isAtLeast(Lifecycle.State.RESUMED)) {
        if (uiState.saveStatus?.isSuccess == true) onBackClicked.invoke()
    }
    Scaffold(topBar = {
        MmTopAppBar(
            titleRes =
            if (uiState.itemState == ItemState.CREATE) R.string.title_category_creation
            else R.string.title_category_edit,
            onNavigationClick = onBackClicked
        )
    }) { padding ->
        Column(
            modifier = Modifier
                .padding(top = padding.calculateTopPadding())
        ) {
            val maxNameLength =
                integerResource(id = com.d9tilov.android.designsystem.R.integer.max_category_name_length)
            val focusRequester = remember { FocusRequester() }
            OutlinedTextField(
                value = uiState.category.name,
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_large),
                        vertical = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_large)
                    ),
                maxLines = 1,
                onValueChange = { text ->
                    if (text.length <= maxNameLength) {
                        onCategoryUpdated.invoke(uiState.category.copy(name = text))
                        onHideError.invoke()
                    }
                },
                label = { Text(text = stringResource(id = R.string.category_name_title)) },
                supportingText = {
                    uiState.saveStatus?.onFailure { ex: Throwable ->
                        val messageId = when (ex) {
                            is CategoryException.CategoryEmptyNameException -> R.string.category_unit_name_exist_error
                            is CategoryException.CategoryExistException -> R.string.category_name_exist_error
                            is CategoryException.CategoryNotFoundException -> R.string.category_not_found_error
                            is CategoryException.CategoryNoParentException -> R.string.category_parent_not_found_error
                            else -> com.d9tilov.android.common.android.R.string.unknown_error
                        }
                        Text(
                            text = stringResource(id = messageId),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
            if (uiState.itemState == ItemState.CREATE) {
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_large))
                    .clickable { clickOnCategoryIcon.invoke() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = uiState.category.icon),
                    contentDescription = "Category",
                    tint = Color(ContextCompat.getColor(context, uiState.category.color))
                )
                Icon(
                    imageVector = MoneyManagerIcons.ArrowRight,
                    contentDescription = "Arrow",
                    tint = Color(ContextCompat.getColor(context, uiState.category.color))
                )
            }
            if (colorListShow) {
                val colorList = ColorManager.colorList
                val state = rememberLazyListState()
                val coroutineScope = rememberCoroutineScope()
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_large),
                        ),
                    state = state,
                ) {
                    items(items = colorList, key = { item -> item }) { colorRes ->
                        val isSelected = colorRes == uiState.category.color
                        ColorListSelectorItem(
                            size = dimensionResource(id = com.d9tilov.android.common.android.R.dimen.item_color_picker_size),
                            color = colorRes,
                            selected = isSelected,
                            onClick = {
                                onCategoryUpdated.invoke(uiState.category.copy(color = colorRes))
                                colorListShow = !colorListShow
                            }
                        )
                    }
                }
                LaunchedEffect(true) {
                    val checkedIndex = colorList.indexOfFirst { it == uiState.category.color }
                    if (checkedIndex <= 0) return@LaunchedEffect
                    coroutineScope.launch { state.scrollToItem(checkedIndex - 1) }
                }
            } else {
                OutlineCircle(
                    modifier = Modifier.padding(
                        horizontal = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_large),
                        vertical = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_large),
                    ),
                    size = dimensionResource(id = com.d9tilov.android.common.android.R.dimen.item_color_picker_size),
                    color = Color(ContextCompat.getColor(context, uiState.category.color)),
                    onClick = { colorListShow = !colorListShow }
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            BottomActionButton(
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding(),
                onClick = onSaveClicked
            )
        }
    }
}

fun isValid(str: String): Boolean {
    for (ch in str.toCharArray()) {
        if (ch.isUpperCase()) return false
    }
    return true
}

@Composable
fun ColorListSelectorItem(
    size: Dp = dimensionResource(id = com.d9tilov.android.common.android.R.dimen.item_color_picker_size),
    color: Int,
    selected: Boolean,
    onClick: (Int) -> Unit,
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .padding(horizontal = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_extra_small)),
        verticalArrangement = Arrangement.Center
    ) {
        val scale = if (selected) 1.5f else 1f
        OutlineCircle(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
            size = size,
            color = Color(ContextCompat.getColor(context, color)),
            showOutline = selected,
            onClick = { onClick.invoke(color) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultCategoryCreationPreview() {
    MoneyManagerTheme {
        CategoryCreationScreen(
            uiState = CategoryCreationUiState(
                category = Category.EMPTY_INCOME.copy(
                    id = 1L,
                    name = "Relax",
                    icon = com.d9tilov.android.category_data_impl.R.drawable.ic_category_beach,
                    color = com.d9tilov.android.category_data_impl.R.color.category_red_theme,
                ),
                isPremium = true
            ),
            onBackClicked = { },
            onSaveClicked = {},
            onCategoryUpdated = {},
            onHideError = {},
            clickOnCategoryIcon = {}
        )
    }
}
