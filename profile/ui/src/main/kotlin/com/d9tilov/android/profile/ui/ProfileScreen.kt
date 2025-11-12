package com.d9tilov.android.profile.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.d9tilov.android.backup.data.impl.PeriodicBackupWorker
import com.d9tilov.android.common.android.utils.getAppVersion
import com.d9tilov.android.core.utils.CurrencyUtils
import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.core.utils.reduceScaleStr
import com.d9tilov.android.core.utils.toStandardStringDate
import com.d9tilov.android.designsystem.CurrencyTextFieldExtraSmall
import com.d9tilov.android.designsystem.SimpleDialog
import com.d9tilov.android.profile.ui.vm.ProfileUiItem
import com.d9tilov.android.profile.ui.vm.ProfileUiState
import com.d9tilov.android.profile.ui.vm.ProfileViewModel
import com.d9tilov.android.profile.ui.vm.UserUiProfile
import dagger.hilt.android.internal.managers.FragmentComponentManager

@Composable
fun ProfileRoute(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToCurrencyListScreen: () -> Unit,
    navigateToBudgetScreen: () -> Unit,
    navigateToRegularIncomeScreen: () -> Unit,
    navigateToRegularExpenseScreen: () -> Unit,
    navigateToSettingsScreen: () -> Unit,
) {
    val uiState: ProfileUiState by viewModel.profileState.collectAsStateWithLifecycle()
    val showDialog by viewModel.showDialog.collectAsStateWithLifecycle()
    val context = LocalContext.current
    Scaffold { paddingValues ->
        ProfileScreen(
            modifier = Modifier.padding(paddingValues),
            state = uiState,
            showDialog = showDialog,
            onCurrencyClicked = navigateToCurrencyListScreen,
            onBudgetClicked = navigateToBudgetScreen,
            onRegularIncomeClicked = navigateToRegularIncomeScreen,
            onRegularExpenseClicked = navigateToRegularExpenseScreen,
            onSettingsClicked = navigateToSettingsScreen,
            onLogoutClicked = { viewModel.showDialog() },
            onLogoutConfirmClicked = {
                viewModel.logout {
                    PeriodicBackupWorker.stopPeriodicJob(context)
                    val intent =
                        context.packageManager.getLaunchIntentForPackage(
                            if (BuildConfig.DEBUG) "com.d9tilov.moneymanager.debug" else "com.d9tilov.moneymanager",
                        )
                    if (intent != null) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        context.startActivity(intent)
                        (FragmentComponentManager.findActivity(context) as Activity).finish()
                    }
                }
            },
            onLogoutDismissClicked = { viewModel.dismissDialog() },
        )
    }
}

@Composable
fun ProfileScreen(
    modifier: Modifier,
    state: ProfileUiState,
    showDialog: Boolean,
    onCurrencyClicked: () -> Unit,
    onBudgetClicked: () -> Unit,
    onRegularIncomeClicked: () -> Unit,
    onRegularExpenseClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onLogoutClicked: () -> Unit,
    onLogoutConfirmClicked: () -> Unit,
    onLogoutDismissClicked: () -> Unit,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        ProfileCard(state.userProfile)
        ProfileSection(state.currency, onCurrencyClicked)
        ProfileSection(state.budgetData, onBudgetClicked)
        ProfileSection(state.regularIncomes, onRegularIncomeClicked)
        ProfileSection(state.regularExpenses, onRegularExpenseClicked)
        ProfileSection(state.settings, onSettingsClicked)
        Spacer(modifier = Modifier.weight(1f))
        OutlinedButton(
            modifier =
                Modifier.padding(vertical = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_medium)),
            onClick = { onLogoutClicked() },
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.outlinedButtonColors().copy(containerColor = MaterialTheme.colorScheme.error),
        ) {
            Text(
                text = stringResource(R.string.profile_logout),
                color = MaterialTheme.colorScheme.onError,
            )
        }
        Text(
            text =
                stringResource(
                    com.d9tilov.android.common.android.R.string.version,
                    getAppVersion(LocalContext.current),
                ),
            modifier = Modifier.padding(dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_small)),
            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.tertiary),
        )
        SimpleDialog(
            show = showDialog,
            title = stringResource(R.string.logout_dialog_title),
            subtitle = stringResource(R.string.logout_dialog_subtitle),
            dismissButton = stringResource(com.d9tilov.android.common.android.R.string.cancel),
            confirmButton = stringResource(R.string.logout_dialog_button),
            onConfirm = { onLogoutConfirmClicked() },
            onDismiss = { onLogoutDismissClicked() },
        )
    }
}

