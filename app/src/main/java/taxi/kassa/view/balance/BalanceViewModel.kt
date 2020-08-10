package taxi.kassa.view.balance

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import taxi.kassa.MyApplication
import taxi.kassa.R
import taxi.kassa.model.Notification
import taxi.kassa.model.responses.ResponseOwner
import taxi.kassa.repository.Repository
import taxi.kassa.util.Constants.ERROR_504
import taxi.kassa.util.isNetworkAvailable

class BalanceViewModel(
    app: Application,
    private val repository: Repository
) : AndroidViewModel(app) {

    private val context = getApplication<MyApplication>()

    val isProgressVisible = MutableLiveData<Boolean>().apply { value = true }
    val isNetworkAvailable = MutableLiveData<Boolean>()
    val responseOwner = MutableLiveData<ResponseOwner>()
    val error = MutableLiveData<String>()
    val notifications = MutableLiveData<MutableList<Notification>>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable.message) {
            ERROR_504 -> error.postValue(context.getString(R.string.internet_unavailable))
            else -> error.postValue(throwable.message)
        }
        isProgressVisible.postValue(false)
    }

    fun getUserData() {
        viewModelScope.launch(exceptionHandler) {
            repository.getOwner()?.apply {
                responseOwner.postValue(response)
                error.postValue(errorMsg)
            }
            isProgressVisible.postValue(false)
            notifications.postValue(repository.getNotificationsAsync().await())
        }
    }

    fun checkInternet() {
        isNetworkAvailable.value = context.isNetworkAvailable()
    }
}