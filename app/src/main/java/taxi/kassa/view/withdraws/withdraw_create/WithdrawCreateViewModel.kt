package taxi.kassa.view.withdraws.withdraw_create

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import taxi.kassa.R
import taxi.kassa.model.Notification
import taxi.kassa.repository.Repository
import taxi.kassa.util.Constants.ERROR_504

class WithdrawCreateViewModel(
    app: Application,
    private val repository: Repository
) : AndroidViewModel(app) {

    val error = MutableLiveData<String>()
    val notifications = MutableLiveData<MutableList<Notification>>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable.message) {
            ERROR_504 -> error.postValue(app.baseContext.getString(R.string.internet_unavailable))
            else -> error.postValue(throwable.message)
        }
    }

    init {
        viewModelScope.launch(exceptionHandler) {
            notifications.postValue(repository.getNotificationsAsync().await())
        }
    }
}