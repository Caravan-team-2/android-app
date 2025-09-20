package smartcaravans.constat.client.main.presentation.navigation.routes.forms

import smartcaravans.constat.client.core.presentation.Location
import smartcaravans.constat.client.core.presentation.util.InputError
import smartcaravans.constat.client.core.util.ValidateInput

data class VehicleInfoFormState(
    val selectedVehicle: Int = -1,
    val vehicleError: InputError? = null,
    val comingFrom: Location = Location(),
    val goingTo: Location = Location(),
) {
    fun withErrors() = ValidateInput.validateVehicleInfoForm(this)
    fun clearErrors(): VehicleInfoFormState = copy(
        vehicleError = null,
    )
    fun hasErrors(): Boolean = vehicleError != null
}