package smartcaravans.constat.client.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import smartcaravans.constat.client.settings.data.SettingsDataStore

val settingsModule = module {
    single<SettingsDataStore> {
        SettingsDataStore(context = androidContext())
    }
}