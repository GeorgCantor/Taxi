package taxi.kassa.view_model

import android.graphics.BitmapFactory
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.android.get
import org.mockito.Mock
import taxi.kassa.MyApplication
import taxi.kassa.R
import taxi.kassa.base.BaseAndroidTest
import taxi.kassa.model.remote.ApiClient
import taxi.kassa.repository.Repository
import taxi.kassa.view.registration.connection.ConnectionViewModel

@RunWith(AndroidJUnit4::class)
class ConnectionViewModelTest : BaseAndroidTest() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    val client = ApiClient

    private lateinit var viewModel: ConnectionViewModel
    private lateinit var repository: Repository

    @Before
    fun setup() {
        repository = Repository(client.create(getContext()), preferenceManager)
        viewModel = ConnectionViewModel(MyApplication().get(), repository)
    }

    @Test
    fun check_loading_images() {
        viewModel.setSelected(1)
        viewModel.sendPhoto(BitmapFactory.decodeResource(getContext().resources, R.drawable.ic_gett))
        viewModel.setSelected(2)
        viewModel.sendPhoto(BitmapFactory.decodeResource(getContext().resources, R.drawable.ic_yandex))
        viewModel.loadedImages.observe(mockLifecycleOwner(), Observer {
            assertEquals(2, it.size)
        })
    }

    @Test
    fun check_removing_images() {
        viewModel.setSelected(1)
        viewModel.sendPhoto(BitmapFactory.decodeResource(getContext().resources, R.drawable.ic_gett))
        viewModel.setSelected(2)
        viewModel.sendPhoto(BitmapFactory.decodeResource(getContext().resources, R.drawable.ic_yandex))
        viewModel.removeLoadImage(1)
        viewModel.loadedImages.observe(mockLifecycleOwner(), Observer {
            assertEquals(1, it.size)
        })
    }
}