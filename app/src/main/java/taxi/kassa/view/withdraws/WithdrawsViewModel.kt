package taxi.kassa.view.withdraws

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import taxi.kassa.model.responses.Withdraws
import taxi.kassa.repository.ApiRepository

class WithdrawsViewModel(private val repository: ApiRepository) : ViewModel() {

    private lateinit var disposable: Disposable

    val withdraws = MutableLiveData<Withdraws>()
    val error = MutableLiveData<String>()

    fun getWithdraws() {
        disposable = Observable.fromCallable {
            repository.getWithdraws()
                ?.subscribe({
                    withdraws.postValue(it?.response)
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