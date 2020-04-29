package taxi.kassa.view.auth.auth_phone

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import taxi.kassa.MyApplication
import taxi.kassa.repository.ApiRepository
import taxi.kassa.util.isNetworkAvailable

class AuthPhoneViewModel(
    app: Application,
    private val repository: ApiRepository
) : AndroidViewModel(app) {

    private val disposable = CompositeDisposable()

    val isProgressVisible = MutableLiveData<Boolean>().apply { this.value = false }
    val isNetworkAvailable = MutableLiveData<Boolean>()
    val isLoggedIn = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    fun login(phone: String) {
        disposable.add(
            Observable.fromCallable {
                repository.login(phone)
                    ?.doOnSubscribe { isProgressVisible.postValue(true) }
                    ?.doFinally { isProgressVisible.postValue(false) }
                    ?.subscribe({
                        isLoggedIn.postValue(it?.success)
                        it?.errorMsg?.let { error.postValue(it) }
                    }, {
                    })
            }
                .subscribeOn(Schedulers.io())
                .subscribe()
        )

        isNetworkAvailable.value = getApplication<MyApplication>().isNetworkAvailable()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}