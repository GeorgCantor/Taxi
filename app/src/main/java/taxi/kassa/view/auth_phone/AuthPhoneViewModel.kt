package taxi.kassa.view.auth_phone

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import taxi.kassa.repository.ApiRepository

class AuthPhoneViewModel(private val repository: ApiRepository) : ViewModel() {

    val isLoggedIn = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    @SuppressLint("CheckResult")
    fun login(phone: String) {
        Observable.fromCallable {
            repository.login(phone)?.subscribe(
                {
                    isLoggedIn.postValue(it?.success == true)
                }, {
                    error.postValue(it.message)
                }
            )
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}