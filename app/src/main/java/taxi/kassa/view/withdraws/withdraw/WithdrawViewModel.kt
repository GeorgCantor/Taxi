package taxi.kassa.view.withdraws.withdraw

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import taxi.kassa.model.responses.AccountsList
import taxi.kassa.repository.ApiRepository

class WithdrawViewModel(private val repository: ApiRepository) : ViewModel() {

    val isProgressVisible = MutableLiveData<Boolean>().apply { this.value = true }
    val accounts = MutableLiveData<AccountsList?>()
    val error = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            val response = repository.getAccounts()
            accounts.postValue(response?.response)
            error.postValue(response?.errorMsg)
            isProgressVisible.postValue(false)
        }
    }
}