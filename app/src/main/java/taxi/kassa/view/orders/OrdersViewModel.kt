package taxi.kassa.view.orders

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import taxi.kassa.MyApplication
import taxi.kassa.R
import taxi.kassa.model.Notification
import taxi.kassa.model.Taxi
import taxi.kassa.repository.Repository

class OrdersViewModel(
    app: Application,
    repository: Repository
) : AndroidViewModel(app) {

    private val context = getApplication<MyApplication>()

    val taxis = MutableLiveData<MutableList<Taxi>>()
    val notifications = MutableLiveData<MutableList<Notification>>()

    init {
        viewModelScope.launch {
            notifications.postValue(repository.getNotificationsAsync().await())
        }

        taxis.value = mutableListOf(
            Taxi(R.drawable.ic_yandex_mini, context.getString(R.string.yandex_title), "0"),
            Taxi(R.drawable.ic_gett_mini, context.getString(R.string.gett_title), "0"),
            Taxi(R.drawable.ic_citymobil_mini, context.getString(R.string.citymobil_title), "0")
        )
    }
}