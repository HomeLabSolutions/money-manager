package com.d9tilov.android.category.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.spring
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.category.domain.entity.CategoryDestination
import com.d9tilov.android.category.ui.vm.CategoryListViewModel
import com.d9tilov.android.category.ui.vm.CategoryUiState
import com.d9tilov.android.core.constants.DataConstants.NO_ID
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.designsystem.BottomActionButton
import com.d9tilov.android.designsystem.MmTopAppBar
import com.d9tilov.android.designsystem.MoneyManagerIcons
import com.d9tilov.android.designsystem.SimpleDialog
import com.d9tilov.android.designsystem.theme.MoneyManagerTheme
import kotlin.math.roundToInt
import kotlin.random.Random

private const val CATEGORY_GRID_COLUMN_COUNT = 4
private const val CATEGORY_SHAKE_OFFSET_PX = 2f
private const val CATEGORY_SHAKE_JITTER_BOUND_PX = 5
private const val PREVIEW_CATEGORY_COUNT = 34
private const val PREVIEW_CATEGORY_NAME_LIMIT = 15

@Composable
fun CategoryListRoute(
    viewModel: CategoryListViewModel = hiltViewModel(),
    openCategory: (Long, TransactionType) -> Unit,
    onCategoryClickAndBack: (Category) -> Unit,
    clickBack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CategoryListScreen(
        uiState = state,
        onBackClicked = clickBack,
        onCategoryClicked = { category ->
            when (viewModel.destination) {
                CategoryDestination.MAIN_SCREEN -> openCategory(category.id, viewModel.transactionType)

                CategoryDestination.MAIN_WITH_SUM_SCREEN,
                CategoryDestination.EDIT_TRANSACTION_SCREEN,
                CategoryDestination.EDIT_REGULAR_TRANSACTION_SCREEN,
                -> onCategoryClickAndBack(category)
            }
        },
        onCreateClicked = { openCategory(NO_ID, viewModel.transactionType) },
        onRemoveClicked = { category -> viewModel.remove(category) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryListScreen(
    uiState: CategoryUiState,
    onBackClicked: () -> Unit,
    onCreateClicked: () -> Unit,
    onCategoryClicked: (Category) -> Unit,
    onRemoveClicked: (Category) -> Unit,
) {
    var isRemoveState by remember { mutableStateOf(false) }
    val shake = rememberCategoryShake(isRemoveState)
    var categoryToRemove by remember { mutableStateOf<Category?>(null) }
    BackHandler {
        if (isRemoveState) {
            isRemoveState = false
        } else {
            onBackClicked()
        }
    }
    Scaffold(topBar = {
        MmTopAppBar(
            titleRes = R.string.title_category,
            onNavigationClick = onBackClicked,
        )
    }) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding()),
        ) {
            LazyVerticalGrid(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(
                            vertical = dimensionResource(id = com.d9tilov.android.designsystem.R.dimen.padding_medium),
                            horizontal =
                                dimensionResource(
                                    id = com.d9tilov.android.designsystem.R.dimen.padding_medium,
                                ),
                        ),
                columns = GridCells.Fixed(CATEGORY_GRID_COLUMN_COUNT),
            ) {
                items(uiState.categories, { it.id }) { item ->
                    CategoryGridItem(
                        category = item,
                        isRemoveState = isRemoveState,
                        shake = shake,
                        onClick = {
                            if (isRemoveState) {
                                categoryToRemove = item
                            } else {
                                onCategoryClicked(item)
                            }
                            isRemoveState = false
                        },
                        onLongClick = { isRemoveState = true },
                    )
                }
            }
            BottomActionButton(
                onClick = onCreateClicked,
                text = stringResource(id = R.string.create),
            )
        }
        CategoryRemovalDialog(
            category = categoryToRemove,
            onRemoveClicked = onRemoveClicked,
            onDismiss = { categoryToRemove = null },
        )
    }
}

@Composable
private fun rememberCategoryShake(isRemoveState: Boolean): Animatable<Float, AnimationVector1D> {
    val shake = remember { Animatable(0f) }
    LaunchedEffect(isRemoveState) {
        var i = 0
        while (isRemoveState) {
            if (i % 2 == 0) {
                shake.animateTo(CATEGORY_SHAKE_OFFSET_PX, spring(stiffness = 5_000f))
            } else {
                shake.animateTo(-CATEGORY_SHAKE_OFFSET_PX, spring(stiffness = 5_000f))
            }
            ++i
            if (i == 2) i = 0
        }
        shake.animateTo(0f)
    }
    return shake
}

@Composable
private fun CategoryGridItem(
    category: Category,
    isRemoveState: Boolean,
    shake: Animatable<Float, AnimationVector1D>,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    val context = LocalContext.current
    Box {
        Column(
            modifier =
                Modifier
                    .size(dimensionResource(id = R.dimen.category_item_size))
                    .padding(8.dp)
                    .offset {
                        IntOffset(
                            x =
                                shake.value.roundToInt() +
                                    Random.nextInt(CATEGORY_SHAKE_JITTER_BOUND_PX),
                            y =
                                shake.value.roundToInt() +
                                    Random.nextInt(CATEGORY_SHAKE_JITTER_BOUND_PX),
                        )
                    }.combinedClickable(
                        onClick = onClick,
                        onLongClick = onLongClick,
                    ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = category.icon),
                contentDescription = "Backup",
                tint = Color(ContextCompat.getColor(context, category.color)),
            )
            Text(
                text = category.name,
                color = Color(ContextCompat.getColor(context, category.color)),
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (isRemoveState) {
            Icon(
                modifier =
                    Modifier
                        .size(32.dp),
                imageVector = MoneyManagerIcons.Cross,
                tint = MaterialTheme.colorScheme.error,
                contentDescription = "",
            )
        }
    }
}

@Composable
private fun CategoryRemovalDialog(
    category: Category?,
    onRemoveClicked: (Category) -> Unit,
    onDismiss: () -> Unit,
) {
    category?.let { categoryToRemove ->
        SimpleDialog(
            show = true,
            title = stringResource(R.string.category_delete_title),
            subtitle = stringResource(R.string.category_delete_subtitle, categoryToRemove.name),
            dismissButton = stringResource(com.d9tilov.android.common.android.R.string.cancel),
            confirmButton = stringResource(com.d9tilov.android.common.android.R.string.delete),
            onConfirm = {
                onRemoveClicked(categoryToRemove)
                onDismiss()
            },
            onDismiss = onDismiss,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultCategoryListPreview() {
    MoneyManagerTheme {
        CategoryListScreen(
            CategoryUiState(
                List(PREVIEW_CATEGORY_COUNT) { index ->
                    val id = index + 1
                    mockCategory(id.toLong(), "Category${minOf(id, PREVIEW_CATEGORY_NAME_LIMIT)}")
                },
            ),
            {},
            {},
            {},
            {},
        )
    }
}

private fun mockCategory(
    id: Long,
    name: String,
) = Category.EMPTY_INCOME.copy(
    id = id,
    name = name,
    icon = com.d9tilov.android.common.android.R.drawable.ic_category_cafe,
    color = android.R.color.holo_blue_light,
)