@Composable
fun ProfileCard(userProfile: UserUiProfile) {
    Column {
        Card(
            modifier =
                Modifier
                    .padding(top = 32.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(align = Alignment.Top),
            elevation = CardDefaults.cardElevation(2.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ProfilePicture(userProfile.photo)
                ProfileContent(userProfile.name)
            }
        }
    }
}

@Composable
fun ProfilePicture(uri: Uri?) {
    Card(
        shape = CircleShape,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.onPrimaryContainer),
        modifier = Modifier.padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        AsyncImage(
            model =
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(uri)
                    .crossfade(true)
                    .build(),
            contentDescription = null,
            modifier =
                Modifier
                    .clip(CircleShape)
                    .size(72.dp),
        )
    }
}

@Composable
fun ProfileContent(name: String?) {
    Text(
        name ?: "",
        modifier = Modifier.padding(dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_medium)),
        style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onPrimaryContainer),
    )
}

@Composable
fun ProfileSection(
    profileUiItem: ProfileUiItem,
    navigationCallback: () -> Unit = {},
) {
    val (icon, title, subtitle) =
        when (profileUiItem) {
            is ProfileUiItem.CurrencyUiItem ->
                ProfileItemData(
                    ImageVector.vectorResource(R.drawable.ic_currency_icon),
                    stringResource(R.string.profile_item_currency_title),
                    null,
                )

            is ProfileUiItem.BudgetUiItem ->
                ProfileItemData(
                    ImageVector.vectorResource(R.drawable.ic_budget_icon),
                    stringResource(R.string.profile_item_budget_title),
                    profileUiItem.budgetData.createdDate.toStandardStringDate(),
                )

            is ProfileUiItem.RegularIncomeUiItem ->
                ProfileItemData(
                    ImageVector.vectorResource(R.drawable.ic_regular_income),
                    stringResource(R.string.profile_item_regular_incomes_title),
                    profileUiItem.regularIncomes.joinToString { it.category.name },
                    null,
                )

            is ProfileUiItem.RegularExpenseUiItem ->
                ProfileItemData(
                    ImageVector.vectorResource(R.drawable.ic_regular_expense),
                    stringResource(R.string.profile_item_regular_expenses_title),
                    profileUiItem.regularExpenses.joinToString { it.category.name },
                )

            is ProfileUiItem.Settings ->
                ProfileItemData(
                    ImageVector.vectorResource(R.drawable.ic_settings),
                    stringResource(R.string.profile_item_settings_title),
                    null,
                )
        }
    ConstraintLayout(
        modifier =
            Modifier
                .then(
                    if (profileUiItem is ProfileUiItem.CurrencyUiItem) {
                        Modifier
                            .fillMaxWidth()
                            .padding(top = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_large))
                    } else {
                        Modifier
                            .fillMaxWidth()
                            .padding(0.dp)
                    },
                ).height(dimensionResource(com.d9tilov.android.designsystem.R.dimen.profile_item_height))
                .clickable(onClick = navigationCallback),
    ) {
        val (idIcon, idTitle, idData, idSubtitle, idDivider) = createRefs()
        Icon(
            modifier =
                Modifier
                    .padding(
                        start = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_large),
                        end = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_medium),
                    ).constrainAs(idIcon) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    },
            imageVector = icon,
            contentDescription = "content description",
            tint = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = title,
            modifier =
                Modifier.constrainAs(idTitle) {
                    start.linkTo(idIcon.end)
                    top.linkTo(idIcon.top)
                    bottom.linkTo(idIcon.bottom)
                },
            style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary),
        )
        when (profileUiItem) {
            is ProfileUiItem.CurrencyUiItem -> {
                val currencyIcon = CurrencyUtils.getCurrencyIcon(profileUiItem.currencyCode)
                val (idCurrencyIcon, idCurrencySymbol) = createRefs()
                Text(
                    modifier =
                        Modifier
                            .padding(horizontal = 4.dp)
                            .constrainAs(idCurrencyIcon) {
                                baseline.linkTo(idTitle.baseline)
                                start.linkTo(idTitle.end)
                            },
                    text = currencyIcon,
                    style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary),
                )
                val currencySymbol = CurrencyUtils.getCurrencySignBy(profileUiItem.currencyCode)
                Text(
                    modifier =
                        Modifier.constrainAs(idCurrencySymbol) {
                            baseline.linkTo(idCurrencyIcon.baseline)
                            start.linkTo(idCurrencyIcon.end)
                        },
                    text = currencySymbol,
                    style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary),
                )
            }

            is ProfileUiItem.BudgetUiItem ->
                CurrencyTextFieldExtraSmall(
                    amount = profileUiItem.budgetData.sum.reduceScaleStr(),
                    currencyCode = profileUiItem.budgetData.currencyCode.getSymbolByCode(),
                    modifier =
                        Modifier
                            .padding(horizontal = 4.dp)
                            .constrainAs(idData) {
                                baseline.linkTo(idTitle.baseline)
                                start.linkTo(idTitle.end)
                            },
                    style =
                        MaterialTheme.typography.titleSmall
                            .copy(
                                color = MaterialTheme.colorScheme.tertiary,
                            ),
                )

            is ProfileUiItem.Settings -> {
                val isPremium = profileUiItem.isPremium
                val (backgroundColor, text, textColor) =
                    if (isPremium) {
                        Triple(
                            MaterialTheme.colorScheme.secondaryContainer,
                            stringResource(R.string.settings_subscription_premium_acknowledged_title),
                            MaterialTheme.colorScheme.onSecondaryContainer,
                        )
                    } else {
                        Triple(
                            MaterialTheme.colorScheme.tertiaryContainer,
                            stringResource(R.string.settings_subscription_premium_title),
                            MaterialTheme.colorScheme.onTertiaryContainer,
                        )
                    }
                Text(
                    modifier =
                        Modifier
                            .padding(
                                horizontal = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_large),
                            ).constrainAs(idData) {
                                baseline.linkTo(idTitle.baseline)
                                start.linkTo(idTitle.end)
                            }.background(
                                color = backgroundColor,
                                shape = RoundedCornerShape(50),
                            ).padding(all = 8.dp),
                    style =
                        MaterialTheme.typography.labelSmall.copy(
                            color = textColor,
                        ),
                    text = text,
                )
            }

            else -> {}
        }

        subtitle?.let {
            Text(
                text = it,
                modifier =
                    Modifier
                        .constrainAs(idSubtitle) {
                            start.linkTo(idTitle.start)
                            end.linkTo(parent.end)
                            top.linkTo(idTitle.bottom)
                            bottom.linkTo(idDivider.top)
                            width = Dimension.fillToConstraints
                        }.padding(end = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_medium)),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.tertiary),
            )
        }
        HorizontalDivider(
            modifier =
                Modifier
                    .padding(horizontal = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_medium))
                    .constrainAs(idDivider) { bottom.linkTo(parent.bottom) },
        )
    }
}

