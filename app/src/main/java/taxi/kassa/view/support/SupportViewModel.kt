package taxi.kassa.view.support

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import taxi.kassa.model.Notification
import taxi.kassa.model.responses.Message
import taxi.kassa.repository.ApiRepository

class SupportViewModel(repository: ApiRepository) : ViewModel() {

    val notifications = MutableLiveData<MutableList<Notification>>()
    val incomingMessages = MutableLiveData<List<Message>>()

    init {
        viewModelScope.launch {
            notifications.postValue(repository.getNotificationsAsync().await())
//            incomingMessages.postValue(repository.getChatHistory("")?.response?.messages?.filter { it.side == ADMIN })
        }
    }
}