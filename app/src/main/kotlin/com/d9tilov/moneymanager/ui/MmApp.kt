package com.d9tilov.moneymanager.ui

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.d9tilov.android.common.android.ui.permissions.PermissionBox
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.core.utils.CurrencyUtils
import com.d9tilov.android.designsystem.SimpleDialog
import com.d9tilov.android.designsystem.component.MmBackground
import com.d9tilov.android.designsystem.component.MmNavigationBar
import com.d9tilov.android.designsystem.component.MmNavigationBarItem
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.home.LocationCurrencyState
import com.d9tilov.moneymanager.navigation.MmNavHost
import com.d9tilov.moneymanager.navigation.TopLevelDestination

@Composable
@RequiresPermission(anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
fun MmApp(
    windowSizeClass: WindowSizeClass,
    appState: MmAppState = rememberMmAppState(windowSizeClass = windowSizeClass),
    locationCurrencyState: LocationCurrencyState,
    onLocationRequest: (permissions: List<String>) -> Unit,
    onDismissClicked: (currencyCode: String?) -> Unit,
    onConfirmClicked: (currencyCode: String?) -> Unit,
) {
    MmBackground {
        val snackBarHostState = remember { SnackbarHostState() }
        Scaffold(
            modifier = Modifier.semantics { testTagsAsResourceId = true },
            contentColor = MaterialTheme.colorScheme.onBackground,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = { SnackbarHost(snackBarHostState) },
            bottomBar = {
                if (appState.shouldShowBottomBar) {
                    MmBottomBar(
                        destinations = appState.topLevelDestinations,
                        onNavigateToDestination = appState::navigateToTopLevelDestination,
                        currentDestination = appState.currentDestination,
                        modifier = Modifier.testTag("MmBottomBar"),
                    )
                }
            },
        ) { padding ->
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal,
                        ),
                    ),
            ) {
                Column(Modifier.fillMaxSize()) {
                    MmNavHost(
                        appState = appState,
                        onShowSnackBar = { message, action ->
                            snackBarHostState.showSnackbar(
                                message = message,
                                actionLabel = action,
                                duration = SnackbarDuration.Short,
                            ) == SnackbarResult.ActionPerformed
                        },
                    )
                }
            }
            SimpleDialog(
                show = locationCurrencyState.showDialog,
                title = stringResource(R.string.location_currency_dialog_title),
                subtitle =
                    stringResource(
                        R.string.location_currency_dialog_subtitle,
                        "${locationCurrencyState.currencyCode} ${
                            CurrencyUtils.getCurrencyIcon(
                                locationCurrencyState.currencyCode
                                    ?: DEFAULT_CURRENCY_CODE,
                            )
                        }",
                    ),
                dismissButton = stringResource(com.d9tilov.android.common.android.R.string.dismiss),
                confirmButton = stringResource(com.d9tilov.android.common.android.R.string.change),
                onConfirm = { onConfirmClicked(locationCurrencyState.currencyCode) },
                onDismiss = { onDismissClicked(locationCurrencyState.currencyCode) },
            )
            PermissionBox(
                permissions = listOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                requiredPermissions = listOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                permissionTitle = stringResource(R.string.permissions_location),
                permissionExplanation = stringResource(R.string.permissions_location_explanation),
            ) { permissions: List<String> -> onLocationRequest(permissions) }
        }
    }
}

@Composable
private fun MmBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    MmNavigationBar(
        modifier = modifier,
    ) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            MmNavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        imageVector = destination.unselectedIcon,
                        contentDescription = null,
                    )
                },
                selectedIcon = {
                    Icon(
                        imageVector = destination.selectedIcon,
                        contentDescription = null,
                    )
                },
                label = { Text(stringResource(destination.iconTextId)) },
                modifier = Modifier,
            )
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false
