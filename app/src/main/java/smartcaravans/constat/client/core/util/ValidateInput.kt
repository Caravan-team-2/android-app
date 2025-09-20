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

    // Constat Form Validation Functions
    fun validateVehicleMake(make: String): InputError? =
        when {
            make.isBlank() -> {
                InputError.Empty(Field.MAKE, R.string.error_empty_make)
            }
            !make.matches(Regex("^[a-zA-Z\\s\\-]+$")) -> {
                InputError.Invalid(Field.MAKE, R.string.error_invalid_make)
            }
            else -> null
        }

    fun validateVehicleModel(model: String): InputError? =
        when {
            model.isBlank() -> {
                InputError.Empty(Field.MODEL, R.string.error_empty_model)
            }
            !model.matches(Regex("^[a-zA-Z0-9\\s\\-]+$")) -> {
                InputError.Invalid(Field.MODEL, R.string.error_invalid_model)
            }
            else -> null
        }

    fun validateVehicleType(type: String): InputError? =
        when {
            type.isBlank() -> {
                InputError.Empty(Field.TYPE, R.string.error_empty_type)
            }
            else -> null
        }

    fun validateRegistrationNumber(registrationNumber: String): InputError? =
        when {
            registrationNumber.isBlank() -> {
                InputError.Empty(Field.REGISTRATION_NUMBER, R.string.error_empty_registration_number)
            }
            !registrationNumber.matches(Regex("^[A-Z0-9\\-\\s]+$")) -> {
                InputError.Invalid(Field.REGISTRATION_NUMBER, R.string.error_invalid_registration_number)
            }
            else -> null
        }

    fun validateLocation(location: String): InputError? =
        when {
            location.isBlank() -> {
                InputError.Empty(Field.LOCATION, R.string.error_empty_location)
            }
            location.length < 5 -> {
                InputError.Short(Field.LOCATION, R.string.error_short_location)
            }
            else -> null
        }

    fun validateDateTime(dateTime: String): InputError? =
        when {
            dateTime.isBlank() -> {
                InputError.Empty(Field.DATE_TIME, R.string.error_empty_date_time)
            }
            else -> null
        }

    fun validateDescription(description: String): InputError? =
        when {
            description.isBlank() -> {
                InputError.Empty(Field.DESCRIPTION, R.string.error_empty_description)
            }
            description.length < 10 -> {
                InputError.Short(Field.DESCRIPTION, R.string.error_short_description)
            }
            else -> null
        }

    fun validateDriverName(driverName: String): InputError? =
        when {
            driverName.isBlank() -> {
                InputError.Empty(Field.DRIVER_NAME, R.string.error_empty_driver_name)
            }
            !driverName.matches(Regex("^[a-zA-Z\\s\\-']+$")) -> {
                InputError.Invalid(Field.DRIVER_NAME, R.string.error_invalid_driver_name)
            }
            else -> null
        }

    fun validateLicenseNumber(licenseNumber: String): InputError? =
        when {
            licenseNumber.isBlank() -> {
                InputError.Empty(Field.LICENSE_NUMBER, R.string.error_empty_license_number)
            }
            !licenseNumber.matches(Regex("^[A-Z0-9\\-]+$")) -> {
                InputError.Invalid(Field.LICENSE_NUMBER, R.string.error_invalid_license_number)
            }
            else -> null
        }

    fun validateInjuredCount(injuredCount: String): InputError? =
        when {
            injuredCount.isBlank() -> {
                InputError.Empty(Field.INJURED_COUNT, R.string.error_empty_injured_count)
            }
            !injuredCount.matches(Regex("^[0-9]+$")) -> {
                InputError.Invalid(Field.INJURED_COUNT, R.string.error_invalid_injured_count)
            }
            injuredCount.toIntOrNull()?.let { it < 0 } == true -> {
                InputError.Invalid(Field.INJURED_COUNT, R.string.error_negative_injured_count)
            }
            else -> null
        }

    fun validateWitnessName(witnessName: String): InputError? =
        when {
            witnessName.isBlank() -> null // Witness is optional
            !witnessName.matches(Regex("^[a-zA-Z\\s\\-']+$")) -> {
                InputError.Invalid(Field.WITNESS_NAME, R.string.error_invalid_witness_name)
            }
            else -> null
        }

    fun validateWitnessPhone(witnessPhone: String): InputError? =
        when {
            witnessPhone.isBlank() -> null // Witness phone is optional
            !Patterns.PHONE.matcher(witnessPhone).matches() -> {
                InputError.Invalid(Field.WITNESS_PHONE, R.string.error_invalid_witness_phone)
            }
            else -> null
        }

    fun validateConstatBasicInfo(
        location: String,
        dateTime: String,
        description: String
    ): InputError? =
        validateLocation(location) ?:
        validateDateTime(dateTime) ?:
        validateDescription(description)

    fun validateConstatVehicleInfo(
        make: String,
        model: String,
        type: String,
        registrationNumber: String
    ): InputError? =
        validateVehicleMake(make) ?:
        validateVehicleModel(model) ?:
        validateVehicleType(type) ?:
        validateRegistrationNumber(registrationNumber)

    fun validateConstatDriverInfo(
        driverName: String,
        licenseNumber: String,
        phoneNumber: String
    ): InputError? =
        validateDriverName(driverName) ?:
        validateLicenseNumber(licenseNumber) ?:
        validatePhoneNumber(phoneNumber)

    fun validateConstatWitnessInfo(
        witnessName: String,
        witnessPhone: String
    ): InputError? =
        validateWitnessName(witnessName) ?:
        validateWitnessPhone(witnessPhone)

    fun validateFullConstat(
        // Basic info
        location: String,
        dateTime: String,
        description: String,
        injuredCount: String,
        // Vehicle info
        make: String,
        model: String,
        type: String,
        registrationNumber: String,
        // Driver info
        driverName: String,
        licenseNumber: String,
        phoneNumber: String,
        // Optional witness info
        witnessName: String = "",
        witnessPhone: String = ""
    ): InputError? =
        validateConstatBasicInfo(location, dateTime, description) ?:
        validateInjuredCount(injuredCount) ?:
        validateConstatVehicleInfo(make, model, type, registrationNumber) ?:
        validateConstatDriverInfo(driverName, licenseNumber, phoneNumber) ?:
        validateConstatWitnessInfo(witnessName, witnessPhone)

    // Form State Validation Functions
    fun validateAddress(address: String): InputError? =
        when {
            address.isBlank() -> {
                InputError.Empty(Field.ADDRESS, R.string.error_empty_address)
            }
            address.length < 10 -> {
                InputError.Short(Field.ADDRESS, R.string.error_short_address)
            }
            else -> null
        }

    fun validateLicenseGivenBy(givenBy: String): InputError? =
        when {
            givenBy.isBlank() -> {
                InputError.Empty(Field.LICENSE_GIVEN_BY, R.string.error_empty_license_given_by)
            }
            !givenBy.matches(Regex("^[a-zA-Z\\s]+$")) -> {
                InputError.Invalid(Field.LICENSE_GIVEN_BY, R.string.error_invalid_license_given_by)
            }
            else -> null
        }

    fun validateLicenseGivenAt(givenAt: java.time.LocalDateTime): InputError? =
        when {
            givenAt.isAfter(java.time.LocalDateTime.now()) -> {
                InputError.InvalidDate(Field.LICENSE_GIVEN_AT, R.string.error_invalid_license_given_at)
            }
            else -> null
        }

    fun validateInsuranceCompany(company: String): InputError? =
        when {
            company.isBlank() -> {
                InputError.Empty(Field.INSURANCE_COMPANY, R.string.error_empty_insurance_company)
            }
            !company.matches(Regex("^[a-zA-Z\\s\\-]+$")) -> {
                InputError.Invalid(Field.INSURANCE_COMPANY, R.string.error_invalid_insurance_company)
            }
            else -> null
        }

    fun validateValidFrom(validFrom: java.time.LocalDateTime): InputError? =
        when {
            validFrom.isAfter(java.time.LocalDateTime.now()) -> {
                InputError.InvalidDate(Field.VALID_FROM, R.string.error_invalid_valid_from)
            }
            else -> null
        }

    fun validateValidTo(validFrom: java.time.LocalDateTime, validTo: java.time.LocalDateTime): InputError? =
        when {
            validTo.isBefore(validFrom) || validTo.isEqual(validFrom) -> {
                InputError.InvalidDate(Field.VALID_TO, R.string.error_invalid_valid_to)
            }
            else -> null
        }

    fun validateComingFrom(comingFrom: String): InputError? =
        when {
            comingFrom.isBlank() -> {
                InputError.Empty(Field.COMING_FROM, R.string.error_empty_coming_from)
            }
            comingFrom.length < 3 -> {
                InputError.Short(Field.COMING_FROM, R.string.error_short_coming_from)
            }
            else -> null
        }

    fun validateGoingTo(goingTo: String): InputError? =
        when {
            goingTo.isBlank() -> {
                InputError.Empty(Field.GOING_TO, R.string.error_empty_going_to)
            }
            goingTo.length < 3 -> {
                InputError.Short(Field.GOING_TO, R.string.error_short_going_to)
            }
            else -> null
        }

    fun validateInsuranceForm(form: smartcaravans.constat.client.main.presentation.navigation.routes.forms.InsuranceFormState): smartcaravans.constat.client.main.presentation.navigation.routes.forms.InsuranceFormState =
        form.copy(
            insuranceNumberError = validateInsuranceNumber(form.insuranceNumber),
            validFromError = validateValidFrom(form.validFrom),
            validToError = validateValidTo(form.validFrom, form.validTo)
        )

    fun validateVehicleInfoForm(form: smartcaravans.constat.client.main.presentation.navigation.routes.forms.VehicleInfoFormState): smartcaravans.constat.client.main.presentation.navigation.routes.forms.VehicleInfoFormState =
        form.copy(
            vehicleError = null //InputError.Empty(Field.VEHICLE, R.string.error_no_vehicle_selected).takeIf { form.selectedVehicle == -1 }
        )
}
