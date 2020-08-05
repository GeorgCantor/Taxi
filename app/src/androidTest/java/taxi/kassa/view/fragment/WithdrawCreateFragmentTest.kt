package taxi.kassa.view.fragment

import android.app.Activity
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Rule
import org.junit.runner.RunWith
import taxi.kassa.base.BaseAndroidTest
import taxi.kassa.view.MainActivity

@RunWith(AndroidJUnit4ClassRunner::class)
class WithdrawCreateFragmentTest : BaseAndroidTest() {

    @get: Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    private fun getActivity(): Activity? {
        var activity: Activity? = null
        rule.scenario.onActivity {
            activity = it
        }

        return activity
    }

//    @Test
//    fun open_withdraw_from_yandex() {
//        if (isUserLoggedIn() && isNetworkAvailable()) {
//            onView(withId(R.id.balance_view)).perform(click())
//            if (getActivity()?.withdraw_yandex_tv?.isEnabled == true) {
//                onView(withId(R.id.withdraw_yandex_tv)).perform(click())
//                onView(withId(R.id.withdraw_create_root_layout)).check(matches(isDisplayed()))
//                onView(withId(R.id.taxi_name)).check(matches(withText(R.string.yandex_title)))
//            }
//        }
//    }
}