package smartcaravans.constat.client.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import smartcaravans.constat.client.main.presentation.viewmodel.MainViewModel

val mainModule = module {
    viewModelOf(::MainViewModel)
}