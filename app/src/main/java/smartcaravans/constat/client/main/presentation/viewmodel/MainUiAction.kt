package smartcaravans.constat.client.main.presentation.viewmodel

sealed interface MainUiAction {
    data class SetConstatSheetState(val owner: Boolean, val state: ConstatSheetState?): MainUiAction
    data class Scanned(val text: String, val nfc: Boolean = true): MainUiAction
    data object SkipScan: MainUiAction
    data object GetConstats: MainUiAction
    data object GetInsurrances: MainUiAction
    data object ShowAddVehicleDialog: MainUiAction
    data object HideAddVehicleDialog: MainUiAction

    data class SetAddVehicleDialogState(val state: CreateVehicleDialogState): MainUiAction
    data class SetConstatFormState(val state: ConstatFormState): MainUiAction
    data object ConstatDialogNext: MainUiAction
    data object ConstatDialogBack: MainUiAction
}