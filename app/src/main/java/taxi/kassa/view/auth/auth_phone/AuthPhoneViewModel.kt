package taxi.kassa.view.auth.auth_phone

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import taxi.kassa.MyApplication
import taxi.kassa.repository.ApiRepository
import taxi.kassa.util.isNetworkAvailable

class AuthPhoneViewModel(
    app: Application,
    private val repository: ApiRepository
) : AndroidViewModel(app) {

    val isProgressVisible = MutableLiveData<Boolean>().apply { this.value = false }
    val isNetworkAvailable = MutableLiveData<Boolean>()
    val isLoggedIn = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    fun login(phone: String) {
        isProgressVisible.value = true

        viewModelScope.launch {
            val response = repository.login(phone)
            isLoggedIn.postValue(response?.success)
            response?.errorMsg?.let { error.postValue(it) }
            isProgressVisible.postValue(false)
        }

        isNetworkAvailable.value = getApplication<MyApplication>().isNetworkAvailable()
    }
}