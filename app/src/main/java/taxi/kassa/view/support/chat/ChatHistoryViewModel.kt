package taxi.kassa.view.support.chat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import taxi.kassa.MyApplication
import taxi.kassa.R
import taxi.kassa.model.responses.Message
import taxi.kassa.model.responses.Messages
import taxi.kassa.repository.Repository
import taxi.kassa.util.Constants.ADMIN
import taxi.kassa.util.Constants.ERROR_504

class ChatHistoryViewModel(
    app: Application,
    private val repository: Repository
) : AndroidViewModel(app) {

    private val context = getApplication<MyApplication>()

    val isProgressVisible = MutableLiveData<Boolean>().apply { this.value = true }
    val error = MutableLiveData<String>()
    val messages = MutableLiveData<Messages>()
    val incomingMessages = MutableLiveData<List<Message>>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable.message) {
            ERROR_504 -> error.postValue(context.getString(R.string.internet_unavailable))
            else -> error.postValue(throwable.message)
        }
        isProgressVisible.postValue(false)
    }

    fun getMessages(offset: String) {
        viewModelScope.launch(exceptionHandler) {
            val response = repository.getChatHistory(offset)
            messages.postValue(response?.response)
            error.postValue(response?.errorMsg)
            incomingMessages.postValue(response?.response?.messages?.filter { it.side == ADMIN })
        }
    }
}