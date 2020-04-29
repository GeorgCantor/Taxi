package taxi.kassa.view.auth.auth_sign_up

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import taxi.kassa.MyApplication
import taxi.kassa.repository.ApiRepository
import taxi.kassa.util.Constants.KEY
import taxi.kassa.util.isNetworkAvailable

class AuthSignUpViewModel(
    app: Application,
    private val repository: ApiRepository
) : AndroidViewModel(app) {

    private val disposable = CompositeDisposable()

    val isProgressVisible = MutableLiveData<Boolean>().apply { this.value = false }
    val isNetworkAvailable = MutableLiveData<Boolean>()
    val isSignUp = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    fun signUp(phone: String) {
        disposable.add(
            Observable.fromCallable {
                repository.signUp("", phone, 11, KEY)
                    ?.doOnSubscribe { isProgressVisible.postValue(true) }
                    ?.doFinally { isProgressVisible.postValue(false) }
                    ?.subscribe({
                        isSignUp.postValue(it?.success)
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