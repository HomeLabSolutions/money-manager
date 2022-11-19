package com.d9tilov.moneymanager.profile.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.d9tilov.moneymanager.BuildConfig
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.backup.PeriodicBackupWorker
import com.d9tilov.moneymanager.core.ui.views.CurrencyTextFieldExtraSmall
import com.d9tilov.moneymanager.core.ui.views.SimpleDialog
import com.d9tilov.moneymanager.core.util.CurrencyUtils
import com.d9tilov.moneymanager.core.util.CurrencyUtils.getSymbolByCode
import com.d9tilov.moneymanager.core.util.toBudgetCreatedDateStr
import com.d9tilov.moneymanager.profile.ui.vm.ProfileUiItem
import com.d9tilov.moneymanager.profile.ui.vm.ProfileUiState
import com.d9tilov.moneymanager.profile.ui.vm.ProfileViewModel2
import com.d9tilov.moneymanager.splash.ui.RouterActivity
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import dagger.hilt.android.internal.managers.FragmentComponentManager

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun ProfileScreen(viewModel: ProfileViewModel2 = hiltViewModel()) {
    val state: ProfileUiState by viewModel.profileState.collectAsStateWithLifecycle()
    val showDialog by viewModel.showDialog.collectAsStateWithLifecycle()
    val context = LocalContext.current
    ProfileScreen(
        state,
        showDialog,
        {},
        {},
        {},
        {},
        {},
        {},
        { viewModel.showDialog() },
        {
            viewModel.logout {
                PeriodicBackupWorker.stopPeriodicJob(context)
                context.startActivity(
                    Intent(context, RouterActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )
                (FragmentComponentManager.findActivity(context) as Activity).finish()
            }
        },
        { viewModel.dismissDialog() }
    )
}

@Composable
fun ProfileScreen(
    state: ProfileUiState,
    showDialog: Boolean,
    onCurrencyClicked: () -> Unit,
    onBudgetClicked: () -> Unit,
    onRegularIncomeClicked: () -> Unit,
    onRegularExpenseClicked: () -> Unit,
    onGoalsClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onLogoutClicked: () -> Unit,
    onLogoutConfirmClicked: () -> Unit,
    onLogoutDismissClicked: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        ProfileCard(state.userProfile)
        ProfileSection(state.currency)
        ProfileSection(state.budgetData)
        ProfileSection(state.regularIncomes)
        ProfileSection(state.regularExpenses)
        ProfileSection(ProfileUiItem.Goals)
        ProfileSection(ProfileUiItem.Settings)
        Spacer(modifier = Modifier.weight(1f))
        OutlinedButton(
            modifier = Modifier.padding(top = dimensionResource(R.dimen.standard_padding)),
            onClick = { onLogoutClicked() },
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.outlinedButtonColors(backgroundColor = MaterialTheme.colorScheme.error)
        ) {
            Text(
                text = stringResource(R.string.profile_logout),
                color = MaterialTheme.colorScheme.onError
            )
        }
        Text(
            text = BuildConfig.VERSION_NAME,
            modifier = Modifier.padding(dimensionResource(R.dimen.standard_padding)),
            style = MaterialTheme.typography.bodySmall,
        )
        SimpleDialog(
            show = showDialog,
            title = stringResource(R.string.logout_dialog_title),
            subtitle = stringResource(R.string.logout_dialog_subtitle),
            dismissButton = stringResource(R.string.cancel),
            confirmButton = stringResource(R.string.logout_dialog_button),
            onConfirm = { onLogoutConfirmClicked() },
            onDismiss = { onLogoutDismissClicked() }
        )
    }
}

