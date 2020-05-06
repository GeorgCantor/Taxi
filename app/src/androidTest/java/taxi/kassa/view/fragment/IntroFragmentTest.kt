package taxi.kassa.view.fragment

import androidx.test.espresso.Espresso.onView
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
class IntroFragmentTest : BaseAndroidTest() {

    @get: Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun is_intro_displayed() {
        if (!isUserLoggedIn()) {
            onView(withId(R.id.kassa_title))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.kassa)))
            onView(withId(R.id.taxi_title))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.taxi)))
            onView(withId(R.id.login_button)).check(matches(isDisplayed()))
            onView(withId(R.id.signup_button)).check(matches(isDisplayed()))
        }
    }
}