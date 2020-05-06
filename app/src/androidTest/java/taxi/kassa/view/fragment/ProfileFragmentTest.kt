package taxi.kassa.view.fragment

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.google.android.material.internal.ContextUtils.getActivity
import org.hamcrest.core.IsNot.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import taxi.kassa.R
import taxi.kassa.base.BaseAndroidTest
import taxi.kassa.view.MainActivity

@RunWith(AndroidJUnit4ClassRunner::class)
class ProfileFragmentTest : BaseAndroidTest() {

    @get: Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun open_balance_fragment() {
        if (isUserLoggedIn()) {
            onView(withId(R.id.balance_view)).perform(click())
            onView(withId(R.id.balance_root_layout)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun open_notifications_fragment() {
        if (isUserLoggedIn()) {
            onView(withId(R.id.notification_image)).perform(click())
            onView(withId(R.id.notifications_root_layout)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun open_notifications_fragment_and_press_back() {
        if (isUserLoggedIn()) {
            onView(withId(R.id.notification_image)).perform(click())
            onView(withId(R.id.notifications_root_layout)).check(matches(isDisplayed()))
            pressBack()
            onView(withId(R.id.profile_root_layout)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun open_phone_dialog() {
        if (isUserLoggedIn()) {
            onView(withId(R.id.phone_image)).perform(click())
            onView(withId(R.id.two_dialog_root_layout)).check(matches(isDisplayed()))
            onView(withId(R.id.title)).check(matches(isDisplayed()))
            onView(withId(R.id.message)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun show_toast_if_no_internet() {
        if (isUserLoggedIn() && !isNetworkAvailable()) {
            onView(withText(R.string.internet_unavailable))
                .inRoot(withDecorView(not(getActivity(getContext())?.window?.decorView)))
                .check(matches(isDisplayed()))
        }
    }
}