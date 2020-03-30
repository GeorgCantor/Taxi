package taxi.kassa.view.withdraws.withdraw

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import taxi.kassa.model.responses.AccountsList
import taxi.kassa.repository.ApiRepository

class WithdrawViewModel(private val repository: ApiRepository) : ViewModel() {

    private var disposable: Disposable

    val progressIsVisible = MutableLiveData<Boolean>().apply { this.value = true }
    val accounts = MutableLiveData<AccountsList?>()
    val error = MutableLiveData<String>()

    init {
        disposable = Observable.fromCallable {
            repository.getAccounts()
                ?.doFinally { progressIsVisible.postValue(false) }
                ?.subscribe({
                    accounts.postValue(it?.response)
                    error.postValue(it?.errorMsg)
                }, {
                })
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}