data class ProfileItemData(
    val icon: ImageVector,
    val title: String,
    val subtitle: String? = null,
    val data: String? = null,
)

private const val PREVIEW_USER_ID = "user123"
private const val PREVIEW_CURRENCY = "EUR"
private const val PREVIEW_DATE_JAN_01 = "2024-01-01T00:00:00"
private const val PREVIEW_DATE_JAN_05 = "2024-01-05T00:00:00"
private const val PREVIEW_DATE_JAN_10 = "2024-01-10T00:00:00"
private const val PREVIEW_DATE_JAN_15 = "2024-01-15T00:00:00"

@Preview(showBackground = true)
@Composable
fun DefaultPreviewProfile() {
    ProfileScreen(
        modifier = Modifier,
        ProfileUiState(
            userProfile =
                UserUiProfile(
                    name = "John Doe",
                    photo = null,
                ),
            currency = ProfileUiItem.CurrencyUiItem(currencyCode = PREVIEW_CURRENCY),
            budgetData =
                ProfileUiItem.BudgetUiItem(
                    budgetData =
                        com.d9tilov.android.budget.domain.model.BudgetData(
                            id = 1L,
                            clientId = PREVIEW_USER_ID,
                            currencyCode = PREVIEW_CURRENCY,
                            sum = java.math.BigDecimal("5000.00"),
                            saveSum = java.math.BigDecimal("1200.00"),
                            createdDate = kotlinx.datetime.LocalDateTime.parse("2024-01-15T10:30:00"),
                        ),
                ),
            regularIncomes =
                ProfileUiItem.RegularIncomeUiItem(
                    regularIncomes =
                        listOf(
                            com.d9tilov.android.transaction.regular.domain.model.RegularTransaction(
                                id = 1L,
                                clientId = PREVIEW_USER_ID,
                                currencyCode = PREVIEW_CURRENCY,
                                type = com.d9tilov.android.core.model.TransactionType.INCOME,
                                sum = java.math.BigDecimal("3500.00"),
                                category =
                                    com.d9tilov.android.category.domain.entity.Category.EMPTY_INCOME.copy(
                                        id = 1L,
                                        name = "Salary",
                                    ),
                                createdDate = kotlinx.datetime.LocalDateTime.parse(PREVIEW_DATE_JAN_01),
                                executionPeriod =
                                    com.d9tilov.android.core.model.ExecutionPeriod.EveryMonth(
                                        dayOfMonth = 1,
                                        lastExecDate = kotlinx.datetime.LocalDateTime.parse(PREVIEW_DATE_JAN_01),
                                    ),
                                description = "Monthly salary",
                                pushEnabled = true,
                                autoAdd = true,
                            ),
                            com.d9tilov.android.transaction.regular.domain.model.RegularTransaction(
                                id = 2L,
                                clientId = PREVIEW_USER_ID,
                                currencyCode = PREVIEW_CURRENCY,
                                type = com.d9tilov.android.core.model.TransactionType.INCOME,
                                sum = java.math.BigDecimal("500.00"),
                                category =
                                    com.d9tilov.android.category.domain.entity.Category.EMPTY_INCOME.copy(
                                        id = 2L,
                                        name = "Freelance",
                                    ),
                                createdDate = kotlinx.datetime.LocalDateTime.parse(PREVIEW_DATE_JAN_01),
                                executionPeriod =
                                    com.d9tilov.android.core.model.ExecutionPeriod.EveryMonth(
                                        dayOfMonth = 15,
                                        lastExecDate = kotlinx.datetime.LocalDateTime.parse(PREVIEW_DATE_JAN_15),
                                    ),
                                description = "Side project",
                                pushEnabled = true,
                                autoAdd = false,
                            ),
                        ),
                ),
            regularExpenses =
                ProfileUiItem.RegularExpenseUiItem(
                    regularExpenses =
                        listOf(
                            com.d9tilov.android.transaction.regular.domain.model.RegularTransaction(
                                id = 3L,
                                clientId = PREVIEW_USER_ID,
                                currencyCode = PREVIEW_CURRENCY,
                                type = com.d9tilov.android.core.model.TransactionType.EXPENSE,
                                sum = java.math.BigDecimal("1200.00"),
                                category =
                                    com.d9tilov.android.category.domain.entity.Category.EMPTY_EXPENSE.copy(
                                        id = 3L,
                                        name = "Rent",
                                    ),
                                createdDate = kotlinx.datetime.LocalDateTime.parse(PREVIEW_DATE_JAN_01),
                                executionPeriod =
                                    com.d9tilov.android.core.model.ExecutionPeriod.EveryMonth(
                                        dayOfMonth = 5,
                                        lastExecDate = kotlinx.datetime.LocalDateTime.parse(PREVIEW_DATE_JAN_05),
                                    ),
                                description = "Apartment rent",
                                pushEnabled = true,
                                autoAdd = true,
                            ),
                            com.d9tilov.android.transaction.regular.domain.model.RegularTransaction(
                                id = 4L,
                                clientId = PREVIEW_USER_ID,
                                currencyCode = PREVIEW_CURRENCY,
                                type = com.d9tilov.android.core.model.TransactionType.EXPENSE,
                                sum = java.math.BigDecimal("50.00"),
                                category =
                                    com.d9tilov.android.category.domain.entity.Category.EMPTY_EXPENSE.copy(
                                        id = 4L,
                                        name = "Internet",
                                    ),
                                createdDate = kotlinx.datetime.LocalDateTime.parse(PREVIEW_DATE_JAN_01),
                                executionPeriod =
                                    com.d9tilov.android.core.model.ExecutionPeriod.EveryMonth(
                                        dayOfMonth = 10,
                                        lastExecDate = kotlinx.datetime.LocalDateTime.parse(PREVIEW_DATE_JAN_10),
                                    ),
                                description = "Home internet",
                                pushEnabled = true,
                                autoAdd = true,
                            ),
                            com.d9tilov.android.transaction.regular.domain.model.RegularTransaction(
                                id = 5L,
                                clientId = PREVIEW_USER_ID,
                                currencyCode = PREVIEW_CURRENCY,
                                type = com.d9tilov.android.core.model.TransactionType.EXPENSE,
                                sum = java.math.BigDecimal("80.00"),
                                category =
                                    com.d9tilov.android.category.domain.entity.Category.EMPTY_EXPENSE.copy(
                                        id = 5L,
                                        name = "Gym",
                                    ),
                                createdDate = kotlinx.datetime.LocalDateTime.parse(PREVIEW_DATE_JAN_01),
                                executionPeriod =
                                    com.d9tilov.android.core.model.ExecutionPeriod.EveryMonth(
                                        dayOfMonth = 1,
                                        lastExecDate = kotlinx.datetime.LocalDateTime.parse(PREVIEW_DATE_JAN_01),
                                    ),
                                description = "Monthly membership",
                                pushEnabled = true,
                                autoAdd = true,
                            ),
                            com.d9tilov.android.transaction.regular.domain.model.RegularTransaction(
                                id = 6L,
                                clientId = PREVIEW_USER_ID,
                                currencyCode = PREVIEW_CURRENCY,
                                type = com.d9tilov.android.core.model.TransactionType.EXPENSE,
                                sum = java.math.BigDecimal("120.00"),
                                category =
                                    com.d9tilov.android.category.domain.entity.Category.EMPTY_EXPENSE.copy(
                                        id = 6L,
                                        name = "Phone",
                                    ),
                                createdDate = kotlinx.datetime.LocalDateTime.parse(PREVIEW_DATE_JAN_01),
                                executionPeriod =
                                    com.d9tilov.android.core.model.ExecutionPeriod.EveryMonth(
                                        dayOfMonth = 15,
                                        lastExecDate = kotlinx.datetime.LocalDateTime.parse(PREVIEW_DATE_JAN_15),
                                    ),
                                description = "Mobile plan",
                                pushEnabled = true,
                                autoAdd = true,
                            ),
                            com.d9tilov.android.transaction.regular.domain.model.RegularTransaction(
                                id = 7L,
                                clientId = PREVIEW_USER_ID,
                                currencyCode = PREVIEW_CURRENCY,
                                type = com.d9tilov.android.core.model.TransactionType.EXPENSE,
                                sum = java.math.BigDecimal("250.00"),
                                category =
                                    com.d9tilov.android.category.domain.entity.Category.EMPTY_EXPENSE.copy(
                                        id = 7L,
                                        name = "Groceries12345678901234567",
                                    ),
                                createdDate = kotlinx.datetime.LocalDateTime.parse(PREVIEW_DATE_JAN_01),
                                executionPeriod =
                                    com.d9tilov.android.core.model.ExecutionPeriod.EveryWeek(
                                        dayOfWeek = 1,
                                        lastExecDate = kotlinx.datetime.LocalDateTime.parse(PREVIEW_DATE_JAN_01),
                                    ),
                                description = "Weekly shopping",
                                pushEnabled = true,
                                autoAdd = false,
                            ),
                        ),
                ),
            settings = ProfileUiItem.Settings(isPremium = true),
        ),
        false,
        {},
        {},
        {},
        {},
        {},
        {},
        {},
        {},
    )
}
