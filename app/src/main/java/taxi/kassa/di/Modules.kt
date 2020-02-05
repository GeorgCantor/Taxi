package taxi.kassa.di

import org.koin.dsl.module
import taxi.kassa.model.remote.ApiClient

val repositoryModule = module {
//    single { Repository(get()) }
}

val viewModelModule = module {
//    viewModel { (query: String) ->
//    }
}

val appModule = module {
    single { ApiClient.create(get()) }
}