package smartcaravans.constat.client.core.util

import android.util.Patterns
import smartcaravans.constat.client.R
import smartcaravans.constat.client.auth.domain.dto.LoginRequest
import smartcaravans.constat.client.auth.domain.dto.SignupRequest
import smartcaravans.constat.client.core.presentation.util.Field
import smartcaravans.constat.client.core.presentation.util.InputError

object ValidateInput {
    fun validateEmail(email: String): InputError? =
        when {
            email.isBlank() -> {
                InputError.Empty(Field.EMAIL, R.string.error_empty_email)
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                InputError.Invalid(Field.EMAIL, R.string.error_invalid_email)
            }
            else -> null
        }

    fun validateLoginPassword(password: String): InputError? =
        when {
            password.isBlank() -> {
                InputError.Empty(Field.PASSWORD, R.string.error_empty_password)
            }
            password.length < 6 -> {
                InputError.Short(Field.PASSWORD, R.string.error_short_password)
            }
            else -> null
        }

    fun validatePassword(password: String): InputError? =
        when {
            password.isBlank() -> {
                InputError.Empty(Field.PASSWORD, R.string.error_empty_password)
            }
            password.length < 6 -> {
                InputError.Short(Field.PASSWORD, R.string.error_short_password)
            }
            !password.matches(Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{12,}$")) -> {
                InputError.Weak(Field.PASSWORD, R.string.error_weak_password)
            }
            else -> null
        }

    fun validateFirstName(firstName: String): InputError? =
        when {
            firstName.isBlank() -> {
                InputError.Empty(Field.FIRST_NAME, R.string.error_empty_first_name)
            }
            !firstName.matches(Regex("^[a-zA-Z\\s]+$")) -> {
                InputError.Invalid(Field.FIRST_NAME, R.string.error_invalid_first_name)
            }
            else -> null
        }

    fun validateLastName(lastName: String): InputError? =
        when {
            lastName.isBlank() -> {
                InputError.Empty(Field.LAST_NAME, R.string.error_empty_last_name)
            }
            !lastName.matches(Regex("^[a-zA-Z\\s]+$")) -> {
                InputError.Invalid(Field.LAST_NAME, R.string.error_invalid_last_name)
            }
            else -> null
        }

    fun validateJobTitle(jobTitle: String): InputError? =
        when {
            jobTitle.isBlank() -> {
                InputError.Empty(Field.JOB_TITLE, R.string.error_empty_job_title)
            }
            !jobTitle.matches(Regex("^[a-zA-Z\\s]+$")) -> {
                InputError.Invalid(Field.JOB_TITLE, R.string.error_invalid_job_title)
            }
            else -> null
        }

    fun validateInsuranceNumber(insuranceNumber: String): InputError? =
        when {
            insuranceNumber.isBlank() -> {
                InputError.Empty(Field.INSURANCE_NUMBER, R.string.error_empty_insurance_number)
            }
            else -> null
        }

    fun validatePhoneNumber(phoneNumber: String): InputError? =
        when {
            phoneNumber.isBlank() -> {
                InputError.Empty(Field.PHONE_NUMBER, R.string.error_empty_phone_number)
            }
            !Patterns.PHONE.matcher(phoneNumber).matches() -> {
                InputError.Invalid(Field.PHONE_NUMBER, R.string.error_invalid_phone_number)
            }
            else -> null
        }

    fun validateConfirmPassword(password: String, confirmPassword: String): InputError? =
        when {
            confirmPassword.isBlank() -> {
                InputError.Empty(Field.CONFIRM_PASSWORD, R.string.error_empty_confirm_password)
            }
            password != confirmPassword -> {
                InputError.Mismatch(Field.CONFIRM_PASSWORD, R.string.error_password_mismatch)
            }
            else -> null
        }

    fun validateLogin(request: LoginRequest): InputError? {
        val emailError = validateEmail(request.email)
        val passwordError = validateLoginPassword(request.password)

        return emailError ?: passwordError
    }

    fun validateSignup(
        request: SignupRequest
    ): InputError? =
        validateFirstName(request.firstName) ?:
        validateLastName(request.lastName) ?:
        validateJobTitle(request.job) ?:
        validatePhoneNumber(request.phoneNumber) ?:
        validateInsuranceNumber(request.insuranceNumber) ?:
        validateInsuranceNumber(request.email) ?:
        validateInsuranceNumber(request.password)


    fun validateResetPassword(password: String, confirmPassword: String): InputError? =
        validatePassword(password) ?: validateConfirmPassword(password, confirmPassword)

    fun validateChangePassword(
//        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String,
    ): InputError? =
        validatePassword(newPassword) ?: validateConfirmPassword(newPassword, confirmNewPassword)
}