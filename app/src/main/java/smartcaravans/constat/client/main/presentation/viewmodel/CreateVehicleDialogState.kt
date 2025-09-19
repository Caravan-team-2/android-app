package smartcaravans.constat.client.main.presentation.viewmodel

import smartcaravans.constat.client.core.presentation.util.InputError

data class CreateVehicleDialogState(
    val id: String = "",
    val idError: InputError? = null,
    val insuranceId: String = "",
    val insuranceIdError: InputError? = null,
    val registrationNumber: String = "",
    val registrationNumberError: InputError? = null,
    val make: String = "",
    val makeError: InputError? = null,
    val model: String = "",
    val modelError: InputError? = null,
    val type: String = "",
    val typeError: InputError? = null,
) {
    fun withErrors() = copy(
        idError = idError,
        insuranceIdError = insuranceIdError,
        registrationNumberError = registrationNumberError,
        makeError = makeError,
        modelError = modelError,
        typeError = typeError,
    )
}
