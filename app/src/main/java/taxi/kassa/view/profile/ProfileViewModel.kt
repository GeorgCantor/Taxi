package taxi.kassa.view.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import taxi.kassa.MyApplication
import taxi.kassa.R
import taxi.kassa.model.Message
import taxi.kassa.model.Notification
import taxi.kassa.model.responses.ResponseOwner
import taxi.kassa.repository.ApiRepository
import taxi.kassa.util.PreferenceManager
import taxi.kassa.util.isNetworkAvailable

class ProfileViewModel(
    app: Application,
    private val preferenceManager: PreferenceManager,
    private val repository: ApiRepository
) : AndroidViewModel(app) {

    private val context = getApplication<MyApplication>()

    val isProgressVisible = MutableLiveData<Boolean>().apply { this.value = true }
    val isNetworkAvailable = MutableLiveData<Boolean>()
    val responseOwner = MutableLiveData<ResponseOwner>()
    val error = MutableLiveData<String>()
    val notifications = MutableLiveData<MutableList<Notification>>()
    val incomingMessages = MutableLiveData<MutableList<Message>>()

    init {
        viewModelScope.launch {
            try {
                val response = repository.getOwner()
                responseOwner.postValue(response?.response)
                error.postValue(response?.errorMsg)
                isProgressVisible.postValue(false)
            } catch (e: HttpException) {
                error.postValue(context.getString(R.string.internet_unavailable))
                isProgressVisible.postValue(false)
            }
        }

        notifications.value = repository.getNotifications()
        incomingMessages.value = repository.getChatHistory().filter { it.isIncoming } as MutableList
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