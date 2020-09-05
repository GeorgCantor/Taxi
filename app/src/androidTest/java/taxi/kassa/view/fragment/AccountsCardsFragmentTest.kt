package taxi.kassa.view.fragment

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
import taxi.kassa.base.BaseAndroidTest
import taxi.kassa.view.MainActivity

@RunWith(AndroidJUnit4ClassRunner::class)
class AccountsCardsFragmentTest : BaseAndroidTest() {

    @get: Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun open_accounts_fragment() {
        if (isUserLoggedIn()) {
            onView(withId(R.id.accounts_and_cards_view)).perform(click())
            onView(withId(R.id.accounts_background)).perform(click())
            onView(withId(R.id.parent_layout)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun open_cards_fragment() {
        if (isUserLoggedIn()) {
            onView(withId(R.id.accounts_and_cards_view)).perform(click())
            onView(withId(R.id.cards_background)).perform(click())
            onView(withId(R.id.refresh_layout)).check(matches(isDisplayed()))
        }
    }
}