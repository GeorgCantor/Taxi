package taxi.kassa.view.support

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import taxi.kassa.model.Message
import taxi.kassa.model.Notification
import taxi.kassa.repository.ApiRepository

class SupportViewModel(private val repository: ApiRepository) : ViewModel() {

    val notifications = MutableLiveData<MutableList<Notification>>()
    val incomingMessages = MutableLiveData<MutableList<Message>>()

    fun getNotifications() {
        notifications.value = repository.getNotifications()
        incomingMessages.value = repository.getChatHistory().filter { it.isIncoming } as MutableList
    }
}