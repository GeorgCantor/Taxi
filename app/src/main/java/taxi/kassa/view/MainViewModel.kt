package taxi.kassa.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import taxi.kassa.model.Notification
import taxi.kassa.repository.ApiRepository

class MainViewModel(private val repository: ApiRepository) : ViewModel() {

    val token = MutableLiveData<String>()
    val notifications = MutableLiveData<MutableList<Notification>>()

    init {
        viewModelScope.launch {
            token.postValue(repository.getTokenAsync().await())
            notifications.postValue(repository.getNotificationsAsync().await())
        }
    }

    fun saveNotifications(notifications: MutableList<Notification>) {
        viewModelScope.launch {
            repository.saveNotificationsAsync(notifications).onAwait
        }
    }
}