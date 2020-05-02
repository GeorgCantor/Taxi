package taxi.kassa.view.withdraws.withdraw_create

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import taxi.kassa.model.Card
import taxi.kassa.model.Notification
import taxi.kassa.model.responses.AccountsList
import taxi.kassa.model.responses.ResponseOwner
import taxi.kassa.repository.ApiRepository

class WithdrawCreateViewModel(private val repository: ApiRepository) : ViewModel() {

    private val accountId = MutableLiveData<Int>()

    val isProgressVisible = MutableLiveData<Boolean>().apply { this.value = true }
    val accounts = MutableLiveData<AccountsList>()
    val creatingStatus = MutableLiveData<String>()
    val deletionStatus = MutableLiveData<String>()
    val responseOwner = MutableLiveData<ResponseOwner>()
    val error = MutableLiveData<String>()
    val notifications = MutableLiveData<MutableList<Notification>>()
    val cards = MutableLiveData<MutableList<Card>>()

    init {
        viewModelScope.launch {
            val response = repository.getOwner()
            responseOwner.postValue(response?.response)
            error.postValue(response?.errorMsg)
            isProgressVisible.postValue(false)
        }

        viewModelScope.launch {
            val response = repository.getAccounts()

            val cardList = mutableListOf<Card>()
            response?.response?.info?.map {
                cardList.add(Card(it.cardNumber, it.cardDate))
            }
            cards.postValue(cardList)

            accountId.postValue(response?.response?.info?.first()?.id)
            accounts.postValue(response?.response)
            error.postValue(response?.errorMsg)
            isProgressVisible.postValue(false)
        }

        notifications.value = repository.getNotifications()
    }

    fun deleteAccount() {
        isProgressVisible.value = true

        viewModelScope.launch {
            accounts.value?.info?.first()?.id?.let {
                val response = repository.deleteAccount(it)

                deletionStatus.postValue(response?.response?.status)
                error.postValue(response?.errorMsg)
                isProgressVisible.postValue(false)
            }
        }
    }

    fun createWithdraw(sourceId: Int, amount: String?) {
        isProgressVisible.value = true

        viewModelScope.launch {
            accountId.value?.let { id ->
                val response = repository.createWithdraw(sourceId, amount, id)

                creatingStatus.postValue(response?.response?.status)
                error.postValue(response?.errorMsg)
                isProgressVisible.postValue(false)
            }
        }
    }
}