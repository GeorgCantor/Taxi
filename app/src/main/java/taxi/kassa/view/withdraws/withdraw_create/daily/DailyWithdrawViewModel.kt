package taxi.kassa.view.withdraws.withdraw_create.daily

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import taxi.kassa.R
import taxi.kassa.model.Notification
import taxi.kassa.model.responses.Account
import taxi.kassa.model.responses.AccountsList
import taxi.kassa.model.responses.ResponseOwner
import taxi.kassa.repository.Repository
import taxi.kassa.util.Constants.ERROR_504

class DailyWithdrawViewModel(
    app: Application,
    private val repository: Repository
) : AndroidViewModel(app) {

    private val accountId = MutableLiveData<Int>()

    val isProgressVisible = MutableLiveData<Boolean>().apply { this.value = true }
    val accounts = MutableLiveData<AccountsList>()
    val creatingStatus = MutableLiveData<String>()
    val responseOwner = MutableLiveData<ResponseOwner>()
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
            val response = repository.getAccounts()
            accountId.postValue(response?.response?.info?.firstOrNull()?.id)
            accounts.postValue(response?.response)
            error.postValue(response?.errorMsg)
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

    fun setAccountId(account: Account) {
        viewModelScope.launch(exceptionHandler) {
            val selectedAccount = accounts.value?.info?.find { it == account }
            accountId.postValue(selectedAccount?.id)
        }
    }

    fun createWithdraw(sourceId: Int, amount: String?) {
        isProgressVisible.value = true

        viewModelScope.launch(exceptionHandler) {
            accountId.value?.let { id ->
                val response = repository.createWithdraw(sourceId, amount, id)
                creatingStatus.postValue(response?.response?.status)
                error.postValue(response?.errorMsg)
                isProgressVisible.postValue(false)
            }
            getOwnerData()
        }
    }
}