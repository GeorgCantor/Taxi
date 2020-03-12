package taxi.kassa.view.withdraws.withdraw_create

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import taxi.kassa.model.Notification
import taxi.kassa.model.responses.AccountsList
import taxi.kassa.model.responses.ResponseOwner
import taxi.kassa.repository.ApiRepository

class WithdrawCreateViewModel(private val repository: ApiRepository) : ViewModel() {

    private val disposable = CompositeDisposable()
    private val accountId = MutableLiveData<Int>()

    val accounts = MutableLiveData<AccountsList>()
    val creatingStatus = MutableLiveData<String>()
    val deletionStatus = MutableLiveData<String>()
    val responseOwner = MutableLiveData<ResponseOwner>()
    val error = MutableLiveData<String>()
    val notifications = MutableLiveData<MutableList<Notification>>()

    fun getUserInfo() {
        disposable.add(
            Observable.fromCallable {
                    repository.getOwner()
                        ?.subscribe({
                            responseOwner.postValue(it?.response)
                            error.postValue(it?.errorMsg)
                        }, {
                        })
                }
                .subscribeOn(Schedulers.io())
                .subscribe()
        )

        notifications.value = repository.getNotifications()
    }

    fun getAccounts() {
        disposable.add(
            Observable.fromCallable {
                    repository.getAccounts()
                        ?.subscribe({
                            accountId.postValue(it?.response?.info?.first()?.id)
                            accounts.postValue(it?.response)
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

    fun createWithdraw(sourceId: Int, amount: String?) {
        disposable.add(
            Observable.fromCallable {
                    accountId.value?.let { id ->
                        repository.createWithdraw(sourceId, amount, id)
                            ?.subscribe({
                                creatingStatus.postValue(it?.response?.status)
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