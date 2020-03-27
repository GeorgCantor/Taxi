package taxi.kassa.view.support.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import taxi.kassa.model.Message
import taxi.kassa.repository.ApiRepository

class ChatHistoryViewModel(private val repository: ApiRepository) : ViewModel() {

    val messages = MutableLiveData<MutableList<Message>>()
    val incomingMessages = MutableLiveData<MutableList<Message>>()

    fun getChatHistory() {
        messages.value = repository.getChatHistory()
        incomingMessages.value = repository.getChatHistory().filter { it.isIncoming } as MutableList
    }
}