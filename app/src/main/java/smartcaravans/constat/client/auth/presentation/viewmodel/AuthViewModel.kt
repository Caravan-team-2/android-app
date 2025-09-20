package smartcaravans.constat.client.auth.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import smartcaravans.constat.client.auth.domain.use_cases.FileUploadUseCase
import smartcaravans.constat.client.auth.domain.use_cases.LoginUseCase
import smartcaravans.constat.client.auth.domain.use_cases.SignupUseCase
import smartcaravans.constat.client.auth.presentation.navigation.NavRoutes
import smartcaravans.constat.client.core.domain.util.onError
import smartcaravans.constat.client.core.domain.util.onSuccess
import smartcaravans.constat.client.core.presentation.util.sendEvent
import smartcaravans.constat.client.core.presentation.util.stateInVM
import smartcaravans.constat.client.core.util.AccountManager
import smartcaravans.constat.client.core.util.Event
import smartcaravans.constat.client.core.util.ValidateInput

class AuthViewModel(
    val accountManager: AccountManager,
    val loginUseCase: LoginUseCase,
    val signupUseCase: SignupUseCase,
    val fileUploadUseCase: FileUploadUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.stateInVM(AuthUiState(), viewModelScope)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (accountManager.getUser() != null)
                sendEvent(Event.LaunchMainActivity)
        }
    }

    fun onAction(action: AuthUiAction) {
        when(action) {
            is AuthUiAction.SetEmail -> {
                _uiState.update {
                    it.copy(email = action.email).clearErrors()
                }
            }
            is AuthUiAction.SetPassword -> {
                _uiState.update {
                    it.copy(password = action.password).clearErrors()
                }
            }
            AuthUiAction.Login -> TODO()
            is AuthUiAction.SetConfirmPassword -> TODO()
            AuthUiAction.ToggleLogin -> {
                _uiState.update {
                    it.copy(login = !it.login).clearErrors()
                }
            }
            is AuthUiAction.SetPasswordVisibility -> {
                _uiState.update {
                    it.copy(passwordHidden = action.hidden).clearErrors()
                }
            }
            AuthUiAction.ToggleTermsAccepted -> TODO()
            AuthUiAction.Authenticate -> {
                if (_uiState.value.login) login()
                else signup()
            }
            is AuthUiAction.SetRemember -> {
                _uiState.update {
                    it.copy(remember = action.remember).clearErrors()
                }
            }

            is AuthUiAction.SetFirstName -> _uiState.update {
                it.copy(firstName = action.value).clearErrors()
            }
            is AuthUiAction.SetInsuranceNumber -> _uiState.update {
                it.copy(insuranceNumber = action.value).clearErrors()
            }
            is AuthUiAction.SetLastName -> _uiState.update {
                it.copy(lastName = action.value).clearErrors()
            }
            is AuthUiAction.SetPhoneNumber -> _uiState.update {
                it.copy(phoneNumber = action.value).clearErrors()
            }

            is AuthUiAction.SetJob -> _uiState.update {
                it.copy(job = action.value).clearErrors()
            }

            is AuthUiAction.IdentityScanned -> {
                Log.i("AuthViewModel", "Scanned image received: ${action.file?.path}")
                sendEvent(Event.AuthNavigate(NavRoutes.IdentityVerification))
                _uiState.update {
                    it.copy(scannedImage = action.file)
                }
            }
            AuthUiAction.Upload -> {
                sendEvent(Event.LaunchMainActivity)
                _uiState.value.scannedImage?.let { file ->
                    viewModelScope.launch(Dispatchers.IO) {
                        fileUploadUseCase(file)
                            .onError {
                                sendEvent(Event.AuthShowMessage(it.messageRes))
                            }
                    }
                }
            }
        }
    }

    private fun login() {
        ValidateInput.validateLogin(_uiState.value.getLoginRequest())?.let { error ->
            _uiState.update { state ->
                state.withError(error)
            }
            return
        }
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            loginUseCase(_uiState.value.getLoginRequest()).onSuccess {
                sendEvent(Event.AuthNavigate(NavRoutes.IdentityTutorial))
            }.onError {
                sendEvent(Event.AuthShowMessage(it.messageRes))
            }
            _uiState.update { it.copy(loading = false) }
        }
    }

    private fun signup() {
        ValidateInput.validateSignup(_uiState.value.getSignupRequest())?.let { error ->
            _uiState.update { state ->
                state.withError(error)
            }
            return
        }
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            signupUseCase(_uiState.value.getSignupRequest()).onSuccess {
                sendEvent(Event.AuthNavigate(NavRoutes.IdentityTutorial))
            }.onError {
                sendEvent(Event.AuthShowMessage(it.messageRes))
            }
            _uiState.update { it.copy(loading = false) }
        }
    }
}