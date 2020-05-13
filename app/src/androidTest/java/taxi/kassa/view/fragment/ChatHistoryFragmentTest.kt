package taxi.kassa.view.fragment

import android.app.Activity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import kotlinx.android.synthetic.main.fragment_chat_history.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import taxi.kassa.R
import taxi.kassa.base.BaseAndroidTest
import taxi.kassa.view.MainActivity

@RunWith(AndroidJUnit4ClassRunner::class)
class ChatHistoryFragmentTest : BaseAndroidTest() {

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
    fun is_messages_displayed() {
        if (isUserLoggedIn()) {
            onView(withId(R.id.support_service_view)).perform(click())
            onView(withId(R.id.chat_history_view)).perform(click())
            assert(getActivity()?.chat_recycler?.adapter?.itemCount ?: 0 > 1)
        }
    }
}