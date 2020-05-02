package taxi.kassa.view.accounts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import taxi.kassa.model.Card
import taxi.kassa.model.Notification
import taxi.kassa.model.responses.AccountsList
import taxi.kassa.repository.ApiRepository

class AccountsViewModel(private val repository: ApiRepository) : ViewModel() {

    val isProgressVisible = MutableLiveData<Boolean>().apply { this.value = true }
    val creatingStatus = MutableLiveData<String>()
    val deletionStatus = MutableLiveData<String>()
    val accounts = MutableLiveData<AccountsList>()
    val error = MutableLiveData<String>()
    val notifications = MutableLiveData<MutableList<Notification>>()
    val cards = MutableLiveData<MutableList<Card>>()

    fun getAccounts() {
        viewModelScope.launch {
            val response = repository.getAccounts()

            val cardList = mutableListOf<Card>()
            response?.response?.info?.map {
                cardList.add(Card(it.cardNumber, it.cardDate))
            }
            cards.postValue(cardList)

            accounts.postValue(response?.response)
            error.postValue(response?.errorMsg)
            isProgressVisible.postValue(false)
        }

        notifications.value = repository.getNotifications()
    }

    fun createAccount(
        firstName: String,
        lastName: String,
        middleName: String,
        accountNumber: String,
        bankCode: String
    ) {
        isProgressVisible.value = true

        viewModelScope.launch {
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

        viewModelScope.launch {
            accounts.value?.info?.first()?.id?.let {
                val response = repository.deleteAccount(it)

                deletionStatus.postValue(response?.response?.status)
                error.postValue(response?.errorMsg)
                isProgressVisible.postValue(false)
            }
        }
    }
}