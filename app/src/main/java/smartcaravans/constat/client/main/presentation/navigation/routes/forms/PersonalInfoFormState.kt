package smartcaravans.constat.client.main.presentation.navigation.routes.forms

import smartcaravans.constat.client.core.presentation.util.InputError
import java.time.LocalDateTime

data class PersonalInfoFormState(
    val firstName: String = "",
    val firstNameError: InputError? = null,
    val lastName: String = "",
    val lastNameError: InputError? = null,
    val address: String = "",
    val addressError: InputError? = null,
    val licenseNumber: String = "",
    val licenseNumberError: InputError? = null,
    val licenseGivenBy: String = "",
    val licenseGivenByError: InputError? = null,
    val licenseGivenAt: LocalDateTime = LocalDateTime.now(),
    val licenseGivenAtError: InputError? = null,
)
