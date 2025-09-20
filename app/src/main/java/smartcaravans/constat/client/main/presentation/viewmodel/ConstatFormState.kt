package smartcaravans.constat.client.main.presentation.viewmodel

import smartcaravans.constat.client.main.presentation.navigation.routes.forms.DamagesFormState
import smartcaravans.constat.client.main.presentation.navigation.routes.forms.InsuranceFormState
import smartcaravans.constat.client.main.presentation.navigation.routes.forms.PictureFormState
import smartcaravans.constat.client.main.presentation.navigation.routes.forms.VehicleInfoFormState

data class ConstatFormState(
    val step: FormStep = FormStep.VehicleInfo,
    val vehicleInfo: VehicleInfoFormState = VehicleInfoFormState(),
    val insuranceFormState: InsuranceFormState = InsuranceFormState(),
    val damagesForm: DamagesFormState = DamagesFormState(),
    val pictureFormState: PictureFormState = PictureFormState(),
)
