package smartcaravans.constat.client.settings.presentation.routes.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import smartcaravans.constat.client.core.presentation.util.stateInVM
import smartcaravans.constat.client.settings.data.SettingsDataStore
import smartcaravans.constat.client.settings.domain.models.ColorScheme
import smartcaravans.constat.client.settings.domain.models.Theme

class ThemeViewModel(val dataStore: SettingsDataStore): ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.stateInVM(UiState(), viewModelScope)

    val theme = dataStore.theme.onEach { theme ->
        _uiState.update {
            it.copy(selectedTheme = theme)
        }
    }.stateInVM(Theme.SYSTEM, viewModelScope)
    val dynamicColors = dataStore.dynamicColors.stateInVM(false, viewModelScope)
    val colorTheme = dataStore.colorTheme.stateInVM(ColorScheme.BLUE, viewModelScope)
    val extraDark = dataStore.extraDark.stateInVM(false, viewModelScope)

    fun update(callback: UiState.() -> UiState) {
        _uiState.value = _uiState.value.callback()
    }

    fun saveSettings(
        theme: Theme? = null,
        dynamic: Boolean? = null,
        color: ColorScheme? = null,
        extraDark: Boolean? = null
    ) {
        viewModelScope.launch {
            dataStore.saveSettings(theme?.toString(), color?.toString(), dynamic, extraDark)
        }
    }
}