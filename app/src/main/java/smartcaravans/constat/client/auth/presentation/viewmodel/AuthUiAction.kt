package smartcaravans.constat.client.auth.presentation.viewmodel

import java.io.File

sealed interface AuthUiAction {
    data class SetFirstName(val value: String) : AuthUiAction
    data class SetLastName(val value: String) : AuthUiAction
    data class SetInsuranceNumber(val value: String) : AuthUiAction
    data class SetPhoneNumber(val value: String) : AuthUiAction
    data class SetJob(val value: String) : AuthUiAction
    data class SetEmail(val email: String) : AuthUiAction
    data class SetPassword(val password: String) : AuthUiAction
    data class SetConfirmPassword(val confirmPassword: String) : AuthUiAction
    data class SetRemember(val remember: Boolean) : AuthUiAction
    data object ToggleTermsAccepted : AuthUiAction
    data class SetPasswordVisibility(val hidden: Boolean) : AuthUiAction
    data class IdentityScanned(val file: File) : AuthUiAction
    data object Upload : AuthUiAction
    data object Login : AuthUiAction
    data object ToggleLogin : AuthUiAction
    data object Authenticate : AuthUiAction
}