package taxi.kassa.view.registration.connection

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import taxi.kassa.model.LoadImage

class ConnectionViewModel : ViewModel() {

    val loadedImages = MutableLiveData<MutableList<LoadImage>>()
    private val images = mutableListOf<LoadImage>()
    private var selected = 0

    fun setLoadImage(bitmap: Bitmap) {
        Observable.fromCallable {
            val image = LoadImage(selected, bitmap, true)
            images.add(image)
            loadedImages.postValue(images)
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun setSelected(selectedNo: Int) {
        selected = selectedNo
    }

    fun removeLoadImage(selectedNo: Int) {
        var removed = LoadImage(0, null, false)
        images.map {
            if (selectedNo == it.id) removed = it
        }
        images.remove(removed)
        loadedImages.value = images
    }
}