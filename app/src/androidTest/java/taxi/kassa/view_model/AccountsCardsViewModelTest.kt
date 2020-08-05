package taxi.kassa.view_model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.android.ext.android.get
import org.mockito.Mock
import taxi.kassa.MyApplication
import taxi.kassa.base.BaseAndroidTest
import taxi.kassa.model.remote.ApiClient
import taxi.kassa.repository.Repository
import taxi.kassa.view.accounts_cards.AccountsCardsViewModel

@RunWith(AndroidJUnit4::class)
class AccountsCardsViewModelTest : BaseAndroidTest() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    val client = ApiClient

    private lateinit var cardsViewModel: AccountsCardsViewModel
    private lateinit var repository: Repository

    @Before
    fun setup() {
        repository = Repository(client.create(getContext()), preferenceManager)
        cardsViewModel = AccountsCardsViewModel(MyApplication().get(), repository)
    }

//    @Test
//    fun request_for_new_account_with_incorrect_values() {
//        cardsViewModel.createAccount(NEW, NEW, NEW, TEST_NUMBER, TEST_NUMBER)
//        cardsViewModel.error.observe(mockLifecycleOwner(), Observer {
//            if (isUserLoggedIn() && isNetworkAvailable()) assertNotNull(it)
//        })
//    }
}