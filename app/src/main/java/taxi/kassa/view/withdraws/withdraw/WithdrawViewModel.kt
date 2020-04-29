package taxi.kassa.view.withdraws.withdraw

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import taxi.kassa.model.responses.AccountsList
import taxi.kassa.repository.ApiRepository

class WithdrawViewModel(private val repository: ApiRepository) : ViewModel() {

    private val disposable = CompositeDisposable()

    val isProgressVisible = MutableLiveData<Boolean>().apply { this.value = true }
    val accounts = MutableLiveData<AccountsList?>()
    val error = MutableLiveData<String>()

    init {
        disposable.add(
            Observable.fromCallable {
                repository.getAccounts()
                    ?.doFinally { isProgressVisible.postValue(false) }
                    ?.subscribe({
                        accounts.postValue(it?.response)
                        error.postValue(it?.errorMsg)
                    }, {
                    })
            }
                .subscribeOn(Schedulers.io())
                .subscribe()
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}