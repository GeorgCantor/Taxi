package taxi.kassa.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import taxi.kassa.model.remote.ApiClient
import taxi.kassa.repository.ApiRepository
import taxi.kassa.view.auth_code.AuthCodeViewModel
import taxi.kassa.view.auth_phone.AuthPhoneViewModel
import taxi.kassa.view.auth_sign_up.AuthSignUpViewModel

val repositoryModule = module {
    single { ApiRepository(get()) }
}

val viewModelModule = module {
    viewModel {
        AuthPhoneViewModel(get())
    }
    viewModel {
        AuthSignUpViewModel(get())
    }
    viewModel {
        AuthCodeViewModel(get())
    }
}

val appModule = module {
    single { ApiClient.create(get()) }
}