package taxi.kassa.view.orders

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import taxi.kassa.MyApplication
import taxi.kassa.R
import taxi.kassa.model.Taxi
import taxi.kassa.model.responses.Orders
import taxi.kassa.repository.ApiRepository

class OrdersViewModel(
    app: Application,
    private val repository: ApiRepository
) : AndroidViewModel(app) {

    private val context = getApplication<MyApplication>()
    private val disposable = CompositeDisposable()

    val taxis = MutableLiveData<MutableList<Taxi>>()
    val orders = MutableLiveData<Orders>()
    val error = MutableLiveData<String>()

    fun getTaxis() {
        taxis.value = mutableListOf(
            Taxi(R.drawable.ic_yandex, context.getString(R.string.yandex_title), "0"),
            Taxi(R.drawable.ic_gett, context.getString(R.string.gett_title), "0"),
            Taxi(R.drawable.ic_citymobil, context.getString(R.string.citymobil_title), "0")
        )
    }

    fun getOrders() {
        disposable.add(
            Observable.fromCallable {
                repository.getOrders(1)
                    ?.subscribe({
                        orders.postValue(it?.response)
                        error.postValue(it?.errorMsg)
                    }, {
                    })
            }
                .subscribeOn(Schedulers.io())
                .subscribe()
        )
    }
}