package smartcaravans.constat.client.main.presentation.viewmodel

import smartcaravans.constat.client.GetConstatsQuery
import smartcaravans.constat.client.UserInsurrancesQuery
import smartcaravans.constat.client.core.util.Resource

data class MainUiState(
    val qrCodeText: String = "",
    val constatSheetState: ConstatSheetState? = null,
    val owner: Boolean = true,
    val constatsResource: Resource<List<GetConstatsQuery.Data1>> = Resource.Idle,
    val insurranceResource: Resource<List<UserInsurrancesQuery.UserInsurrance>> = Resource.Idle,
    val dialogVisible: Boolean = false,
    val createVehicleDialogState: CreateVehicleDialogState = CreateVehicleDialogState()
)
