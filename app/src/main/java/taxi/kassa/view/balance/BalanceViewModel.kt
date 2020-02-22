package taxi.kassa.view.balance

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import taxi.kassa.model.responses.ResponseOwner
import taxi.kassa.repository.ApiRepository

class BalanceViewModel(private val repository: ApiRepository) : ViewModel() {

    private lateinit var disposable: Disposable

    val responseOwner = MutableLiveData<ResponseOwner>()
    val error = MutableLiveData<String>()

    fun getUserInfo() {
        disposable = Observable.fromCallable {
            repository.getOwner()
                ?.subscribe({
                    responseOwner.postValue(it?.response)
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