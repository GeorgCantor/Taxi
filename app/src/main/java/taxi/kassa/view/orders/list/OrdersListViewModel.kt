package taxi.kassa.view.orders.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import taxi.kassa.model.responses.Orders
import taxi.kassa.repository.ApiRepository

class OrdersListViewModel(private val repository: ApiRepository) : ViewModel() {

    val isProgressVisible = MutableLiveData<Boolean>().apply { this.value = true }
    val orders = MutableLiveData<Orders>()
    val error = MutableLiveData<String>()

    fun getOrders(offset: String) {
        viewModelScope.launch {
            val response = repository.getOrders(offset)
            orders.postValue(response?.response)
            error.postValue(response?.errorMsg)
            isProgressVisible.postValue(false)
        }
    }
}