package taxi.kassa.view.auth.auth_sign_up

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import taxi.kassa.MyApplication
import taxi.kassa.R
import taxi.kassa.repository.ApiRepository
import taxi.kassa.util.Constants.KEY
import taxi.kassa.util.isNetworkAvailable

class AuthSignUpViewModel(
    app: Application,
    private val repository: ApiRepository
) : AndroidViewModel(app) {

    private val context = getApplication<MyApplication>()

    val isProgressVisible = MutableLiveData<Boolean>().apply { this.value = false }
    val isNetworkAvailable = MutableLiveData<Boolean>()
    val isSignUp = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        error.postValue(context.getString(R.string.internet_unavailable))
        isProgressVisible.postValue(false)
    }

    fun signUp(phone: String) {
        isProgressVisible.value = true

        viewModelScope.launch(exceptionHandler) {
            val response = repository.signUp("", phone, 11, KEY)
            isSignUp.postValue(response?.success)
            response?.errorMsg?.let { error.postValue(it) }
            isProgressVisible.postValue(false)
        }

        isNetworkAvailable.value = context.isNetworkAvailable()
    }
}