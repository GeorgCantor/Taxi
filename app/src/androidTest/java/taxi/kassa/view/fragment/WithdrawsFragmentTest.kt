package taxi.kassa.view.fragment

import android.app.Activity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import kotlinx.android.synthetic.main.fragment_withdraws.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import taxi.kassa.R
import taxi.kassa.base.BaseAndroidTest
import taxi.kassa.view.MainActivity

@RunWith(AndroidJUnit4ClassRunner::class)
class WithdrawsFragmentTest : BaseAndroidTest() {

    @get: Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    private fun getActivity(): Activity? {
        var activity: Activity? = null
        rule.scenario.onActivity {
            activity = it
        }

        return activity
    }

    @Test
    fun open_withdraw_fragment() {
        if (isUserLoggedIn() && isNetworkAvailable()) {
            onView(withId(R.id.withdrawal_applications_view)).perform(click())
            if (getActivity()?.empty_withdraws?.isVisible == false) {
                onView(withId(R.id.withdraws_recycler))
                    .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))
                onView(withId(R.id.withdraw_root_layout)).check(matches(isDisplayed()))
            }
        }
    }

    @Test
    fun check_empty_withdraws() {
        if (isUserLoggedIn() && isNetworkAvailable()) {
            onView(withId(R.id.withdrawal_applications_view)).perform(click())
            if (getActivity()?.withdraws_recycler?.adapter?.itemCount ?: 0 < 1) {
                onView(withId(R.id.empty_withdraws)).check(matches(isDisplayed()))
            }
        }
    }
}