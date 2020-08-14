package taxi.kassa.view.fuel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import taxi.kassa.MyApplication
import taxi.kassa.R
import taxi.kassa.model.Notification
import taxi.kassa.model.responses.ResponseOwner
import taxi.kassa.repository.Repository
import taxi.kassa.util.Constants.ERROR_504
import taxi.kassa.util.Constants.YANDEX
import taxi.kassa.util.isNetworkAvailable

class FuelReplenishViewModel(
    app: Application,
    private val repository: Repository
) : AndroidViewModel(app) {

    private val context = getApplication<MyApplication>()

    val isProgressVisible = MutableLiveData<Boolean>().apply { value = true }
    val isNetworkAvailable = MutableLiveData<Boolean>()
    val responseOwner = MutableLiveData<ResponseOwner>()
    val showSuccessScreen = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()
    val notifications = MutableLiveData<MutableList<Notification>>()
    val newFuelBalance = MutableLiveData<String>()
    val selectedTaxi = MutableLiveData<String>().apply { value = YANDEX }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable.message) {
            ERROR_504 -> error.postValue(context.getString(R.string.internet_unavailable))
            else -> error.postValue(throwable.message)
        }
        isProgressVisible.postValue(false)
    }

    fun getOwnerData() {
        viewModelScope.launch(exceptionHandler) {
            repository.getOwner()?.apply {
                responseOwner.postValue(response)
                error.postValue(errorMsg)
            }
            isProgressVisible.postValue(false)
            notifications.postValue(repository.getNotificationsAsync().await())
        }

        isNetworkAvailable.value = context.isNetworkAvailable()
    }

    fun refillFuelBalance(amount: Float) {
        isProgressVisible.value = true

        viewModelScope.launch(exceptionHandler) {
            repository.refillFuelBalance(selectedTaxi.value ?: YANDEX, amount)?.apply {
                response?.newFuelBalance?.let { newFuelBalance.postValue(it.toString()) }
                showSuccessScreen.postValue(success)
                error.postValue(errorMsg)
            }
            isProgressVisible.postValue(false)
            getOwnerData()
        }
    }
}