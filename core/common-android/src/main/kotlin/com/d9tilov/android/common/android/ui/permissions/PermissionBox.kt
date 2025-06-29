package com.d9tilov.android.common.android.ui.permissions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.d9tilov.android.common.android.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale

/**
 * The PermissionBox uses a [Box] to show a simple permission request UI when the provided [permission]
 * is revoked or the provided [onGranted] content if the permission is granted.
 *
 * This composable follows the permission request flow but for a complete example check the samples
 * under privacy/permissions
 */
@Composable
fun PermissionBox(
    modifier: Modifier = Modifier,
    permission: String,
    permissionTitle: String,
    permissionExplanation: String,
    contentAlignment: Alignment = Alignment.TopStart,
    onGranted: @Composable BoxScope.() -> Unit,
) {
    PermissionBox(
        modifier,
        permissions = listOf(permission),
        requiredPermissions = listOf(permission),
        permissionTitle,
        permissionExplanation,
        contentAlignment,
    ) { onGranted() }
}

/**
 * A variation of [PermissionBox] that takes a list of permissions and only calls [onGranted] when
 * all the [requiredPermissions] are granted.
 *
 * By default it assumes that all [permissions] are required.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionBox(
    modifier: Modifier = Modifier,
    permissions: List<String>,
    requiredPermissions: List<String> = permissions,
    permissionTitle: String,
    permissionExplanation: String,
    contentAlignment: Alignment = Alignment.TopStart,
    onGranted: @Composable BoxScope.(List<String>) -> Unit,
) {
    val permissionState = rememberMultiplePermissionsState(permissions = permissions) { _ -> }
    val allRequiredPermissionsGranted =
        permissionState.revokedPermissions.none { it.permission in requiredPermissions }
    var showRationale by remember(permissionState) {
        mutableStateOf(!allRequiredPermissionsGranted && permissionState.shouldShowRationale)
    }
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .then(modifier),
        contentAlignment =
            if (allRequiredPermissionsGranted) {
                contentAlignment
            } else {
                Alignment.Center
            },
    ) {
        if (allRequiredPermissionsGranted) {
            onGranted(
                permissionState.permissions
                    .filter { it.status.isGranted }
                    .map { it.permission },
            )
        } else {
            if (showRationale) {
                AlertDialog(
                    onDismissRequest = {
                        showRationale = false
                    },
                    title = { Text(text = stringResource(R.string.permissions_dialog_title)) },
                    text = {
                        Text(
                            text =
                                stringResource(
                                    R.string.permissions_dialog_subtitle,
                                    permissionTitle,
                                    permissionExplanation,
                                ),
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showRationale = false
                                permissionState.launchMultiplePermissionRequest()
                            },
                        ) {
                            Text(stringResource(R.string.str_continue))
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showRationale = false },
                        ) { Text(stringResource(R.string.dismiss)) }
                    },
                )
            } else {
                LaunchedEffect(Unit) {
                    permissionState.launchMultiplePermissionRequest()
                }
            }
        }
    }
}
