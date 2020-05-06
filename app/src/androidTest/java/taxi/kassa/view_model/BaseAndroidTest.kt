package taxi.kassa.view_model

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.test.core.app.ApplicationProvider
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

open class BaseAndroidTest {

    protected fun getContext(): Context = ApplicationProvider.getApplicationContext()

    protected fun mockLifecycleOwner(): LifecycleOwner {
        val owner: LifecycleOwner = mock(LifecycleOwner::class.java)
        val lifecycle = LifecycleRegistry(owner)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        `when`(owner.lifecycle).thenReturn(lifecycle)

        return owner
    }
}