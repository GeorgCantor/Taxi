package taxi.kassa.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import taxi.kassa.model.remote.ApiClient
import taxi.kassa.repository.ApiRepository
import taxi.kassa.view.auth_phone.AuthPhoneViewModel

val repositoryModule = module {
    single { ApiRepository(get()) }
}

val viewModelModule = module {
    viewModel {
        AuthPhoneViewModel(get())
    }
}

val appModule = module {
    single { ApiClient.create(get()) }
}