package smartcaravans.constat.client.main.presentation.navigation.routes.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import smartcaravans.constat.client.R
import smartcaravans.constat.client.core.presentation.DateField
import smartcaravans.constat.client.core.presentation.MyTextField

@Composable
fun PersonalInfoFormScreen(
    formState: PersonalInfoFormState,
    onFormStateChange: (PersonalInfoFormState) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier.verticalScroll(rememberScrollState()).fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MyTextField(
            formState.firstName,
            {
                onFormStateChange(formState.copy(firstName = it))
            },
            label = stringResource(R.string.first_name),
            error = formState.firstNameError,
            autoCompleteContentType = ContentType.PersonFirstName
        )
        MyTextField(
            formState.lastName,
            {
                onFormStateChange(formState.copy(lastName = it))
            },
            label = stringResource(R.string.last_name),
            error = formState.lastNameError,
            autoCompleteContentType = ContentType.PersonLastName
        )
        MyTextField(
            formState.address,
            {
                onFormStateChange(formState.copy(address = it))
            },
            label = stringResource(R.string.phone_number),
            error = formState.addressError,
            autoCompleteContentType = ContentType.AddressStreet
        )
        MyTextField(
            formState.licenseNumber,
            {
                onFormStateChange(formState.copy(licenseNumber = it))
            },
            label = stringResource(R.string.license_number),
            error = formState.licenseNumberError,
        )
        Row(Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MyTextField(
                formState.licenseGivenBy,
                {
                    onFormStateChange(formState.copy(licenseGivenBy = it))
                },
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.given_by),
                error = formState.licenseGivenByError,
            )
            DateField(
                formState.licenseGivenAt,
                {
                    it?.let {
                        onFormStateChange(formState.copy(licenseGivenAt = it))
                    }
                },
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.given_at),
                error = formState.licenseGivenAtError,
            )
        }
    }
}