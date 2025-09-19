package smartcaravans.constat.client.main.presentation.viewmodel

import smartcaravans.constat.client.R

enum class FormStep(val titleRes: Int) {
    PersonalInfo(titleRes = R.string.personal_info),
    VehicleInfo(titleRes = R.string.vehicle_info),
    InsuranceInfo(titleRes = R.string.insurance_info),
    Pictures(titleRes = R.string.pictures),
}