package taxi.kassa.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import taxi.kassa.model.Notification
import taxi.kassa.repository.Repository
import taxi.kassa.util.Constants.LAUNCHES
import taxi.kassa.util.PreferenceManager

class MainViewModel(
    private val repository: Repository,
    private val prefManager: PreferenceManager
) : ViewModel() {

    val token = MutableLiveData<String>()
    val notifications = MutableLiveData<MutableList<Notification>>()
    val isRateDialogShow = MutableLiveData<Boolean>()

    init {
        viewModelScope.launch {
            token.postValue(repository.getTokenAsync().await())
            notifications.postValue(repository.getNotificationsAsync().await())

            var numberOfLaunches = prefManager.getInt(LAUNCHES) ?: 0
            when (numberOfLaunches) {
                in 0..7 -> {
                    numberOfLaunches++
                    prefManager.saveInt(LAUNCHES, numberOfLaunches)
                }
                else -> isRateDialogShow.postValue(true)
            }
        }
    }

    fun saveNotifications(notifications: MutableList<Notification>) {
        viewModelScope.launch {
            repository.saveNotificationsAsync(notifications).onAwait
        }
    }
}