package taxi.kassa.view_model

import android.graphics.BitmapFactory
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import taxi.kassa.R
import taxi.kassa.base.BaseAndroidTest
import taxi.kassa.view.registration.connection.ConnectionViewModel

@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class ConnectionViewModelTest : BaseAndroidTest() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ConnectionViewModel

    @Before
    fun setup() {
        viewModel = ConnectionViewModel()
    }

    @Test
    fun check_loading_images() {
        viewModel.setSelected(1)
        viewModel.setLoadImage(BitmapFactory.decodeResource(getContext().resources, R.drawable.ic_gett))
        viewModel.setSelected(2)
        viewModel.setLoadImage(BitmapFactory.decodeResource(getContext().resources, R.drawable.ic_yandex))
        viewModel.loadedImages.observe(mockLifecycleOwner(), Observer {
            assertEquals(2, it.size)
        })
    }

    @Test
    fun check_removing_images() {
        viewModel.setSelected(1)
        viewModel.setLoadImage(BitmapFactory.decodeResource(getContext().resources, R.drawable.ic_gett))
        viewModel.setSelected(2)
        viewModel.setLoadImage(BitmapFactory.decodeResource(getContext().resources, R.drawable.ic_yandex))
        viewModel.removeLoadImage(1)
        viewModel.loadedImages.observe(mockLifecycleOwner(), Observer {
            assertEquals(1, it.size)
        })
    }
}