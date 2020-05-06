package taxi.kassa.view.fragment

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import taxi.kassa.R
import taxi.kassa.base.BaseAndroidTest
import taxi.kassa.view.MainActivity

@RunWith(AndroidJUnit4ClassRunner::class)
class ConnectionFragmentTest : BaseAndroidTest() {

    @get: Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun open_connection_fragment_in_yandex() {
        if (!isUserLoggedIn()) {
            onView(withId(R.id.signup_button)).perform(click())
            onView(withId(R.id.download_documents_button)).perform(click())
            onView(withId(R.id.yandex_background)).perform(click())
            onView(withId(R.id.yandex_block)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun make_photo_and_return() {
        if (!isUserLoggedIn()) {
            onView(withId(R.id.signup_button)).perform(click())
            onView(withId(R.id.download_documents_button)).perform(click())
            onView(withId(R.id.city_background)).perform(click())
            onView(withId(R.id.city_driver_license_front_edit_text)).perform(click())
            onView(withId(R.id.take_photo_btn)).perform(click())
            onView(isRoot()).perform(waitFor(3000))
            onView(withId(R.id.done)).perform(click())
            onView(withId(R.id.city_driver_license_front_cancel)).check(matches(isDisplayed()))
        }
    }
}