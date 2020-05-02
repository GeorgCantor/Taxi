package taxi.kassa.view.auth.auth_code

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import taxi.kassa.repository.ApiRepository

class AuthCodeViewModel(private val repository: ApiRepository) : ViewModel() {

    val isProgressVisible = MutableLiveData<Boolean>().apply { this.value = false }
    val isLoggedIn = MutableLiveData<Boolean>()
    val token = MutableLiveData<String>()
    val error = MutableLiveData<String>()

    fun login(phone: String, code: String) {
        isProgressVisible.value = true

        viewModelScope.launch {
            val response = repository.getCode(phone, code)
            isLoggedIn.postValue(response?.success)
            response?.response?.let { token.postValue(it.token) }
            response?.errorMsg?.let { error.postValue(it) }
            isProgressVisible.postValue(false)
        }
    }
}