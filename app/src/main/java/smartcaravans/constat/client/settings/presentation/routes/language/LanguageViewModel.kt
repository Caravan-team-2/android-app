package smartcaravans.constat.client.settings.presentation.routes.language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import smartcaravans.constat.client.core.presentation.util.stateInVM
import smartcaravans.constat.client.settings.data.SettingsDataStore
import smartcaravans.constat.client.settings.domain.models.Language

class LanguageViewModel(
    val dataStore: SettingsDataStore
): ViewModel() {
    val language = dataStore.language.stateInVM(Language.SYSTEM, viewModelScope)

    fun saveLanguage(language: Language) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.saveSettings(language = language.toString())
        }
    }
}