@Composable
fun ProfileCard(userProfile: UserProfile?) {
    Column {
        Card(
            modifier = Modifier
                .padding(top = 32.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.Top),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = CutCornerShape(topEnd = 24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ProfilePicture(userProfile?.photoUrl)
                ProfileContent(userProfile?.displayedName)
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
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(uri)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .size(72.dp)
        )
    }
}

@Composable
fun ProfileContent(name: String?) {
    Text(
        name ?: "",
        modifier = Modifier.padding(16.dp),
        style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onPrimaryContainer)
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileSection(profileUiItem: ProfileUiItem) {
    val (icon, title, subtitle) = when (profileUiItem) {
        is ProfileUiItem.CurrencyUiItem -> ProfileItemData(
            ImageVector.vectorResource(R.drawable.ic_currency_icon),
            stringResource(R.string.profile_item_currency_title),
            null
        )
        is ProfileUiItem.BudgetUiItem -> ProfileItemData(
            ImageVector.vectorResource(R.drawable.ic_budget_icon),
            stringResource(R.string.profile_item_budget_title),
            profileUiItem.budgetData.createdDate.toBudgetCreatedDateStr()
        )
        is ProfileUiItem.RegularIncomeUiItem -> ProfileItemData(
            ImageVector.vectorResource(R.drawable.ic_regular_income),
            stringResource(R.string.profile_item_regular_incomes_title),
            profileUiItem.regularIncomes.joinToString(separator = ", ") { it.category.name },
            null
        )
        is ProfileUiItem.RegularExpenseUiItem -> ProfileItemData(
            ImageVector.vectorResource(R.drawable.ic_regular_expense),
            stringResource(R.string.profile_item_regular_expenses_title),
            profileUiItem.regularExpenses.joinToString(separator = ", ") { it.category.name }
        )
        is ProfileUiItem.Goals -> ProfileItemData(
            ImageVector.vectorResource(R.drawable.ic_goal),
            stringResource(R.string.profile_item_goals_title_empty),
            null,
        )
        is ProfileUiItem.Settings -> ProfileItemData(
            ImageVector.vectorResource(R.drawable.ic_settings),
            stringResource(R.string.profile_item_settings_title),
            null,
        )
    }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = if (profileUiItem is ProfileUiItem.CurrencyUiItem) dimensionResource(R.dimen.standard_large_padding) else 0.dp)
            .height(dimensionResource(R.dimen.profile_item_height))
    ) {
        val (idIcon, idTitle, idData, idSubtitle, idDivider) = createRefs()
        Icon(
            modifier = Modifier
                .padding(
                    start = dimensionResource(R.dimen.standard_large_padding),
                    end = dimensionResource(R.dimen.standard_padding)
                )
                .constrainAs(idIcon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                },
            imageVector = icon,
            contentDescription = "content description",
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = title,
            modifier = Modifier.constrainAs(idTitle) {
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
                    modifier = Modifier.padding(horizontal = 4.dp)
                        .constrainAs(idCurrencyIcon) {
                            baseline.linkTo(idTitle.baseline)
                            start.linkTo(idTitle.end)
                        },
                    text = currencyIcon,
                    style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary)
                )
                val currencySymbol = CurrencyUtils.getCurrencySignBy(profileUiItem.currencyCode)
                Text(
                    modifier = Modifier.constrainAs(idCurrencySymbol) {
                        baseline.linkTo(idCurrencyIcon.baseline)
                        start.linkTo(idCurrencyIcon.end)
                    },
                    text = currencySymbol,
                    style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary)
                )
            }
            is ProfileUiItem.BudgetUiItem -> CurrencyTextFieldExtraSmall(
                amount = profileUiItem.budgetData.sum.toString(),
                currencyCode = profileUiItem.budgetData.currencyCode.getSymbolByCode(),
                modifier = Modifier.padding(horizontal = 4.dp).constrainAs(idData) {
                    baseline.linkTo(idTitle.baseline)
                    start.linkTo(idTitle.end)
                },
                style = MaterialTheme.typography.titleMedium
            )
            else -> {}
        }

        subtitle?.let {
            Text(
                text = it,
                modifier = Modifier.constrainAs(idSubtitle) {
                    start.linkTo(idTitle.start)
                    top.linkTo(idTitle.bottom)
                    bottom.linkTo(idDivider.top)
                },
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.tertiary),
            )
        }
        Divider(
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.standard_padding))
                .constrainAs(idDivider) { bottom.linkTo(parent.bottom) },
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.primaryContainer
        )
    }
}

data class ProfileItemData(
    val icon: ImageVector,
    val title: String,
    val subtitle: String? = null,
    val data: String? = null
)

@Preview(showBackground = true)
@Composable
fun DefaultPreviewProfile() {
    ProfileScreen(
        ProfileUiState(userProfile = UserProfile.EMPTY.copy(displayedName = "First name")),
        false,
        {},
        {},
        {},
        {},
        {},
        {},
        {},
        {},
        {})
}