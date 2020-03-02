package taxi.kassa.view.auth_code

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import taxi.kassa.repository.ApiRepository

class AuthCodeViewModel(private val repository: ApiRepository) : ViewModel() {

    private lateinit var disposable: Disposable

    val isLoggedIn = MutableLiveData<Boolean>()
    val token = MutableLiveData<String>()
    val error = MutableLiveData<String>()

    fun login(phone: String, code: String) {
        disposable = Observable.fromCallable {
            repository.getCode(phone, code)
                ?.subscribe({
                    isLoggedIn.postValue(it?.success)
                    it?.response?.let { token.postValue(it.token) }
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