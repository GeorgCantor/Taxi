package taxi.kassa.view.orders

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import taxi.kassa.MyApplication
import taxi.kassa.R
import taxi.kassa.model.Notification
import taxi.kassa.model.Taxi
import taxi.kassa.repository.ApiRepository

class OrdersViewModel(
    app: Application,
    private val repository: ApiRepository
) : AndroidViewModel(app) {

    private val context = getApplication<MyApplication>()

    val taxis = MutableLiveData<MutableList<Taxi>>()
    val notifications = MutableLiveData<MutableList<Notification>>()

    fun getTaxis() {
        taxis.value = mutableListOf(
            Taxi(R.drawable.ic_yandex_mini, context.getString(R.string.yandex_title), "0"),
            Taxi(R.drawable.ic_gett_mini, context.getString(R.string.gett_title), "0"),
            Taxi(R.drawable.ic_citymobil_mini, context.getString(R.string.citymobil_title), "0")
        )

        notifications.value = repository.getNotifications()
    }
}