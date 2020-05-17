package taxi.kassa.view.notifications

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import taxi.kassa.model.Notification
import taxi.kassa.repository.ApiRepository

class NotificationsViewModel(private val repository: ApiRepository) : ViewModel() {

    val notifications = MutableLiveData<MutableList<Notification>>()
    val selectedNotification = MutableLiveData<Notification>()

    fun getNotifications() {
        viewModelScope.launch {
            notifications.postValue(repository.getNotificationsAsync().await())
        }
    }

    fun setSelectedNotification(notification: Notification) {
        selectedNotification.value = notification
    }
}