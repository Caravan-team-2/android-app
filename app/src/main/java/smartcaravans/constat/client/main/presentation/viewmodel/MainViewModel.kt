package smartcaravans.constat.client.main.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import smartcaravans.constat.client.core.domain.models.User
import smartcaravans.constat.client.core.domain.util.onError
import smartcaravans.constat.client.core.domain.util.onSuccess
import smartcaravans.constat.client.core.presentation.util.sendEvent
import smartcaravans.constat.client.core.presentation.util.stateInVM
import smartcaravans.constat.client.core.util.AccountManager
import smartcaravans.constat.client.core.util.Event.MainNavigate
import smartcaravans.constat.client.core.util.Resource
import smartcaravans.constat.client.core.util.Resource.*
import smartcaravans.constat.client.main.domain.use_case.GetConstatsUseCase
import smartcaravans.constat.client.main.domain.use_case.GetInsurrancesUseCase
import smartcaravans.constat.client.main.presentation.navigation.NavRoutes
import kotlin.math.max
import kotlin.math.min

class MainViewModel(
    accountManager: AccountManager,
    val getConstatsUseCase: GetConstatsUseCase,
    val getInsurrancesUseCase: GetInsurrancesUseCase
): ViewModel() {
    val user = accountManager.user.map {
        it ?: User()
    }.stateInVM(User(), viewModelScope)

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.stateInVM(MainUiState(), viewModelScope)

    private val _formState = MutableStateFlow(ConstatFormState())
    val formState = _formState.stateInVM(ConstatFormState(), viewModelScope)

    init {
        onAction(MainUiAction.GetConstats)
        onAction(MainUiAction.GetInsurrances)
        viewModelScope.launch(Dispatchers.IO) {
            user.collect { user ->
                _uiState.update { state ->
                    state.copy(qrCodeText = user.getQrCodeData())
                }
            }
        }
    }

    fun onAction(action: MainUiAction) {
        when(action) {
            is MainUiAction.SetConstatSheetState -> _uiState.update {
                it.copy(constatSheetState = action.state, owner = action.owner)
            }

            is MainUiAction.Scanned -> {
                if (action.nfc and _uiState.value.owner) return  // Ignore scans when owner is scanning NFC
                _uiState.update {
                    it.copy(constatSheetState = null)
                }
                println("Scanned: ${action.text}")
                if(action.text.isNotEmpty())
                    sendEvent(MainNavigate(NavRoutes.CreateConstat))
            }
            is MainUiAction.SkipScan -> {
                _uiState.update {
                    it.copy(constatSheetState = null)
                }
                sendEvent(MainNavigate(NavRoutes.CreateConstat))
            }
            MainUiAction.GetConstats -> {
                _uiState.update {
                    it.copy(constatsResource = Loading)
                }
                viewModelScope.launch(Dispatchers.IO) {
                    getConstatsUseCase().onSuccess { result ->
                        _uiState.update {
                            it.copy(constatsResource = Success(result))
                        }
                    }.onError { error ->
                        _uiState.update {
                            it.copy(constatsResource = Error(error))
                        }
                    }
                }
            }

            MainUiAction.GetInsurrances -> {
                _uiState.update {
                    it.copy(insurranceResource = Loading)
                }
                viewModelScope.launch(Dispatchers.IO) {
                    getInsurrancesUseCase().onSuccess { result ->
                        _uiState.update {
                            it.copy(insurranceResource = Success(result))
                        }
                    }.onError { error ->
                        _uiState.update {
                            it.copy(insurranceResource = Error(error))
                        }
                    }
                }
            }

            MainUiAction.HideAddVehicleDialog -> _uiState.update {
                it.copy(dialogVisible = false)
            }
            MainUiAction.ShowAddVehicleDialog -> _uiState.update {
                it.copy(dialogVisible = true)
            }

            is MainUiAction.SetAddVehicleDialogState -> _uiState.update {
                it.copy(createVehicleDialogState = action.state)
            }
            MainUiAction.ConstatDialogBack -> _formState.update {
                it.copy(step = FormStep.entries[max(it.step.ordinal - 1, 0)])
            }
            MainUiAction.ConstatDialogNext -> _formState.update {
                it.copy(step = FormStep.entries[min(it.step.ordinal + 1, FormStep.entries.size - 1)])
            }
            is MainUiAction.SetConstatFormState -> _formState.value = action.state
        }
    }
}