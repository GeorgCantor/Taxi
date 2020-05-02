package taxi.kassa.view.registration.connection

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import taxi.kassa.model.LoadImage

class ConnectionViewModel : ViewModel() {

    val loadedImages = MutableLiveData<MutableList<LoadImage>>()
    private val images = mutableListOf<LoadImage>()
    private var selected = 0

    fun setLoadImage(bitmap: Bitmap) {
        val image = LoadImage(selected, bitmap)
        images.add(image)
        loadedImages.value = images
    }

    fun setSelected(selectedNo: Int) {
        selected = selectedNo
    }

    fun removeLoadImage(selectedNo: Int) {
        var removed = LoadImage(0, null)
        images.map {
            if (selectedNo == it.id) removed = it
        }
        images.remove(removed)
        loadedImages.value = images
    }
}