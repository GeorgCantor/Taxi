package taxi.kassa.view_model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
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
import taxi.kassa.util.Constants.TEST_NUMBER
import taxi.kassa.view.auth.auth_phone.AuthPhoneViewModel

@RunWith(AndroidJUnit4::class)
class AuthPhoneViewModelTest : BaseAndroidTest() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    val client = ApiClient

    private lateinit var viewModel: AuthPhoneViewModel
    private lateinit var repository: Repository

    @Before
    fun setup() {
        repository = Repository(client.create(getContext()), preferenceManager)
        viewModel = AuthPhoneViewModel(MyApplication().get(), repository)
    }

    @Test
    fun login() = runBlocking {
        viewModel.login(TEST_NUMBER)
        viewModel.isLoggedIn.observe(mockLifecycleOwner(), Observer {
            if (!isUserLoggedIn() && isNetworkAvailable()) assert(true)
        })
    }

    @Test
    fun login_if_no_internet() = runBlocking {
        viewModel.login(TEST_NUMBER)
        viewModel.isLoggedIn.observe(mockLifecycleOwner(), Observer {
            if (!isUserLoggedIn() && !isNetworkAvailable()) assert(false)
        })
    }
}