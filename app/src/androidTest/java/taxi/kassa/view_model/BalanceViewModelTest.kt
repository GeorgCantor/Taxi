package taxi.kassa.view_model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertNotNull
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
import taxi.kassa.view.balance.BalanceViewModel

@RunWith(AndroidJUnit4::class)
class BalanceViewModelTest : BaseAndroidTest() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    val client = ApiClient

    private lateinit var viewModel: BalanceViewModel
    private lateinit var repository: Repository

    @Before
    fun setup() {
        repository = Repository(client.create(getContext()), preferenceManager)
        viewModel = BalanceViewModel(MyApplication().get(), repository)
    }

    @Test
    fun get_owner_data() = runBlocking {
        viewModel.responseOwner.observe(mockLifecycleOwner(), Observer {
            if (isUserLoggedIn() && isNetworkAvailable()) assertNotNull(it)
        })
    }
}