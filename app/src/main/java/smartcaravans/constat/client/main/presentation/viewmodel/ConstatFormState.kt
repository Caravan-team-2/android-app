package smartcaravans.constat.client.main.presentation.viewmodel

import smartcaravans.constat.client.main.presentation.navigation.routes.forms.InsuranceFormState
import smartcaravans.constat.client.main.presentation.navigation.routes.forms.PersonalInfoFormState
import smartcaravans.constat.client.main.presentation.navigation.routes.forms.PictureFormState
import smartcaravans.constat.client.main.presentation.navigation.routes.forms.VehicleInfoFormState

data class ConstatFormState(
    val step: FormStep = FormStep.PersonalInfo,
    val personalInfoForm: PersonalInfoFormState = PersonalInfoFormState(),
    val vehicleInfo: VehicleInfoFormState = VehicleInfoFormState(),
    val insuranceFormState: InsuranceFormState = InsuranceFormState(),
    val pictureFormState: PictureFormState = PictureFormState(),
)
