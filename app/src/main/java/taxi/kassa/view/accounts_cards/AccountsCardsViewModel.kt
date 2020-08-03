package taxi.kassa.view.accounts_cards

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

class AccountsCardsViewModel(
    app: Application,
    private val repository: Repository
) : AndroidViewModel(app) {

    val isProgressVisible = MutableLiveData<Boolean>().apply { this.value = true }
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
            isProgressVisible.postValue(false)
            notifications.postValue(repository.getNotificationsAsync().await())
        }
    }

//    fun deleteAccount() {
//        isProgressVisible.value = true
//
//        viewModelScope.launch(exceptionHandler) {
//            accounts.value?.info?.firstOrNull()?.id?.let {
//                val response = repository.deleteAccount(it)
//                deletionStatus.postValue(response?.response?.status)
//                error.postValue(response?.errorMsg)
//                isProgressVisible.postValue(false)
//            }
//        }
//    }

//    fun deleteCard(cardId: Int) {
//        isProgressVisible.value = true
//
//        viewModelScope.launch(exceptionHandler) {
//            val response = repository.deleteCard(cardId)
//            deletionStatus.postValue(response?.response?.status)
//            error.postValue(response?.errorMsg)
//            isProgressVisible.postValue(false)
//        }
//    }
}