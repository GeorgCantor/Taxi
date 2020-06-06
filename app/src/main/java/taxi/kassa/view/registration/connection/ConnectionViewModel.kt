package taxi.kassa.view.registration.connection

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import taxi.kassa.MyApplication
import taxi.kassa.R
import taxi.kassa.model.LoadImage
import taxi.kassa.repository.Repository
import taxi.kassa.util.Constants.ERROR_504
import taxi.kassa.util.Constants.KEY
import taxi.kassa.util.getPhotoType
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ConnectionViewModel(
    app: Application,
    private val repository: Repository
) : AndroidViewModel(app) {

    private val context = getApplication<MyApplication>()
    private val images = mutableListOf<LoadImage>()
    private var selected = 0

    val isProgressVisible = MutableLiveData<Boolean>().apply { this.value = false }
    val isNetworkAvailable = MutableLiveData<Boolean>()
    val isRegistered = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()
    val loadedImages = MutableLiveData<MutableList<LoadImage>>()
    val phone = MutableLiveData<String>()
    val requestUid = MutableLiveData<String>()
    val taxiId = MutableLiveData<Int>()
    val gettId = MutableLiveData<String>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable.message) {
            ERROR_504 -> error.postValue(context.getString(R.string.internet_unavailable))
            else -> error.postValue(throwable.message)
        }
        isProgressVisible.postValue(false)
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

    fun sendPhoto(bitmap: Bitmap) {
        isProgressVisible.value = true

        val file = File(context.cacheDir, "photo.jpg")
        val outputStream: OutputStream = BufferedOutputStream(FileOutputStream(file))
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.close()

        val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
        val filePart = MultipartBody.Part.createFormData(
            "file", file.name, requestBody
        )

        viewModelScope.launch(exceptionHandler) {
            val response = repository.sendPhoto(
                filePart,
                requestUid.value ?: "",
                selected.getPhotoType()
            )
            response?.errorMsg?.let { error.postValue(it) }

            if (response?.success == true) {
                images.add(LoadImage(selected, bitmap))
                loadedImages.value = images
            }

            isProgressVisible.postValue(false)
        }
    }

    fun sendRegisterRequest() {
        isProgressVisible.value = true

        viewModelScope.launch(exceptionHandler) {
            val response = repository.sendRegisterRequest(
                taxiId.value ?: 0,
                phone.value ?: "",
                KEY,
                requestUid.value ?: "",
                gettId.value ?: ""
            )
            isRegistered.postValue(response?.success)
            response?.errorMsg?.let { error.postValue(it) }
            isProgressVisible.postValue(false)
        }
    }
}