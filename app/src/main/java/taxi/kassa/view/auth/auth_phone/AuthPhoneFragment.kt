package taxi.kassa.view.auth.auth_phone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_auth_phone.*
import kotlinx.android.synthetic.main.keyboard.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import taxi.kassa.R
import taxi.kassa.util.*
import taxi.kassa.util.Constants.PHONE

class AuthPhoneFragment : Fragment() {

    private lateinit var viewModel: AuthPhoneViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(R.layout.fragment_auth_phone)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewModel) {
            isNetworkAvailable.observe(viewLifecycleOwner) { available ->
                if (!available) context?.longToast(getString(R.string.internet_unavailable))
            }

            isProgressVisible.observe(viewLifecycleOwner) { visible ->
                progress_bar.visibility = if (visible) VISIBLE else GONE
            }

            error.observe(viewLifecycleOwner) { phone_input_view.error = it }

            isLoggedIn.observe(viewLifecycleOwner) { loggedIn ->
                if (loggedIn) Navigation.findNavController(view).navigate(R.id.action_authPhoneFragment_to_authCodeFragment)
            }
        }

        login_checkbox.setOnCheckedChangeListener { _, _ ->
            phone_input_view.error = null
        }

        with(phone_edit_text) {
            setMaskListener(phone_input_view)
            setKeyboard(
                arrayOf(num_0, num_1, num_2, num_3, num_4, num_5, num_6, num_7, num_8, num_9, erase_btn, apply_btn)
            ) { apply() }
        }

        login_button.setOnClickListener { apply() }

        back_arrow.setOnClickListener { activity?.onBackPressed() }
    }

    private fun apply() {
        if (!login_checkbox.isChecked) {
            phone_input_view.error = getString(R.string.accept_conditions_error)
            return
        }

        val phone: String = phone_edit_text.value.replace("[^\\d]", "")
        PreferenceManager(requireContext()).saveString(PHONE, phone)

        viewModel.login(phone)
    }
}