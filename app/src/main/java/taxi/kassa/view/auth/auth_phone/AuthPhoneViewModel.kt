package taxi.kassa.view.auth.auth_phone

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import taxi.kassa.repository.ApiRepository

class AuthPhoneViewModel(private val repository: ApiRepository) : ViewModel() {

    private lateinit var disposable: Disposable

    val isProgressVisible = MutableLiveData<Boolean>().apply { this.value = false }
    val isLoggedIn = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    fun login(phone: String) {
        disposable = Observable.fromCallable {
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
    }

    override fun onCleared() {
        super.onCleared()
        if (::disposable.isInitialized) disposable.dispose()
    }
}