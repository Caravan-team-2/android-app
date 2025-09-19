package smartcaravans.constat.client.di

import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module
import smartcaravans.constat.client.auth.data.repo.AuthRepoImpl
import smartcaravans.constat.client.auth.domain.repo.AuthRepo
import smartcaravans.constat.client.auth.domain.use_cases.FileUploadUseCase
import smartcaravans.constat.client.auth.domain.use_cases.LoginUseCase
import smartcaravans.constat.client.auth.domain.use_cases.SignupUseCase
import smartcaravans.constat.client.auth.presentation.viewmodel.AuthViewModel
import smartcaravans.constat.client.core.data.networking.ApiCall
import smartcaravans.constat.client.main.data.MainRepoImpl
import smartcaravans.constat.client.main.domain.repo.MainRepo
import smartcaravans.constat.client.main.domain.use_case.GetConstatsUseCase
import smartcaravans.constat.client.main.domain.use_case.GetInsurrancesUseCase

val authModule = module {
    viewModel {
        AuthViewModel(get(), get(), get(), get())
    }
}

val repoModule = module {
    single<AuthRepo> {
        AuthRepoImpl(get(), get(StringQualifier("my-api-call")))
    }

    single<MainRepo> {
        MainRepoImpl(get())
    }
}

val useCaseModule = module {
    factory {
        LoginUseCase(get())
    }
    factory {
        SignupUseCase(get())
    }
    factory {
        FileUploadUseCase(get())
    }
    factory {
        GetConstatsUseCase(get())
    }
    factory {
        GetInsurrancesUseCase(get())
    }
}