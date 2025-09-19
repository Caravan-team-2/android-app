package smartcaravans.constat.client.auth.presentation.routes

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.materialkolor.ktx.of
import org.koin.compose.viewmodel.koinViewModel
import smartcaravans.constat.client.R
import smartcaravans.constat.client.auth.presentation.viewmodel.AuthUiAction
import smartcaravans.constat.client.auth.presentation.viewmodel.AuthViewModel
import smartcaravans.constat.client.core.presentation.MyTextField
import smartcaravans.constat.client.core.presentation.PasswordTextField
import smartcaravans.constat.client.ui.theme.AppTheme
import younesbouhouche.musicplayer.core.presentation.util.ExpressiveButton

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()
    Column(modifier
        .systemBarsPadding()
        .padding(24.dp)
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(40.dp, Alignment.CenterVertically)
    ) {
        Column(Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(
                painterResource(id = R.drawable.app_icon),
                null,
                Modifier.size(60.dp),
                tint = MaterialTheme.colorScheme.surface,
            )
            Text(
                stringResource(
                    if (uiState.login) R.string.login_title
                    else R.string.signup_title,
                ),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.surface,
            )
            Text(
                stringResource(
                    R.string.login_text,
                ),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.surface,
            )
        }
        Surface(
            Modifier.weight(1f, false),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                Modifier.verticalScroll(rememberScrollState())
                    .imePadding()
                    .padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AnimatedVisibility(!uiState.login) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            MyTextField(
                                uiState.firstName,
                                { viewModel.onAction(AuthUiAction.SetFirstName(it)) },
                                Modifier.weight(1f),
                                label = stringResource(R.string.first_name),
                                error = uiState.firstNameError,
                                autoCompleteContentType = ContentType.PersonFirstName,
                                enabled = !uiState.loading
                            )
                            MyTextField(
                                uiState.lastName,
                                { viewModel.onAction(AuthUiAction.SetLastName(it)) },
                                Modifier.weight(1f),
                                label = stringResource(R.string.last_name),
                                error = uiState.lastNameError,
                                autoCompleteContentType = ContentType.PersonLastName,
                                enabled = !uiState.loading
                            )
                        }
                        MyTextField(
                            uiState.insuranceNumber,
                            { viewModel.onAction(AuthUiAction.SetInsuranceNumber(it)) },
                            Modifier.fillMaxWidth(),
                            label = stringResource(R.string.insurance_number),
                            error = uiState.insuranceNumberError,
                            keyboardType = KeyboardType.Number,
                            enabled = !uiState.loading
                        )
                        MyTextField(
                            uiState.phoneNumber,
                            { viewModel.onAction(AuthUiAction.SetPhoneNumber(it)) },
                            Modifier.fillMaxWidth(),
                            label = stringResource(R.string.phone_number),
                            error = uiState.phoneNumberError,
                            autoCompleteContentType = ContentType.PhoneNumber,
                            keyboardType = KeyboardType.Number,
                            enabled = !uiState.loading
                        )
                        MyTextField(
                            uiState.job,
                            { viewModel.onAction(AuthUiAction.SetJob(it)) },
                            Modifier.fillMaxWidth(),
                            label = stringResource(R.string.job),
                            error = uiState.jobError,
                            enabled = !uiState.loading,
                        )
                    }
                }
                MyTextField(
                    uiState.email,
                    { viewModel.onAction(AuthUiAction.SetEmail(it)) },
                    Modifier.fillMaxWidth(),
                    label = stringResource(R.string.email),
                    error = uiState.emailError,
                    autoCompleteContentType = ContentType.EmailAddress,
                    enabled = !uiState.loading,
                )
                PasswordTextField(
                    uiState.password,
                    { viewModel.onAction(AuthUiAction.SetPassword(it)) },
                    uiState.passwordHidden,
                    { viewModel.onAction(AuthUiAction.SetPasswordVisibility(it)) },
                    Modifier.fillMaxWidth(),
                    error = uiState.passwordError,
                    autoCompleteContentType = ContentType.Password,
                    enabled = !uiState.loading,
                )
                AnimatedVisibility(uiState.login) {
                    Row(Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            Modifier.offset(x = (-10).dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                uiState.remember,
                                {
                                    viewModel.onAction(AuthUiAction.SetRemember(it))
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = MaterialTheme.colorScheme.primary,
                                    uncheckedColor = MaterialTheme.colorScheme.primary,
                                    checkmarkColor = MaterialTheme.colorScheme.surface,
                                ),
                                enabled = !uiState.loading
                            )
                            Text(
                                stringResource(R.string.remember_me),
                                Modifier.offset(x = (-4).dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                        Text(
                            stringResource(R.string.forgot_password),
                            Modifier.padding(start = 8.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                AnimatedContent(
                    uiState.loading,
                    Modifier.padding(vertical = 16.dp)
                ) {
                    if (it)
                        LinearWavyProgressIndicator(Modifier.fillMaxWidth())
                    else
                        ExpressiveButton(
                            stringResource(
                                if (uiState.login) R.string.login_button
                                else R.string.signup_button,
                            ),
                            size = ButtonDefaults.MediumContainerHeight,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            viewModel.onAction(AuthUiAction.Authenticate)
                        }
                }
                FlowRow(Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        stringResource(
                            if (uiState.login) R.string.no_account
                            else R.string.have_account,
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        stringResource(
                            if (uiState.login) R.string.signup_button
                            else R.string.login_button,
                        ),
                        Modifier.padding(start = 8.dp).clickable(
                            enabled = !uiState.loading
                        ) {
                            viewModel.onAction(AuthUiAction.ToggleLogin)
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold,
                    )

                }
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    AppTheme {
        LoginScreen(koinViewModel())
    }
}