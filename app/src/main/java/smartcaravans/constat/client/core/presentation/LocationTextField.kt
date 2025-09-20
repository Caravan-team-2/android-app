package smartcaravans.constat.client.core.presentation

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import smartcaravans.constat.client.R
import smartcaravans.constat.client.core.presentation.util.InputError

// Simple location holder for UI
data class Location(
    val address: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationTextField(
    value: Location,
    onValueChange: (Location) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    placeholder: String? = null,
    error: InputError? = null,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: androidx.compose.foundation.text.KeyboardActions = androidx.compose.foundation.text.KeyboardActions.Default,
    interactionSource: MutableInteractionSource? = null
) {
    var showMapPicker by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val resolvedLabel = label ?: stringResource(R.string.address)
    val resolvedPlaceholder = placeholder ?: stringResource(R.string.address)

    MyTextField(
        value = value.address,
        onValueChange = { address ->
            onValueChange(value.copy(address = address))
        },
        modifier = modifier,
        enabled = enabled,
        label = resolvedLabel,
        readOnly = true,
        placeholder = resolvedPlaceholder,
        leadingIcon = Icons.Default.LocationOn,
        trailingIcon = {
            IconButton(
                onClick = { showMapPicker = true }
            ) {
                Icon(
                    Icons.Default.Map,
                    contentDescription = stringResource(R.string.current_location),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        error = error,
        imeAction = imeAction,
        keyboardType = KeyboardType.Text,
        keyboardActions = keyboardActions,
        singleLine = true,
        interactionSource = interactionSource
    )

    if (showMapPicker) {
        ModalBottomSheet(
            onDismissRequest = { showMapPicker = false },
            sheetState = sheetState,
            sheetGesturesEnabled = false
        ) {
            MapLocationPicker(
                currentLocation = value,
                onLocationSelected = { location ->
                    onValueChange(location)
                    showMapPicker = false
                },
                onDismiss = { showMapPicker = false }
            )
        }
    }
}
