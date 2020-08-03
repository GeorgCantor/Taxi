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

    val isProgressVisible = MutableLiveData<Boolean>().apply { this.value = true }
    val responseOwner = MutableLiveData<ResponseOwner>()
    val cards = MutableLiveData<List<Card>>()
    val creatingStatus = MutableLiveData<String>()
    val error = MutableLiveData<String>()
    val notifications = MutableLiveData<MutableList<Notification>>()

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

    fun createWithdraw(sourceId: Int, amount: String?, cardId: Int) {
        isProgressVisible.value = true

        viewModelScope.launch(exceptionHandler) {
            val response = repository.createWithdraw(sourceId, amount, cardId)
            creatingStatus.postValue(response?.response?.status)
            error.postValue(response?.errorMsg)
            isProgressVisible.postValue(false)
            getOwnerData()
        }
    }
}