package taxi.kassa.view.orders.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import taxi.kassa.R
import taxi.kassa.model.responses.Orders
import taxi.kassa.repository.Repository
import taxi.kassa.util.Constants.ERROR_504

class OrdersListViewModel(
    app: Application,
    private val repository: Repository
) : AndroidViewModel(app) {

    val isProgressVisible = MutableLiveData<Boolean>().apply { value = true }
    val orders = MutableLiveData<Orders>()
    val error = MutableLiveData<String>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable.message) {
            ERROR_504 -> error.postValue(app.baseContext.getString(R.string.internet_unavailable))
            else -> error.postValue(throwable.message)
        }
        isProgressVisible.postValue(false)
    }

    fun getOrders(offset: String) {
        viewModelScope.launch(exceptionHandler) {
            repository.getOrders(offset)?.apply {
                orders.postValue(response)
                error.postValue(errorMsg)
            }
            isProgressVisible.postValue(false)
        }
    }
}