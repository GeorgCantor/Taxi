package taxi.kassa.view

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.activity_main.*
import taxi.kassa.R
import taxi.kassa.model.Notification
import taxi.kassa.util.Constants.MESSAGE
import taxi.kassa.util.Constants.NOTIFICATIONS
import taxi.kassa.util.Constants.PUSH_PATTERN
import taxi.kassa.util.Constants.TITLE
import taxi.kassa.util.Constants.TOKEN
import taxi.kassa.util.Constants.myDateFormatSymbols
import taxi.kassa.util.PreferenceManager
import taxi.kassa.util.hideKeyboard
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val manager = PreferenceManager(this)

        var title = ""
        var message = ""

        intent?.extras?.let {
            val dateFormat = SimpleDateFormat(PUSH_PATTERN, myDateFormatSymbols)
            val date: String = dateFormat.format(Date())

            it.keySet().map { key ->
                when (key) {
                    TITLE -> title = intent.extras?.get(key) as String
                    MESSAGE -> message = intent.extras?.get(key) as String
                }
            }

            if (message.isNotBlank()) {
                var notifications = manager.getNotifications(NOTIFICATIONS)
                val newNotification = Notification(title, message, date, true)

                if (notifications.isNullOrEmpty()) {
                    notifications = arrayListOf(newNotification)
                } else {
                    notifications.add(newNotification)
                }

                manager.saveNotifications(NOTIFICATIONS, notifications)
            }
        }

        val token = manager.getString(TOKEN) ?: ""

        val navHostFragment = navHostFragment as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_graph)

        when {
            token.isNotEmpty() -> {
                graph.startDestination = if (message.isEmpty()) R.id.profileFragment else R.id.notificationsFragment
            }
            else -> graph.startDestination = R.id.introFragment
        }

        navHostFragment.navController.graph = graph
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({ this.root_layout.hideKeyboard() }, 100)
    }
}
