package smartcaravans.constat.client.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import smartcaravans.constat.client.settings.data.SettingsDataStore
import smartcaravans.constat.client.settings.presentation.routes.language.LanguageViewModel
import smartcaravans.constat.client.settings.presentation.routes.theme.ThemeViewModel

val settingsModule = module {
    single<SettingsDataStore> {
        SettingsDataStore(context = androidContext())
    }
    viewModel {
        ThemeViewModel(get())
    }
    viewModel {
        LanguageViewModel(get())
    }
}