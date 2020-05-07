package taxi.kassa.view.notifications

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import taxi.kassa.model.Notification
import taxi.kassa.repository.ApiRepository

class NotificationsViewModel(repository: ApiRepository) : ViewModel() {

    val notifications = MutableLiveData<MutableList<Notification>>()
    val selectedNotification = MutableLiveData<Notification>()

    init {
        notifications.value = repository.getNotifications()
    }

    fun setSelectedNotification(notification: Notification) {
        selectedNotification.value = notification
    }
}