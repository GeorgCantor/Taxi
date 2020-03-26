package taxi.kassa.view.accounts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import taxi.kassa.model.Card
import taxi.kassa.model.Notification
import taxi.kassa.model.responses.AccountsList
import taxi.kassa.repository.ApiRepository

class AccountsViewModel(private val repository: ApiRepository) : ViewModel() {

    private val disposable = CompositeDisposable()

    val creatingStatus = MutableLiveData<String>()
    val deletionStatus = MutableLiveData<String>()
    val accounts = MutableLiveData<AccountsList>()
    val error = MutableLiveData<String>()
    val notifications = MutableLiveData<MutableList<Notification>>()
    val cards = MutableLiveData<MutableList<Card>>()

    fun getAccounts() {
        disposable.add(
            Observable.fromCallable {
                repository.getAccounts()
                    ?.subscribe({
                        accounts.postValue(it?.response)
                        error.postValue(it?.errorMsg)
                    }, {
                    })
            }
                .subscribeOn(Schedulers.io())
                .subscribe()
        )

        notifications.value = repository.getNotifications()
        cards.value = repository.getCards()
    }

    fun createAccount(
        firstName: String,
        lastName: String,
        middleName: String,
        accountNumber: String,
        bankCode: String
    ) {
        disposable.add(
            Observable.fromCallable {
                repository.createAccount(firstName, lastName, middleName, accountNumber, bankCode)
                    ?.subscribe({
                        creatingStatus.postValue(it?.response?.status)
                        error.postValue(it?.errorMsg)
                    }, {
                    })
            }
                .subscribeOn(Schedulers.io())
                .subscribe()
        )
    }

    fun deleteAccount() {
        disposable.add(
            Observable.fromCallable {
                accounts.value?.info?.first()?.id?.let {
                    repository.deleteAccount(it)
                        ?.subscribe({
                            deletionStatus.postValue(it?.response?.status)
                            error.postValue(it?.errorMsg)
                        }, {
                        })
                }
            }
                .subscribeOn(Schedulers.io())
                .subscribe()
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}