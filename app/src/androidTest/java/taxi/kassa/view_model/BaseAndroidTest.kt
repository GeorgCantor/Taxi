package taxi.kassa.view_model

import android.content.Context
import androidx.annotation.StyleRes
import androidx.test.core.app.ApplicationProvider
import taxi.kassa.R

open class BaseAndroidTest {

    protected fun getContext(): Context = ApplicationProvider.getApplicationContext()

    @StyleRes
    protected fun getAppTheme(): Int = R.style.AppTheme
}