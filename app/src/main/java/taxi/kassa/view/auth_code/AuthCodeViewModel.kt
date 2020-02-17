package taxi.kassa.view.auth_code

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import taxi.kassa.repository.ApiRepository

class AuthCodeViewModel(private val repository: ApiRepository) : ViewModel() {

    val isLoggedIn = MutableLiveData<Boolean>()
    val token = MutableLiveData<String>()
    val error = MutableLiveData<String>()

    fun login(phone: String, code: String) {
        Observable.fromCallable {
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
}