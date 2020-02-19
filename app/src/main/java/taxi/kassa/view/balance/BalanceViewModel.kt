package taxi.kassa.view.balance

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import taxi.kassa.model.responses.ResponseOwner
import taxi.kassa.repository.ApiRepository

class BalanceViewModel(private val repository: ApiRepository) : ViewModel() {

    val responseOwner = MutableLiveData<ResponseOwner>()
    val error = MutableLiveData<String>()

    fun getUserInfo() {
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
    }
}