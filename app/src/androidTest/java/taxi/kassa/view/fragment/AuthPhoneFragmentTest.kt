package taxi.kassa.view.fragment

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import taxi.kassa.R
import taxi.kassa.base.BaseAndroidTest
import taxi.kassa.util.Constants.TEST_NUMBER
import taxi.kassa.view.MainActivity

@RunWith(AndroidJUnit4ClassRunner::class)
class AuthPhoneFragmentTest : BaseAndroidTest() {

    companion object {
        private const val WRONG_FORMAT = "телефон введен в неверном формате"
    }

    @get: Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun is_auth_phone_fragment_displayed() {
        if (!isUserLoggedIn()) {
            onView(withId(R.id.login_button)).perform(click())
            onView(withId(R.id.back_arrow)).check(matches(isDisplayed()))
            onView(withId(R.id.login_button)).check(matches(isDisplayed()))
            onView(withId(R.id.keyboard)).check(matches(isDisplayed()))
            onView(withId(R.id.input_title))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.input_phone_number)))
            onView(withId(R.id.phone_edit_text))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.phone_start_symbols)))
            onView(withId(R.id.login_checkbox))
                .check(matches(isDisplayed()))
                .check(matches(isChecked()))
        }
    }

    @Test
    fun is_checkbox_work() {
        if (!isUserLoggedIn()) {
            onView(withId(R.id.login_button)).perform(click())
            onView(withId(R.id.phone_edit_text))
                .perform(click())
                .perform(typeText(TEST_NUMBER))
            onView(withId(R.id.login_checkbox)).perform(click())
            onView(withId(R.id.login_button)).perform(click())
            onView(withText(R.string.accept_conditions_error)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun not_login_if_no_internet() {
        if (!isUserLoggedIn() && !isNetworkAvailable()) {
            onView(withId(R.id.login_button)).perform(click())
            onView(withId(R.id.phone_edit_text))
                .perform(click())
                .perform(typeText(TEST_NUMBER))
            onView(withId(R.id.login_button)).perform(click())
            onView(withText(R.string.internet_unavailable)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun go_to_code_input_if_phone_register() {
        if (!isUserLoggedIn() && isNetworkAvailable()) {
            onView(withId(R.id.login_button)).perform(click())
            onView(withId(R.id.phone_edit_text))
                .perform(click())
                .perform(typeText(TEST_NUMBER))
            onView(withId(R.id.login_button)).perform(click())
            onView(isRoot()).perform(waitFor(3000))
            onView(withId(R.id.auth_code_root_layout)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun input_phone_in_wrong_format() {
        if (!isUserLoggedIn() && isNetworkAvailable()) {
            onView(withId(R.id.login_button)).perform(click())
            onView(withId(R.id.phone_edit_text))
                .perform(click())
                .perform(typeText("1234"))
            onView(withId(R.id.apply_btn)).perform(click())
            onView(isRoot()).perform(waitFor(1000))
            onView(withText(WRONG_FORMAT)).check(matches(isDisplayed()))
        }
    }
}