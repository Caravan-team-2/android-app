package smartcaravans.constat.client.main.presentation.viewmodel

import smartcaravans.constat.client.R

enum class FormStep(val titleRes: Int) {
    VehicleInfo(titleRes = R.string.vehicle_info),
    InsuranceInfo(titleRes = R.string.insurance_info),
    Damages(titleRes = R.string.damages),
    Pictures(titleRes = R.string.pictures),
}