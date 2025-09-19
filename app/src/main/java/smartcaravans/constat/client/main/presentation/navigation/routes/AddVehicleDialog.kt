package smartcaravans.constat.client.main.presentation.navigation.routes

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import smartcaravans.constat.client.R
import smartcaravans.constat.client.core.presentation.MyTextField
import smartcaravans.constat.client.core.presentation.util.Field
import smartcaravans.constat.client.core.presentation.util.InputError
import smartcaravans.constat.client.main.presentation.viewmodel.CreateVehicleDialogState
import younesbouhouche.musicplayer.core.presentation.util.ExpressiveButton

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AddVehicleDialog(
    state: CreateVehicleDialogState,
    onChange: (CreateVehicleDialogState) -> Unit,
    visible: Boolean,
    onConfirmRequest: () -> Unit,
    onDismissRequest: () -> Unit
) {
    var makeError by remember { mutableStateOf<InputError?>(null) }
    var modelError by remember { mutableStateOf<InputError?>(null) }
    var typeError by remember { mutableStateOf<InputError?>(null) }
    var registrationError by remember { mutableStateOf<InputError?>(null) }

    val isEditing = state.id.isNotBlank()

    if (visible) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = if (isEditing) "Edit Vehicle" else "Add Vehicle",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Make field
                    MyTextField(
                        value = state.make,
                        onValueChange = {
                            onChange(state.copy(make = it))
                            makeError = null
                        },
                        label = "Make",
                        placeholder = "e.g., Toyota, Honda",
                        error = makeError,
                        keyboardType = KeyboardType.Text,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Model field
                    MyTextField(
                        value = state.model,
                        onValueChange = {
                            onChange(state.copy(model = it))
                            modelError = null
                        },
                        label = "Model",
                        placeholder = "e.g., Camry, Civic",
                        error = modelError,
                        keyboardType = KeyboardType.Text,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Type field
                    MyTextField(
                        value = state.type,
                        onValueChange = {
                            onChange(state.copy(type = it))
                            typeError = null
                        },
                        label = "Type",
                        placeholder = "e.g., Sedan, SUV, Truck",
                        error = typeError,
                        keyboardType = KeyboardType.Text,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Registration Number field
                    MyTextField(
                        value = state.registrationNumber,
                        onValueChange = {
                            onChange(state.copy(registrationNumber = it.uppercase()))
                            registrationError = null
                        },
                        label = "Registration Number",
                        placeholder = "e.g., ABC-123",
                        error = registrationError,
                        keyboardType = KeyboardType.Text,
                        modifier = Modifier.fillMaxWidth()
                    )


                    Row(
                        Modifier.fillMaxWidth().padding(top = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        repeat(2) { index ->
                            val interactionSource = remember { MutableInteractionSource() }
                            val pressed by interactionSource.collectIsPressedAsState()
                            val weight by animateFloatAsState(
                                if (pressed) 1.5f else 1f
                            )
                            ExpressiveButton(
                                text =
                                    if (index == 0) stringResource(R.string.close)
                                    else stringResource(R.string.add_vehicle),
                                size = ButtonDefaults.MediumContainerHeight,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp)
                                    .weight(weight),
                                outlined = index == 0,
                                interactionSource = interactionSource,
                                onClick = if (index == 0) onDismissRequest else onConfirmRequest
                            )
                        }
                    }
                }
            }
        }
    }
}