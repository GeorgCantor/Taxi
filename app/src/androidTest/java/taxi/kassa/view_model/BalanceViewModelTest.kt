package taxi.kassa.view_model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.android.get
import org.mockito.Mock
import taxi.kassa.MyApplication
import taxi.kassa.model.remote.ApiClient
import taxi.kassa.repository.ApiRepository
import taxi.kassa.util.PreferenceManager
import taxi.kassa.view.balance.BalanceViewModel

@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class BalanceViewModelTest : BaseAndroidTest() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    val client = ApiClient

    private lateinit var viewModel: BalanceViewModel
    private lateinit var repository: ApiRepository

    @Before
    fun setup() {
        val preferenceManager = PreferenceManager(getContext())
        repository = ApiRepository(client.create(getContext()), preferenceManager)
        viewModel = BalanceViewModel(MyApplication().get(), repository)
    }

    @Test
    fun get_owner_data() = runBlocking {
        viewModel.responseOwner.observe(mockLifecycleOwner(), Observer {
            assertNotNull(it)
        })
    }
}