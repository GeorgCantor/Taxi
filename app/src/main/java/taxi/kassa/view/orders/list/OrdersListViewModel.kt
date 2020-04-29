package taxi.kassa.view.orders.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import taxi.kassa.model.responses.Orders
import taxi.kassa.repository.ApiRepository

class OrdersListViewModel(private val repository: ApiRepository) : ViewModel() {

    private val disposable = CompositeDisposable()

    val isProgressVisible = MutableLiveData<Boolean>().apply { this.value = true }
    val orders = MutableLiveData<Orders>()
    val error = MutableLiveData<String>()

    fun getOrders(offset: String) {
        disposable.add(
            Observable.fromCallable {
                repository.getOrders(offset)
                    ?.doFinally { isProgressVisible.postValue(false) }
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

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}