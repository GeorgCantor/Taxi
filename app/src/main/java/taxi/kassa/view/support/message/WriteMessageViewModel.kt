package taxi.kassa.view.support.message

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import taxi.kassa.MyApplication
import taxi.kassa.R
import taxi.kassa.repository.Repository
import taxi.kassa.util.Constants.ERROR_504

class WriteMessageViewModel(
    app: Application,
    private val repository: Repository
) : AndroidViewModel(app) {

    private val context = getApplication<MyApplication>()

    val isProgressVisible = MutableLiveData<Boolean>().apply { value = false }
    val error = MutableLiveData<String>()
    val isMessageSent = MutableLiveData<Boolean>().apply { value = false }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable.message) {
            ERROR_504 -> error.postValue(context.getString(R.string.internet_unavailable))
            else -> error.postValue(throwable.message)
        }
        isProgressVisible.postValue(false)
    }

    fun sendMessage(message: String) {
        isProgressVisible.value = true

        viewModelScope.launch(exceptionHandler) {
            val response = repository.sendMessage(message)
            isMessageSent.postValue(response?.success)
            error.postValue(response?.errorMsg)
            isProgressVisible.postValue(false)
        }
    }
}