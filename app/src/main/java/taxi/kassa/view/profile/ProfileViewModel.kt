package taxi.kassa.view.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import taxi.kassa.MyApplication
import taxi.kassa.R
import taxi.kassa.model.Notification
import taxi.kassa.model.responses.Message
import taxi.kassa.model.responses.ResponseOwner
import taxi.kassa.repository.Repository
import taxi.kassa.util.Constants.ERROR_504
import taxi.kassa.util.Constants.UNREAD
import taxi.kassa.util.PreferenceManager
import taxi.kassa.util.isNetworkAvailable

class ProfileViewModel(
    app: Application,
    private val preferenceManager: PreferenceManager,
    private val repository: Repository
) : AndroidViewModel(app) {

    private val context = getApplication<MyApplication>()

    val isProgressVisible = MutableLiveData<Boolean>().apply { this.value = true }
    val isNetworkAvailable = MutableLiveData<Boolean>()
    val responseOwner = MutableLiveData<ResponseOwner>()
    val error = MutableLiveData<String>()
    val notifications = MutableLiveData<MutableList<Notification>>()
    val incomingMessages = MutableLiveData<List<Message>>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable.message) {
            ERROR_504 -> error.postValue(context.getString(R.string.internet_unavailable))
            else -> error.postValue(throwable.message)
        }
        isProgressVisible.postValue(false)
    }

    fun getUserData() {
        viewModelScope.launch(exceptionHandler) {
            repository.getOwner()?.apply {
                responseOwner.postValue(response)
                error.postValue(errorMsg)
            }
            notifications.postValue(repository.getNotificationsAsync().await())
            isProgressVisible.postValue(false)

            incomingMessages.postValue(repository.getChatHistory("")?.response?.messages?.filter { it.status == UNREAD })
        }
    }

    fun checkInternet() {
        isNetworkAvailable.value = context.isNetworkAvailable()
    }

    fun saveToPrefs(key: String, value: Any) {
        when (value) {
            is String -> preferenceManager.saveString(key, value)
            is Int -> preferenceManager.saveInt(key, value)
        }
    }

    fun getFromPrefs(key: String): Int? {
        return preferenceManager.getInt(key)
    }
}