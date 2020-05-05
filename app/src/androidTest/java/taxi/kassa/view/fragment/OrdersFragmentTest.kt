package taxi.kassa.view.fragment

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasChildCount
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import taxi.kassa.R
import taxi.kassa.view.MainActivity

@RunWith(AndroidJUnit4ClassRunner::class)
class OrdersFragmentTest {

    @get: Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun check_number_of_tabs() {
        onView(withId(R.id.orders_view)).perform(ViewActions.click())
        onView(withId(R.id.taxi_recycler)).check(matches(hasChildCount(3)))
    }

    @Test
    fun press_back_button() {
        onView(withId(R.id.orders_view)).perform(ViewActions.click())
        onView(withId(R.id.back_arrow)).perform(ViewActions.click())
        onView(withId(R.id.profile_root_layout)).check(matches(ViewMatchers.isDisplayed()))
    }
}