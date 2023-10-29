package com.d9tilov.android.category.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.category.ui.vm.CategoryCreationUiState
import com.d9tilov.android.category.ui.vm.CategoryCreationViewModel
import com.d9tilov.android.category_ui.R
import com.d9tilov.android.core.model.ItemState
import com.d9tilov.android.designsystem.MmTopAppBar
import com.d9tilov.android.designsystem.MoneyManagerIcons
import com.d9tilov.android.designsystem.OutlineCircle
import com.d9tilov.android.designsystem.theme.MoneyManagerTheme

@Composable
fun CategoryCreationRoute(
    viewModel: CategoryCreationViewModel = hiltViewModel(),
    clickOnCategoryIcon: () -> Unit,
    clickSave: () -> Unit,
    clickBack: () -> Unit,
) {
    val state: CategoryCreationUiState by viewModel.uiState.collectAsStateWithLifecycle()
    CategoryCreationScreen(
        uiState = state,
        onBackClicked = clickBack,
        onSaveClicked = clickSave,
        clickOnCategoryIcon = clickOnCategoryIcon,
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
                    uiState.error?.let { errorState ->
                        Text(
                            text = stringResource(id = errorState.errorMessageId),
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
            OutlineCircle(
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_large),
                    vertical = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_large),
                ),
                size = dimensionResource(id = com.d9tilov.android.common.android.R.dimen.item_color_picker_size),
                color = Color(ContextCompat.getColor(context, uiState.category.color))
            )
        }
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
                    color = android.R.color.holo_blue_light,
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
