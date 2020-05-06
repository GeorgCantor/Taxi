package taxi.kassa.view.fragment

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import taxi.kassa.R
import taxi.kassa.base.BaseAndroidTest
import taxi.kassa.view.MainActivity

@RunWith(AndroidJUnit4ClassRunner::class)
class WriteMessageFragmentTest : BaseAndroidTest() {

    @get: Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun input_text_and_submit() {
        if (isUserLoggedIn()) {
            onView(withId(R.id.support_service_view)).perform(click())
            onView(withId(R.id.write_to_us_view)).perform(click())
            onView(withId(R.id.message_edit_text)).perform(click()).perform(typeText("sss"))
            onView(withId(R.id.send_button)).perform(click())
            onView(withId(R.id.success_root_layout)).check(matches(ViewMatchers.isDisplayed()))
        }
    }
}