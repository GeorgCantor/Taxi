package taxi.kassa.view.auth_phone

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import taxi.kassa.repository.ApiRepository

class AuthPhoneViewModel(private val repository: ApiRepository) : ViewModel() {

    val isLoggedIn = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    fun login(phone: String) {
        Observable.fromCallable {
            repository.login(phone)
                ?.subscribe({
                    isLoggedIn.postValue(it?.success)
                    it?.errorMsg?.let { error.postValue(it) }
                }, {
                })
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}