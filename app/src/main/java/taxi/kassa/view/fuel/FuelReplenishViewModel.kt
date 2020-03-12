package taxi.kassa.view.fuel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import taxi.kassa.model.Notification
import taxi.kassa.model.responses.ResponseOwner
import taxi.kassa.repository.ApiRepository

class FuelReplenishViewModel(private val repository: ApiRepository) : ViewModel() {

    private val disposable = CompositeDisposable()

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

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}