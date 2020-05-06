package taxi.kassa.view.fragment

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.hamcrest.core.IsNot.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import taxi.kassa.R
import taxi.kassa.view.MainActivity

@RunWith(AndroidJUnit4ClassRunner::class)
class FuelReplenishFragmentTest {

    @get: Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun click_replenish_if_input_is_empty() {
        onView(withId(R.id.balance_view)).perform(click())
        onView(withId(R.id.replenish_rosneft_tv)).perform(click())
        onView(withId(R.id.replenish_button)).perform(click())
        onView(withText(R.string.input_error)).check(matches(isDisplayed()))
    }

    @Test
    fun is_keyboard_appear_when_click_on_input() {
        onView(withId(R.id.balance_view)).perform(click())
        onView(withId(R.id.replenish_rosneft_tv)).perform(click())
        onView(withId(R.id.replenish_button)).perform(click())
        onView(withId(R.id.enter_amount_edit_text)).perform(click())
        onView(withId(R.id.keyboard)).check(matches(isDisplayed()))
    }

    @Test
    fun is_keyboard_disappear_when_pressed_back() {
        is_keyboard_appear_when_click_on_input()
        pressBack()
        onView(withId(R.id.keyboard)).check(matches(not(isDisplayed())))
    }
}