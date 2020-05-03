package taxi.kassa.view.withdraws

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import taxi.kassa.MyApplication
import taxi.kassa.R
import taxi.kassa.model.Notification
import taxi.kassa.model.responses.Withdraws
import taxi.kassa.repository.ApiRepository

class WithdrawsViewModel(
    app: Application,
    private val repository: ApiRepository
) : AndroidViewModel(app) {

    private val context = getApplication<MyApplication>()

    val isProgressVisible = MutableLiveData<Boolean>().apply { this.value = true }
    val withdraws = MutableLiveData<Withdraws>()
    val error = MutableLiveData<String>()
    val notifications = MutableLiveData<MutableList<Notification>>()

    init {
        viewModelScope.launch {
            try {
                val response = repository.getWithdraws()
                withdraws.postValue(response?.response)
                error.postValue(response?.errorMsg)
                isProgressVisible.postValue(false)
            } catch (e: HttpException) {
                error.postValue(context.getString(R.string.internet_unavailable))
                isProgressVisible.postValue(false)
            }
        }

        notifications.value = repository.getNotifications()
    }
}