package smartcaravans.constat.client.main.presentation.navigation.routes.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import se.warting.signaturepad.SignaturePadAdapter
import se.warting.signaturepad.SignaturePadView
import smartcaravans.constat.client.R
import smartcaravans.constat.client.core.presentation.DateField
import smartcaravans.constat.client.core.presentation.MyTextField
import smartcaravans.constat.client.main.presentation.components.SignaturePad

@Composable
fun InsuranceInfoFormScreen(
    formState: InsuranceFormState,
    onStateChange: (InsuranceFormState) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier.verticalScroll(rememberScrollState()).fillMaxSize().padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MyTextField(
            formState.insuranceCompany,
            {
                onStateChange(formState.copy(insuranceCompany = it))
            },
            label = stringResource(R.string.insurance_company),
            error = formState.insuranceCompanyError,
        )
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