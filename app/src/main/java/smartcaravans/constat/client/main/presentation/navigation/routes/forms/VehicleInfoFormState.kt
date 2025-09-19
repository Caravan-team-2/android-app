package smartcaravans.constat.client.main.presentation.navigation.routes.forms

import smartcaravans.constat.client.core.presentation.util.InputError

data class VehicleInfoFormState(
    val selectedVehicle: Int = -1,
    val comingFrom: String = "",
    val comingFromError: InputError? = null,
    val goingTo: String = "",
    val goingToError: InputError? = null,
)
