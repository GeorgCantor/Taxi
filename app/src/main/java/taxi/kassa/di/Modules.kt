package taxi.kassa.di

import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import taxi.kassa.model.remote.ApiClient
import taxi.kassa.repository.ApiRepository
import taxi.kassa.util.PreferenceManager
import taxi.kassa.view.accounts.AccountsViewModel
import taxi.kassa.view.auth.auth_code.AuthCodeViewModel
import taxi.kassa.view.auth.auth_phone.AuthPhoneViewModel
import taxi.kassa.view.auth.auth_sign_up.AuthSignUpViewModel
import taxi.kassa.view.balance.BalanceViewModel
import taxi.kassa.view.fuel.FuelReplenishViewModel
import taxi.kassa.view.notifications.NotificationsViewModel
import taxi.kassa.view.orders.OrdersViewModel
import taxi.kassa.view.orders.list.OrdersListViewModel
import taxi.kassa.view.profile.ProfileViewModel
import taxi.kassa.view.registration.connection.ConnectionViewModel
import taxi.kassa.view.support.SupportViewModel
import taxi.kassa.view.support.chat.ChatHistoryViewModel
import taxi.kassa.view.withdraws.WithdrawsViewModel
import taxi.kassa.view.withdraws.withdraw.WithdrawViewModel
import taxi.kassa.view.withdraws.withdraw_create.WithdrawCreateViewModel

val repositoryModule = module {
    single { ApiRepository(get(), PreferenceManager(androidApplication().applicationContext)) }
}

val viewModelModule = module(override = true) {
    viewModel {
        AuthPhoneViewModel(androidApplication(), get())
    }
    viewModel {
        AuthSignUpViewModel(androidApplication(), get())
    }
    viewModel {
        AuthCodeViewModel(androidApplication(), get())
    }
    viewModel {
        ProfileViewModel(androidApplication(), get())
    }
    viewModel {
        BalanceViewModel(androidApplication(), get())
    }
    viewModel {
        WithdrawsViewModel(androidApplication(), get())
    }
    viewModel {
        WithdrawViewModel(get())
    }
    viewModel {
        AccountsViewModel(androidApplication(), get())
    }
    viewModel {
        WithdrawCreateViewModel(androidApplication(), get())
    }
    viewModel {
        OrdersViewModel(androidApplication(), get())
    }
    viewModel {
        FuelReplenishViewModel(androidApplication(), get())
    }
    viewModel {
        OrdersListViewModel(androidApplication(), get())
    }
    viewModel {
        ChatHistoryViewModel(get())
    }
    viewModel {
        NotificationsViewModel(get())
    }
    viewModel {
        SupportViewModel(get())
    }
    viewModel {
        SupportViewModel(get())
    }
    viewModel {
        ConnectionViewModel()
    }
}

val appModule = module {
    single { ApiClient.create(get()) }
}