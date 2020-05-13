package taxi.kassa.view.withdraws.withdraw

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import taxi.kassa.MyApplication
import taxi.kassa.R
import taxi.kassa.model.responses.AccountsList
import taxi.kassa.repository.ApiRepository
import taxi.kassa.util.Constants.ERROR_504

class WithdrawViewModel(
    app: Application,
    private val repository: ApiRepository
) : AndroidViewModel(app) {

    val isProgressVisible = MutableLiveData<Boolean>().apply { this.value = true }
    val accounts = MutableLiveData<AccountsList?>()
    val error = MutableLiveData<String>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable.message) {
            ERROR_504 -> error.postValue(getApplication<MyApplication>().getString(R.string.internet_unavailable))
            else -> error.postValue(throwable.message)
        }
        isProgressVisible.postValue(false)
    }

    init {
        viewModelScope.launch(exceptionHandler) {
            val response = repository.getAccounts()
            accounts.postValue(response?.response)
            error.postValue(response?.errorMsg)
            isProgressVisible.postValue(false)
        }
    }
}