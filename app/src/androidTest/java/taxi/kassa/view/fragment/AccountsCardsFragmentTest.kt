package taxi.kassa.view.fragment

import android.app.Activity
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Rule
import org.junit.runner.RunWith
import taxi.kassa.base.BaseAndroidTest
import taxi.kassa.view.MainActivity

@RunWith(AndroidJUnit4ClassRunner::class)
class AccountsCardsFragmentTest : BaseAndroidTest() {

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
//    fun request_for_new_account_with_incorrect_values() {
//        if (isUserLoggedIn() && isNetworkAvailable()) {
//            onView(withId(R.id.accounts_and_cards_view)).perform(click())
//
//            if (getActivity()?.no_account_block?.isVisible == true) {
//                onView(withId(R.id.add_account_image)).perform(click())
//                onView(withId(R.id.name_edit_text))
//                    .perform(click())
//                    .perform(typeText(VISA))
//                pressBack()
//                onView(withId(R.id.surname_edit_text))
//                    .perform(click())
//                    .perform(typeText(VISA))
//                pressBack()
//                onView(withId(R.id.account_edit_text))
//                    .perform(click())
//                    .perform(typeText(TEST_NUMBER))
//                pressBack()
//                onView(withId(R.id.bik_edit_text))
//                    .perform(click())
//                    .perform(typeText(TEST_NUMBER))
//                pressBack()
//
//                onView(withId(R.id.add_account_button)).perform(click())
//                onView(withText(INCORRECT_VALUES_ERROR))
//                    .inRoot(withDecorView(not(getActivity()?.window?.decorView)))
//                    .check(matches(isDisplayed()))
//            }
//        }
//    }
}