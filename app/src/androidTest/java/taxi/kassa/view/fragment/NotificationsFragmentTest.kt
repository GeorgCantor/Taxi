package taxi.kassa.view.fragment

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import taxi.kassa.R
import taxi.kassa.base.BaseAndroidTest
import taxi.kassa.util.Constants.NOTIFICATIONS
import taxi.kassa.view.MainActivity

@RunWith(AndroidJUnit4ClassRunner::class)
class NotificationsFragmentTest : BaseAndroidTest() {

    @get: Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun open_notification() {
        val notEmpty = preferenceManager.getNotifications(NOTIFICATIONS)?.isNotEmpty()
        if (isUserLoggedIn() && notEmpty == true) {
            onView(withId(R.id.notification_image)).perform(click())
            onView(withId(R.id.notifications_recycler))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
            onView(withId(R.id.title_tv)).check(matches(isDisplayed()))
            onView(withId(R.id.message_tv)).check(matches(isDisplayed()))
            onView(withId(R.id.ok_button)).check(matches(isDisplayed()))
        }
    }
}