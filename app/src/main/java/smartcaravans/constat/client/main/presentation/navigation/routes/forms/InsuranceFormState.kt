package smartcaravans.constat.client.main.presentation.navigation.routes.forms

import smartcaravans.constat.client.core.domain.models.InsuranceCompanies
import smartcaravans.constat.client.core.domain.models.InsuranceCompany
import smartcaravans.constat.client.core.presentation.util.InputError
import smartcaravans.constat.client.core.util.ValidateInput
import java.time.LocalDateTime

data class InsuranceFormState(
    val insuranceCompany: InsuranceCompanies = InsuranceCompanies.SAA,
    val insuranceCompanyError: InputError? = null,
    val insuranceNumber: String = "",
    val insuranceNumberError: InputError? = null,
    val validFrom: LocalDateTime = LocalDateTime.now(),
    val validFromError: InputError? = null,
    val validTo: LocalDateTime = LocalDateTime.now().plusYears(1),
    val validToError: InputError? = null,
    val draw: String? = null,
    val signature: String? = null,
) {

    fun withErrors() = ValidateInput.validateInsuranceForm(this)
    fun clearErrors() = copy(
        insuranceCompanyError = null,
        insuranceNumberError = null,
        validFromError = null,
        validToError = null
    )
    fun hasErrors() = listOf(
        insuranceCompanyError,
        insuranceNumberError,
        validFromError,
        validToError
    ).any { it != null }
}