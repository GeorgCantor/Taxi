package taxi.kassa.view.withdraws

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import taxi.kassa.model.Notification
import taxi.kassa.model.responses.Withdraws
import taxi.kassa.repository.ApiRepository

class WithdrawsViewModel(private val repository: ApiRepository) : ViewModel() {

    val isProgressVisible = MutableLiveData<Boolean>().apply { this.value = true }
    val withdraws = MutableLiveData<Withdraws>()
    val error = MutableLiveData<String>()
    val notifications = MutableLiveData<MutableList<Notification>>()

    init {
        viewModelScope.launch {
            val response = repository.getWithdraws()
            withdraws.postValue(response?.response)
            error.postValue(response?.errorMsg)
            isProgressVisible.postValue(false)
        }

        notifications.value = repository.getNotifications()
    }
}