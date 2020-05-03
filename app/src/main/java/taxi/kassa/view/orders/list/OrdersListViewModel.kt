package taxi.kassa.view.orders.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import taxi.kassa.MyApplication
import taxi.kassa.R
import taxi.kassa.model.responses.Orders
import taxi.kassa.repository.ApiRepository

class OrdersListViewModel(
    app: Application,
    private val repository: ApiRepository
) : AndroidViewModel(app) {

    private val context = getApplication<MyApplication>()

    val isProgressVisible = MutableLiveData<Boolean>().apply { this.value = true }
    val orders = MutableLiveData<Orders>()
    val error = MutableLiveData<String>()

    fun getOrders(offset: String) {
        viewModelScope.launch {
            try {
                val response = repository.getOrders(offset)
                orders.postValue(response?.response)
                error.postValue(response?.errorMsg)
                isProgressVisible.postValue(false)
            } catch (e: HttpException) {
                error.postValue(context.getString(R.string.internet_unavailable))
                isProgressVisible.postValue(false)
            }
        }
    }
}