package taxi.kassa.view.fragment

import android.app.Activity
import androidx.core.view.isVisible
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import kotlinx.android.synthetic.main.fragment_accounts.*
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import taxi.kassa.R
import taxi.kassa.base.BaseAndroidTest
import taxi.kassa.util.Constants.TEST_NUMBER
import taxi.kassa.util.Constants.VISA
import taxi.kassa.view.MainActivity

@RunWith(AndroidJUnit4ClassRunner::class)
class AccountsCardsFragmentTest : BaseAndroidTest() {

    companion object {
        private const val INCORRECT_VALUES_ERROR = "Одно или несколько значений указаны неверно"
    }

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
    fun request_for_new_account_with_incorrect_values() {
        if (isUserLoggedIn() && isNetworkAvailable()) {
            onView(withId(R.id.accounts_and_cards_view)).perform(click())

            if (getActivity()?.no_account_block?.isVisible == true) {
                onView(withId(R.id.add_account_image)).perform(click())
                onView(withId(R.id.name_edit_text))
                    .perform(click())
                    .perform(typeText(VISA))
                pressBack()
                onView(withId(R.id.surname_edit_text))
                    .perform(click())
                    .perform(typeText(VISA))
                pressBack()
                onView(withId(R.id.account_edit_text))
                    .perform(click())
                    .perform(typeText(TEST_NUMBER))
                pressBack()
                onView(withId(R.id.bik_edit_text))
                    .perform(click())
                    .perform(typeText(TEST_NUMBER))
                pressBack()

                onView(withId(R.id.add_account_button)).perform(click())
                onView(withText(INCORRECT_VALUES_ERROR))
                    .inRoot(withDecorView(not(getActivity()?.window?.decorView)))
                    .check(matches(isDisplayed()))
            }
        }
    }
}