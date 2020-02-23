package taxi.kassa.view.withdraw

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import taxi.kassa.model.responses.AccountsList
import taxi.kassa.repository.ApiRepository

class WithdrawViewModel(private val repository: ApiRepository) : ViewModel() {

    private lateinit var disposable: Disposable

    val accounts = MutableLiveData<AccountsList>()
    val error = MutableLiveData<String>()

    fun getAccounts() {
        disposable = Observable.fromCallable {
            repository.getAccounts()
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
        if (::disposable.isInitialized) disposable.dispose()
    }
}