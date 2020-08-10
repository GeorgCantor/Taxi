package taxi.kassa.view.accounts_cards.accounts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import taxi.kassa.R
import taxi.kassa.model.Notification
import taxi.kassa.model.responses.Account
import taxi.kassa.repository.Repository
import taxi.kassa.util.Constants.ERROR_504

class AccountsViewModel(
    app: Application,
    private val repository: Repository
) : AndroidViewModel(app) {

    val isProgressVisible = MutableLiveData<Boolean>().apply { value = true }
    val accounts = MutableLiveData<List<Account>>()
    val showSuccessScreen = MutableLiveData<Boolean>()
    val deletionStatus = MutableLiveData<String>()
    val error = MutableLiveData<String>()
    val notifications = MutableLiveData<MutableList<Notification>>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable.message) {
            ERROR_504 -> error.postValue(app.baseContext.getString(R.string.internet_unavailable))
            else -> error.postValue(throwable.message)
        }
        isProgressVisible.postValue(false)
    }

    fun getAccounts() {
        viewModelScope.launch(exceptionHandler) {
            val response = repository.getAccounts()
            accounts.postValue(response?.response?.info?.filter { it.autoPay == "0" })
            error.postValue(response?.errorMsg)
            isProgressVisible.postValue(false)
            notifications.postValue(repository.getNotificationsAsync().await())
        }
    }

    fun deleteAccount(accountId: Int) {
        isProgressVisible.value = true

        viewModelScope.launch(exceptionHandler) {
            val response = repository.deleteAccount(accountId)
            deletionStatus.postValue(response?.response?.status)
            error.postValue(response?.errorMsg)
            getAccounts()
            isProgressVisible.postValue(false)
        }
    }

    fun createAccount(
        firstName: String,
        lastName: String,
        middleName: String,
        accountNumber: String,
        bik: String
    ) {
        isProgressVisible.value = true

        viewModelScope.launch(exceptionHandler) {
            val response = repository.createAccount(
                firstName, lastName, middleName, accountNumber, bik
            )
            showSuccessScreen.postValue(response?.success)
            error.postValue(response?.errorMsg)
            getAccounts()
            isProgressVisible.postValue(false)
        }
    }
}