package taxi.kassa.view.notifications

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import taxi.kassa.model.Notification

class NotificationsViewModel : ViewModel() {

    val notifications = MutableLiveData<MutableList<Notification>>()

    fun getNotifications() {
        notifications.value = mutableListOf(
            Notification(
                title = "Уважаемые водители!",
                message = "С 16 октября водителям для доступа в сервис нужен будет рейтинг не менее 4,5",
                date = "14:00, 10 окт."
            ),
            Notification(
                title = "Уважаемые водители!",
                message = "С 16 октября водителям для доступа в сервис нужен будет рейтинг не менее 4,5",
                date = "14:00, 10 окт."
            ),
            Notification(
                title = "Уважаемые водители!",
                message = "С 16 октября водителям для доступа в сервис нужен будет рейтинг не менее 4,5",
                date = "14:00, 10 окт."
            ),
            Notification(
                title = "Уважаемые водители!",
                message = "С 16 октября водителям для доступа в сервис нужен будет рейтинг не менее 4,5",
                date = "14:00, 10 окт."
            )
        )
    }
}