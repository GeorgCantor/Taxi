package taxi.kassa.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import taxi.kassa.R
import taxi.kassa.model.Notification
import taxi.kassa.util.Constants.MESSAGE
import taxi.kassa.util.Constants.PUSH_PATTERN
import taxi.kassa.util.Constants.TITLE
import taxi.kassa.util.Constants.accessToken
import taxi.kassa.util.Constants.myDateFormatSymbols
import taxi.kassa.util.hideKeyboard
import taxi.kassa.util.observe
import taxi.kassa.util.runDelayed
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = getViewModel()

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
                viewModel.notifications.observe(this) {
                    var notifications = it
                    val newNotification = Notification(title, message, date, System.currentTimeMillis(), true)

                    when (notifications.isNullOrEmpty()) {
                        true -> notifications = mutableListOf(newNotification)
                        false -> notifications.add(newNotification)
                    }

                    viewModel.saveNotifications(notifications)
                }
            }
        }

        viewModel.token.observe(this) { token ->
            accessToken = token

            val navHostFragment = nav_host_fragment as NavHostFragment
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
    }

    override fun onResume() {
        super.onResume()
        // if a custom keyboard was opened before exiting, both can be opened
        100L.runDelayed { this.root_layout.hideKeyboard() }
    }
}
