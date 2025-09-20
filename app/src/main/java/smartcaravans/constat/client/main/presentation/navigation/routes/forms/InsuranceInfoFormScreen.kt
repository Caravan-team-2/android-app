@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package smartcaravans.constat.client.main.presentation.navigation.routes.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import se.warting.signaturepad.SignaturePadAdapter
import se.warting.signaturepad.SignaturePadView
import smartcaravans.constat.client.R
import smartcaravans.constat.client.core.domain.models.InsuranceCompanies
import smartcaravans.constat.client.core.presentation.DateField
import smartcaravans.constat.client.core.presentation.MyTextField
import smartcaravans.constat.client.main.presentation.components.SignaturePad

@Composable
fun InsuranceInfoFormScreen(
    formState: InsuranceFormState,
    onStateChange: (InsuranceFormState) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val textFieldState = rememberTextFieldState()
    Column(
        modifier.verticalScroll(rememberScrollState()).fillMaxSize().padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                state = textFieldState,
                readOnly = true,
                lineLimits = TextFieldLineLimits.SingleLine,
                label = { Text(stringResource(R.string.insurance_company)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                InsuranceCompanies.entries.forEach {
                    DropdownMenuItem(
                        text = { Text(it.name, style = MaterialTheme.typography.bodyLarge) },
                        onClick = {
                            textFieldState.setTextAndPlaceCursorAtEnd(it.name)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }

        }
        MyTextField(
            formState.insuranceNumber,
            {
                onStateChange(formState.copy(insuranceNumber = it))
            },
            label = stringResource(R.string.insurance_number),
            error = formState.insuranceNumberError,
        )
        Row(Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
            DateField(
                formState.validFrom,
                {
                    it?.let {
                        onStateChange(formState.copy(validFrom = it))
                    }
                },
                label = stringResource(R.string.valid_from),
                modifier = Modifier.weight(1f),
                error = formState.validFromError,
            )
            DateField(
                formState.validTo,
                {
                    it?.let {
                        onStateChange(formState.copy(validTo = it))
                    }
                },
                label = stringResource(R.string.valid_from),
                modifier = Modifier.weight(1f),
                error = formState.validToError,
            )
        }
        SignaturePad(
            stringResource(R.string.draw),
            Modifier.padding(top = 16.dp),
            ratio = 1f
        ) {
            onStateChange(formState.copy(draw = it))
        }
        SignaturePad(
            stringResource(R.string.signature),
            Modifier.padding(top = 16.dp)
        ) {
            onStateChange(formState.copy(signature = it))
        }
    }
}