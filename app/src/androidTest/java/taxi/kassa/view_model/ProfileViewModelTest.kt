package taxi.kassa.view_model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.android.get
import org.mockito.Mock
import taxi.kassa.MyApplication
import taxi.kassa.base.BaseAndroidTest
import taxi.kassa.model.remote.ApiClient
import taxi.kassa.repository.Repository
import taxi.kassa.view.profile.ProfileViewModel

@RunWith(AndroidJUnit4::class)
class ProfileViewModelTest : BaseAndroidTest() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    val client = ApiClient

    private lateinit var viewModel: ProfileViewModel
    private lateinit var repository: Repository

    @Before
    fun setup() {
        repository = Repository(client.create(getContext()), preferenceManager)
        viewModel = ProfileViewModel(MyApplication().get(), preferenceManager, repository)
    }

    @Test
    fun get_owner_data() = runBlocking {
        viewModel.responseOwner.observe(mockLifecycleOwner(), Observer {
            if (isUserLoggedIn() && isNetworkAvailable()) assertNotNull(it)
        })
    }

    @Test
    fun check_internet() {
        viewModel.checkInternet()

        when (isUserLoggedIn()) {
            true -> {
                viewModel.isNetworkAvailable.observe(mockLifecycleOwner(), Observer {
                    if (isNetworkAvailable()) assertTrue(it) else assertFalse(it)
                })
            }
            false -> {
            }
        }
    }
}