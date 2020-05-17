package taxi.kassa.view.accounts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import taxi.kassa.MyApplication
import taxi.kassa.R
import taxi.kassa.model.Card
import taxi.kassa.model.Notification
import taxi.kassa.model.responses.AccountsList
import taxi.kassa.repository.ApiRepository
import taxi.kassa.util.Constants.ERROR_504

class AccountsViewModel(
    app: Application,
    private val repository: ApiRepository
) : AndroidViewModel(app) {

    private val context = getApplication<MyApplication>()

    val isProgressVisible = MutableLiveData<Boolean>().apply { this.value = true }
    val creatingStatus = MutableLiveData<String>()
    val deletionStatus = MutableLiveData<String>()
    val accounts = MutableLiveData<AccountsList>()
    val error = MutableLiveData<String>()
    val notifications = MutableLiveData<MutableList<Notification>>()
    val cards = MutableLiveData<MutableList<Card>>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable.message) {
            ERROR_504 -> error.postValue(context.getString(R.string.internet_unavailable))
            else -> error.postValue(throwable.message)
        }
        isProgressVisible.postValue(false)
    }

    fun getAccounts() {
        viewModelScope.launch(exceptionHandler) {
            val response = repository.getAccounts()

            val cardList = mutableListOf<Card>()
            response?.response?.info?.map {
                cardList.add(Card(it.cardNumber, it.cardDate))
            }
            cards.postValue(cardList)

            accounts.postValue(response?.response)
            error.postValue(response?.errorMsg)
            isProgressVisible.postValue(false)
            notifications.postValue(repository.getNotificationsAsync().await())
        }
    }

    fun createAccount(
        firstName: String,
        lastName: String,
        middleName: String,
        accountNumber: String,
        bankCode: String
    ) {
        isProgressVisible.value = true

        viewModelScope.launch(exceptionHandler) {
            val response = repository.createAccount(
                firstName, lastName, middleName, accountNumber, bankCode
            )
            creatingStatus.postValue(response?.response?.status)
            error.postValue(response?.errorMsg)
            isProgressVisible.postValue(false)
        }
    }

    fun deleteAccount() {
        isProgressVisible.value = true

        viewModelScope.launch(exceptionHandler) {
            accounts.value?.info?.firstOrNull()?.id?.let {
                val response = repository.deleteAccount(it)

                deletionStatus.postValue(response?.response?.status)
                error.postValue(response?.errorMsg)
                isProgressVisible.postValue(false)
            }
        }
    }
}