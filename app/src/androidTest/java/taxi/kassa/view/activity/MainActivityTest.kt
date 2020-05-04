package taxi.kassa.view.activity

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import taxi.kassa.R
import taxi.kassa.view.MainActivity

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @get: Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun isActivityInView() {
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.root_layout)).check(matches(isDisplayed()))
    }

    @Test
    fun isBalanceFragmentOpen() {
        onView(withId(R.id.balance_view)).perform(click())
        onView(withId(R.id.balance_root_layout)).check(matches(isDisplayed()))
    }
}