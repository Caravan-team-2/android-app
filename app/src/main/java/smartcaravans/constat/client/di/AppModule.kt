package smartcaravans.constat.client.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import smartcaravans.constat.client.core.util.AccountManager
import smartcaravans.constat.client.core.util.apiDatastore

val appModule = module {
    includes(networkModule, settingsModule)
    single {
        androidContext().apiDatastore
    }
    single {
        AccountManager(get())
    }
//    includes(databaseModule, repoModule, viewModelModule, utilsModule, useCaseModule)
}
