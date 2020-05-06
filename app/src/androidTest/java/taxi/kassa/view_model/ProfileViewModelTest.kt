package taxi.kassa.view_model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import taxi.kassa.MyApplication
import taxi.kassa.model.remote.ApiClient
import taxi.kassa.repository.ApiRepository
import taxi.kassa.util.PreferenceManager
import taxi.kassa.view.profile.ProfileViewModel

@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class ProfileViewModelTest : BaseAndroidTest() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    val client = ApiClient

    private lateinit var viewModel: ProfileViewModel
    private lateinit var repository: ApiRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        val preferenceManager = PreferenceManager(getContext())
        repository = ApiRepository(client.create(getContext()), preferenceManager)
        viewModel = ProfileViewModel(MyApplication(), preferenceManager, repository)
    }

    @Test
    fun get_owner_data() {
    }
}