package taxi.kassa.view.orders

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import taxi.kassa.R
import taxi.kassa.model.Taxi
import taxi.kassa.repository.ApiRepository

class OrdersViewModel(
    private val repository: ApiRepository,
    private val context: Context
) : ViewModel() {

    val taxis = MutableLiveData<MutableList<Taxi>>()

    fun getTaxis() {
        taxis.value = mutableListOf(
            Taxi(R.drawable.ic_yandex, context.getString(R.string.yandex_title), "0"),
            Taxi(R.drawable.ic_gett, context.getString(R.string.gett_title), "0"),
            Taxi(R.drawable.ic_citymobil, context.getString(R.string.citymobil_title), "0")
        )
    }
}