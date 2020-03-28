package taxi.kassa.view.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import taxi.kassa.MyApplication
import taxi.kassa.model.Message
import taxi.kassa.model.Notification
import taxi.kassa.model.responses.ResponseOwner
import taxi.kassa.repository.ApiRepository
import taxi.kassa.util.isNetworkAvailable

class ProfileViewModel(
    app: Application,
    private val repository: ApiRepository
) : AndroidViewModel(app) {

    private val context = getApplication<MyApplication>()

    private var disposable: Disposable

    val progressIsVisible = MutableLiveData<Boolean>().apply { this.value = true }
    val isNetworkAvailable = MutableLiveData<Boolean>()
    val responseOwner = MutableLiveData<ResponseOwner>()
    val error = MutableLiveData<String>()
    val notifications = MutableLiveData<MutableList<Notification>>()
    val incomingMessages = MutableLiveData<MutableList<Message>>()

    init {
        disposable = Observable.fromCallable {
                repository.getOwner()
                    ?.doFinally { progressIsVisible.postValue(false) }
                    ?.subscribe({
                        responseOwner.postValue(it?.response)
                        error.postValue(it?.errorMsg)
                    }, {
                    })
            }
            .subscribeOn(Schedulers.io())
            .subscribe()

        notifications.value = repository.getNotifications()
        incomingMessages.value = repository.getChatHistory().filter { it.isIncoming } as MutableList

        isNetworkAvailable.value = context.isNetworkAvailable()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}