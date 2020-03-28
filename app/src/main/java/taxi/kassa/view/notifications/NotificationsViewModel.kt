package taxi.kassa.view.notifications

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import taxi.kassa.model.Notification
import taxi.kassa.repository.ApiRepository

class NotificationsViewModel(private val repository: ApiRepository) : ViewModel() {

    val notifications = MutableLiveData<MutableList<Notification>>()

    init {
        notifications.value = repository.getNotifications()
    }
}