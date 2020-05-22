package taxi.kassa.model.remote.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import taxi.kassa.MyApplication
import taxi.kassa.model.Notification
import taxi.kassa.util.Constants.NOTIFICATIONS
import taxi.kassa.util.Constants.PUSH_PATTERN
import taxi.kassa.util.PreferenceManager
import taxi.kassa.util.convertToTime

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("NEW_TOKEN", token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        MyApplication.appContext()?.let {
            val manager = PreferenceManager(it)
            var notifications = manager.getNotifications(NOTIFICATIONS)

            val newNotification = Notification(
                remoteMessage.notification?.title ?: "",
                remoteMessage.notification?.body ?: "",
                remoteMessage.sentTime.convertToTime(PUSH_PATTERN),
                System.currentTimeMillis(),
                true
            )

            if (notifications.isNullOrEmpty()) {
                notifications = mutableListOf(newNotification)
            } else {
                notifications.add(newNotification)
            }

            manager.saveNotifications(NOTIFICATIONS, notifications)
        }
    }
}