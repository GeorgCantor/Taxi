package taxi.kassa.view.withdraws.withdraw_create.instant

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import taxi.kassa.R
import taxi.kassa.model.Notification
import taxi.kassa.model.responses.Card
import taxi.kassa.model.responses.ResponseOwner
import taxi.kassa.repository.Repository
import taxi.kassa.util.Constants.ERROR_504

class InstantWithdrawViewModel(
    app: Application,
    private val repository: Repository
) : AndroidViewModel(app) {

    private val accountId = MutableLiveData<Int>()

    val isProgressVisible = MutableLiveData<Boolean>().apply { this.value = true }
    val responseOwner = MutableLiveData<ResponseOwner>()
    val error = MutableLiveData<String>()
    val notifications = MutableLiveData<MutableList<Notification>>()
    val cards = MutableLiveData<List<Card>>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable.message) {
            ERROR_504 -> error.postValue(app.baseContext.getString(R.string.internet_unavailable))
            else -> error.postValue(throwable.message)
        }
        isProgressVisible.postValue(false)
    }

    init {
        viewModelScope.launch(exceptionHandler) {
            val response = repository.getCards()
            cards.postValue(response?.response?.cards)
            isProgressVisible.postValue(false)
            notifications.postValue(repository.getNotificationsAsync().await())
        }
    }

    fun getOwnerData() {
        viewModelScope.launch(exceptionHandler) {
            val response = repository.getOwner()
            responseOwner.postValue(response?.response)
            error.postValue(response?.errorMsg)
            isProgressVisible.postValue(false)
        }
    }
}