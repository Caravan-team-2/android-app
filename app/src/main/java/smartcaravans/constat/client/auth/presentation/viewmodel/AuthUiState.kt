package smartcaravans.constat.client.auth.presentation.viewmodel

import smartcaravans.constat.client.auth.domain.dto.LoginRequest
import smartcaravans.constat.client.auth.domain.dto.SignupRequest
import smartcaravans.constat.client.core.presentation.util.Field
import smartcaravans.constat.client.core.presentation.util.InputError
import java.io.File

data class AuthUiState(
    val login: Boolean = true,
    val firstName: String = "",
    val firstNameError: InputError? = null,
    val lastName: String = "",
    val lastNameError: InputError? = null,
    val insuranceNumber: String = "",
    val insuranceNumberError: InputError? = null,
    val phoneNumber: String = "",
    val phoneNumberError: InputError? = null,
    val job: String = "",
    val jobError: InputError? = null,
    val email: String = "",
    val emailError: InputError? = null,
    val password: String = "",
    val passwordError: InputError? = null,
    val passwordHidden: Boolean = true,
    val confirmPassword: String = "",
    val confirmPasswordError: InputError? = null,
    val remember: Boolean = false,
    val loading: Boolean = false,
    val scannedImage: File? = null,
) {
    fun getSignupRequest() = SignupRequest(
        firstName = firstName,
        lastName = lastName,
        insuranceNumber = insuranceNumber,
        phoneNumber = phoneNumber,
        job = job,
        email = email,
        password = password,
    )

    fun getLoginRequest() = LoginRequest(
        email = email,
        password = password,
    )

    fun withError(error: InputError) = copy(
        firstNameError = error.takeIf { it.field == Field.FIRST_NAME },
        lastNameError = error.takeIf { it.field == Field.LAST_NAME },
        insuranceNumberError = error.takeIf { it.field ==  Field.INSURANCE_NUMBER },
        phoneNumberError = error.takeIf { it.field ==  Field.PHONE_NUMBER },
        jobError = error.takeIf { it.field ==  Field.JOB_TITLE },
        emailError = error.takeIf { it.field ==  Field.EMAIL },
        passwordError = error.takeIf { it.field ==  Field.PASSWORD },
        confirmPasswordError = error.takeIf { it.field ==  Field.CONFIRM_PASSWORD },
    )

    fun clearErrors() = copy(
        firstNameError = null,
        lastNameError = null,
        insuranceNumberError = null,
        phoneNumberError = null,
        jobError = null,
        emailError = null,
        passwordError = null,
        confirmPasswordError = null,
    )
}