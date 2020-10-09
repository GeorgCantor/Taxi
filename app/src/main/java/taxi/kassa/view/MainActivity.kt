package taxi.kassa.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import taxi.kassa.R
import taxi.kassa.model.Notification
import taxi.kassa.util.Constants.MESSAGE
import taxi.kassa.util.Constants.PUSH_PATTERN
import taxi.kassa.util.Constants.TITLE
import taxi.kassa.util.Constants.accessToken
import taxi.kassa.util.Constants.myDateFormatSymbols
import taxi.kassa.util.NetworkUtils.getNetworkLiveData
import taxi.kassa.util.hideKeyboard
import taxi.kassa.util.observe
import taxi.kassa.util.runDelayed
import taxi.kassa.util.slideAnim
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val viewModel by inject<MainViewModel>()
    private lateinit var reviewManager: ReviewManager
    private lateinit var reviewInfo: ReviewInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        reviewManager = ReviewManagerFactory.create(this)
        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                reviewInfo = task.result
                viewModel.isRateDialogShow.observe(this) { show ->
                    if (show) showInAppReview()
                }
            }
        }

        getNetworkLiveData(applicationContext).observe(this) { isConnected ->
            no_internet_warning.slideAnim(root_layout, !isConnected)
        }
    }

    override fun onResume() {
        super.onResume()
        // if a custom keyboard was opened before exiting, both can be opened
        100L.runDelayed { this.root_layout.hideKeyboard() }
    }

    /**
     * Google Play In-App Review API lets you prompt users to submit Play Store ratings
     * and reviews without the inconvenience of leaving your app
     */
    private fun showInAppReview() {
        if (::reviewInfo.isInitialized) reviewManager.launchReviewFlow(this, reviewInfo)
    }
}
