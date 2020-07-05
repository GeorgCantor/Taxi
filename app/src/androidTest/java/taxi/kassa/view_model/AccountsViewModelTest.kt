package taxi.kassa.view_model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertNotNull
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
import taxi.kassa.util.Constants.NEW
import taxi.kassa.util.Constants.TEST_NUMBER
import taxi.kassa.view.accounts.AccountsViewModel

@RunWith(AndroidJUnit4::class)
class AccountsViewModelTest : BaseAndroidTest() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    val client = ApiClient

    private lateinit var viewModel: AccountsViewModel
    private lateinit var repository: Repository

    @Before
    fun setup() {
        repository = Repository(client.create(getContext()), preferenceManager)
        viewModel = AccountsViewModel(MyApplication().get(), repository)
    }

    @Test
    fun request_for_new_account_with_incorrect_values() {
        viewModel.createAccount(NEW, NEW, NEW, TEST_NUMBER, TEST_NUMBER)
        viewModel.error.observe(mockLifecycleOwner(), Observer {
            if (isUserLoggedIn() && isNetworkAvailable()) assertNotNull(it)
        })
    }
}