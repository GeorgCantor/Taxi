package taxi.kassa.view.orders

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import taxi.kassa.MyApplication
import taxi.kassa.R
import taxi.kassa.model.Taxi

class OrdersViewModel(app: Application) : AndroidViewModel(app) {

    private val context = getApplication<MyApplication>()

    val taxis = MutableLiveData<MutableList<Taxi>>()

    fun getTaxis() {
        taxis.value = mutableListOf(
            Taxi(R.drawable.ic_yandex, context.getString(R.string.yandex_title), "0"),
            Taxi(R.drawable.ic_gett, context.getString(R.string.gett_title), "0"),
            Taxi(R.drawable.ic_citymobil, context.getString(R.string.citymobil_title), "0")
        )
    }
}