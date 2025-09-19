package smartcaravans.constat.client.main.presentation.navigation.routes.forms

import smartcaravans.constat.client.core.domain.models.InsuranceCompany
import smartcaravans.constat.client.core.presentation.util.InputError
import java.time.LocalDateTime

data class InsuranceFormState(
    val insuranceCompany: String = "",
    val insuranceCompanyError: InputError? = null,
    val insuranceNumber: String = "",
    val insuranceNumberError: InputError? = null,
    val validFrom: LocalDateTime = LocalDateTime.now(),
    val validFromError: InputError? = null,
    val validTo: LocalDateTime = LocalDateTime.now().plusYears(1),
    val validToError: InputError? = null,
    val draw: String? = null,
    val signature: String? = null,
)
