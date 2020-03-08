package taxi.kassa.di

import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import taxi.kassa.model.remote.ApiClient
import taxi.kassa.repository.ApiRepository
import taxi.kassa.view.accounts.AccountsViewModel
import taxi.kassa.view.auth.auth_code.AuthCodeViewModel
import taxi.kassa.view.auth.auth_phone.AuthPhoneViewModel
import taxi.kassa.view.auth.auth_sign_up.AuthSignUpViewModel
import taxi.kassa.view.balance.BalanceViewModel
import taxi.kassa.view.fuel.FuelReplenishViewModel
import taxi.kassa.view.orders.OrdersViewModel
import taxi.kassa.view.orders.list.OrdersListViewModel
import taxi.kassa.view.profile.ProfileViewModel
import taxi.kassa.view.support.chat.ChatHistoryViewModel
import taxi.kassa.view.withdraws.WithdrawsViewModel
import taxi.kassa.view.withdraws.withdraw.WithdrawViewModel
import taxi.kassa.view.withdraws.withdraw_create.WithdrawCreateViewModel

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
    viewModel {
        ProfileViewModel(get())
    }
    viewModel {
        BalanceViewModel(get())
    }
    viewModel {
        WithdrawsViewModel(get())
    }
    viewModel {
        WithdrawViewModel(get())
    }
    viewModel {
        AccountsViewModel(get())
    }
    viewModel {
        WithdrawCreateViewModel(get())
    }
    viewModel {
        OrdersViewModel(androidApplication())
    }
    viewModel {
        FuelReplenishViewModel(get())
    }
    viewModel {
        OrdersListViewModel(get())
    }
    viewModel {
        ChatHistoryViewModel()
    }
}

val appModule = module {
    single { ApiClient.create(get()) }
}