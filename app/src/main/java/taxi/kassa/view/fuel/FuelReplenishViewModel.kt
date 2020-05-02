package taxi.kassa.view.fuel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import taxi.kassa.MyApplication
import taxi.kassa.model.Notification
import taxi.kassa.model.responses.ResponseOwner
import taxi.kassa.repository.ApiRepository
import taxi.kassa.util.isNetworkAvailable

class FuelReplenishViewModel(
    app: Application,
    private val repository: ApiRepository
) : AndroidViewModel(app) {

    val isProgressVisible = MutableLiveData<Boolean>().apply { this.value = true }
    val isNetworkAvailable = MutableLiveData<Boolean>()
    val responseOwner = MutableLiveData<ResponseOwner>()
    val error = MutableLiveData<String>()
    val notifications = MutableLiveData<MutableList<Notification>>()

    init {
        viewModelScope.launch {
            val response = repository.getOwner()
            responseOwner.postValue(response?.response)
            error.postValue(response?.errorMsg)
            isProgressVisible.postValue(false)
        }

        notifications.value = repository.getNotifications()
        isNetworkAvailable.value = getApplication<MyApplication>().isNetworkAvailable()
    }
}