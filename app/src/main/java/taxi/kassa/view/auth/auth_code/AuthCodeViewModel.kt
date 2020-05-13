package taxi.kassa.view.auth.auth_code

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import taxi.kassa.MyApplication
import taxi.kassa.R
import taxi.kassa.repository.ApiRepository
import taxi.kassa.util.Constants.ERROR_504
import taxi.kassa.util.Constants.TOKEN
import taxi.kassa.util.PreferenceManager

class AuthCodeViewModel(
    app: Application,
    private val preferenceManager: PreferenceManager,
    private val repository: ApiRepository
) : AndroidViewModel(app) {

    private val context = getApplication<MyApplication>()

    val isProgressVisible = MutableLiveData<Boolean>().apply { this.value = false }
    val isLoggedIn = MutableLiveData<Boolean>()
    val token = MutableLiveData<String>()
    val error = MutableLiveData<String>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable.message) {
            ERROR_504 -> error.postValue(context.getString(R.string.internet_unavailable))
            else -> error.postValue(throwable.message)
        }
        isProgressVisible.postValue(false)
    }

    fun login(phone: String, code: String) {
        isProgressVisible.value = true

        viewModelScope.launch(exceptionHandler) {
            val response = repository.getCode(phone, code)
            isLoggedIn.postValue(response?.success)
            response?.response?.let {
                token.postValue(it.token)
                saveToPrefs(it.token ?: "")
            }
            response?.errorMsg?.let { error.postValue(it) }
            isProgressVisible.postValue(false)
        }
    }

    private fun saveToPrefs(value: String) = preferenceManager.saveString(TOKEN, value)

    fun getFromPrefs(key: String): String? {
        return preferenceManager.getString(key)
    }
}