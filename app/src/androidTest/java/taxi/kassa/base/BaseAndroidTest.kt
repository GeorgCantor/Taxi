package taxi.kassa.base

import android.content.Context
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import taxi.kassa.util.Constants
import taxi.kassa.util.PreferenceManager
import taxi.kassa.util.isNetworkAvailable

open class BaseAndroidTest {

    protected fun getContext(): Context = ApplicationProvider.getApplicationContext()

    val preferenceManager = PreferenceManager(getContext())

    protected fun isUserLoggedIn(): Boolean {
        val token = preferenceManager.getString(Constants.TOKEN) ?: ""

        return token.isNotEmpty()
    }

    protected fun isNetworkAvailable() = getContext().isNetworkAvailable()

    protected fun mockLifecycleOwner(): LifecycleOwner {
        val owner: LifecycleOwner = mock(LifecycleOwner::class.java)
        val lifecycle = LifecycleRegistry(owner)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        `when`(owner.lifecycle).thenReturn(lifecycle)

        return owner
    }

    protected fun waitFor(delay: Long): ViewAction {
        return object : ViewAction {
            override fun perform(uiController: UiController?, view: View?) {
                uiController?.loopMainThreadForAtLeast(delay)
            }

            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isRoot()
            }

            override fun getDescription(): String {
                return "wait for " + delay + "milliseconds"
            }
        }
    }
}