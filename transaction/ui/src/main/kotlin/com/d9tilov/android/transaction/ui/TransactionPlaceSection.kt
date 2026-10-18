package com.d9tilov.android.transaction.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuBoxScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.designsystem.MoneyManagerIcons
import com.d9tilov.android.places.domain.model.NearbyPlace
import com.d9tilov.android.transaction.domain.model.Transaction
import com.d9tilov.android.transaction.ui.model.TransactionInfoMode
import com.d9tilov.android.transaction.ui.vm.NearbyPlacesStatus
import com.d9tilov.android.transaction.ui.vm.TransactionInfoUiState

@Composable
internal fun TransactionPlaceSection(
    uiState: TransactionInfoUiState,
    actions: TransactionPlaceActions,
    modifier: Modifier = Modifier,
) {
    if (uiState.transaction.type != TransactionType.EXPENSE) return
    val name = uiState.transaction.place.name
    if (uiState.mode == TransactionInfoMode.VIEW && name.isNullOrBlank()) return
    Column(
        modifier = modifier,
        verticalArrangement =
            Arrangement.spacedBy(
                dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_small),
            ),
    ) {
        if (uiState.mode == TransactionInfoMode.EDIT) {
            TransactionPlaceNameRow(uiState, actions)
        } else {
            Text(stringResource(R.string.transaction_place_title), style = MaterialTheme.typography.labelLarge)
            SelectedPlaceDetails(uiState.transaction)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionPlaceNameRow(
    uiState: TransactionInfoUiState,
    actions: TransactionPlaceActions,
) {
    var customNameVisible by rememberSaveable { mutableStateOf(false) }
    val onToggle = { if (uiState.placeState.pickerVisible) actions.onDismiss() else actions.onEdit() }
    ExposedDropdownMenuBox(
        expanded = uiState.placeState.pickerVisible,
        onExpandedChange = { onToggle() },
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            SelectedPlaceDetails(uiState.transaction, modifier = Modifier.weight(1f))
            PlaceEditButton(
                loading = uiState.placeState.status == NearbyPlacesStatus.LOADING,
                onClick = onToggle,
                modifier =
                    Modifier.menuAnchor(
                        type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                        enabled = uiState.placeState.status != NearbyPlacesStatus.LOADING,
                    ),
            )
        }
        PlaceSelectionMenu(
            uiState = uiState,
            actions = actions,
            onCustomName = {
                actions.onDismiss()
                customNameVisible = true
            },
        )
    }
    if (customNameVisible) {
        CustomPlaceNameDialog(
            name =
                uiState.transaction.place.name
                    .orEmpty(),
            onNameChanged = actions.onNameChanged,
            onDismiss = { customNameVisible = false },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExposedDropdownMenuBoxScope.PlaceSelectionMenu(
    uiState: TransactionInfoUiState,
    actions: TransactionPlaceActions,
    onCustomName: () -> Unit,
) {
    ExposedDropdownMenu(
        expanded = uiState.placeState.pickerVisible,
        onDismissRequest = actions.onDismiss,
        modifier = Modifier.heightIn(max = dimensionResource(R.dimen.transaction_places_menu_max_height)),
        shape = RoundedCornerShape(dimensionResource(R.dimen.transaction_places_menu_corner_radius)),
    ) {
        Column(
            modifier =
                Modifier.padding(
                    horizontal = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_expanded),
                    vertical = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_medium),
                ),
        ) {
            PlaceListStatus(uiState)
            PlaceMenuItem(onClick = onCustomName) {
                Text(stringResource(R.string.transaction_place_custom_name), style = MaterialTheme.typography.bodyLarge)
            }
            uiState.placeState.places.forEach { place ->
                HorizontalDivider()
                PlaceMenuItem(onClick = { actions.onPlaceSelected(place) }) { PlaceDetails(place) }
            }
        }
    }
}

@Composable
private fun SelectedPlaceDetails(
    transaction: Transaction,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text =
                transaction.place.name
                    .orEmpty()
                    .ifBlank { stringResource(R.string.transaction_place_not_selected) },
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        if (!transaction.place.name.isNullOrBlank() && !transaction.place.address.isNullOrBlank()) {
            Text(
                text = transaction.place.address.orEmpty(),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun PlaceMenuItem(
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    DropdownMenuItem(
        text = content,
        onClick = onClick,
        modifier = Modifier.heightIn(min = dimensionResource(R.dimen.transaction_place_item_min_height)),
        contentPadding =
            PaddingValues(
                horizontal = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_medium),
                vertical = dimensionResource(com.d9tilov.android.designsystem.R.dimen.padding_small),
            ),
    )
}

@Composable
private fun PlaceEditButton(
    loading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(onClick = onClick, enabled = !loading, modifier = modifier) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(dimensionResource(R.dimen.transaction_place_loader_size)),
                strokeWidth = dimensionResource(R.dimen.transaction_place_loader_stroke_width),
            )
        } else {
            Icon(
                imageVector = MoneyManagerIcons.Edit,
                contentDescription = stringResource(R.string.transaction_place_edit),
            )
        }
    }
}

@Composable
private fun PlaceListStatus(uiState: TransactionInfoUiState) {
    val message =
        when (uiState.placeState.status) {
            NearbyPlacesStatus.UNAVAILABLE -> {
                R.string.transaction_place_unavailable
            }

            NearbyPlacesStatus.NO_LOCATION -> {
                R.string.transaction_place_no_location
            }

            NearbyPlacesStatus.READY -> {
                if (uiState.placeState.places.isEmpty()) R.string.transaction_place_empty else null
            }

            else -> {
                null
            }
        }
    if (message != null) {
        Text(stringResource(message), style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun CustomPlaceNameDialog(
    name: String,
    onNameChanged: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var draft by rememberSaveable(name) { mutableStateOf(name) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.transaction_place_name)) },
        text = {
            OutlinedTextField(
                value = draft,
                onValueChange = { draft = it },
                singleLine = true,
            )
        },
        confirmButton = {
            TextButton(onClick = {
                onNameChanged(draft)
                onDismiss()
            }) { Text(stringResource(com.d9tilov.android.common.android.R.string.ok)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(com.d9tilov.android.common.android.R.string.cancel)) }
        },
    )
}

@Composable
private fun PlaceDetails(place: NearbyPlace) {
    Column {
        Text(place.name, style = MaterialTheme.typography.bodyLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
        if (place.address.isNotBlank()) {
            Text(
                place.address,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Text(
            stringResource(R.string.transaction_place_distance, place.distanceMeters),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
internal fun TransactionPlaceAttribution(
    uiState: TransactionInfoUiState,
    modifier: Modifier = Modifier,
) {
    if (uiState.transaction.type != TransactionType.EXPENSE) return
    if (uiState.placeState.places.isEmpty() &&
        uiState.transaction.place.name
            .isNullOrBlank()
    ) {
        return
    }
    val uriHandler = LocalUriHandler.current
    Row(modifier = modifier) {
        TextButton(onClick = { uriHandler.openUri("https://www.geoapify.com/") }) {
            Text(stringResource(R.string.transaction_place_geoapify), style = MaterialTheme.typography.labelSmall)
        }
        TextButton(onClick = { uriHandler.openUri("https://www.openstreetmap.org/copyright") }) {
            Text(stringResource(R.string.transaction_place_osm), style = MaterialTheme.typography.labelSmall)
        }
    }
}
