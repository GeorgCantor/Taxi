package taxi.kassa.view.auth.auth_sign_up

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_auth_sign_up.*
import kotlinx.android.synthetic.main.keyboard.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import taxi.kassa.R
import taxi.kassa.util.*

class AuthSignUpFragment : Fragment() {

    private lateinit var viewModel: AuthSignUpViewModel
    private var agreementChecked = false
    private var phone = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(R.layout.fragment_auth_sign_up)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        agreementChecked = true

        with(viewModel) {
            isNetworkAvailable.observe(viewLifecycleOwner) { available ->
                if (!available) context?.longToast(getString(R.string.internet_unavailable))
            }

            isProgressVisible.observe(viewLifecycleOwner) { visible ->
                progress_bar.visibility = if (visible) VISIBLE else GONE
            }

            error.observe(viewLifecycleOwner) { phone_input_view.error = it }

            isSignUp.observe(viewLifecycleOwner) { success ->
                if (success) findNavController(this@AuthSignUpFragment).navigate(R.id.action_authSignUpFragment_to_successRequestFragment)
            }
        }

        login_checkbox.setOnCheckedChangeListener { _, isChecked ->
            phone_input_view.error = null
            agreementChecked = isChecked
        }

        with(phone_edit_text) {
            setMaskListener(phone_input_view)
            setKeyboard(
                arrayOf(num_0, num_1, num_2, num_3, num_4, num_5, num_6, num_7, num_8, num_9, erase_btn, apply_btn)
            ) { apply() }
        }

        signup_button.setOnClickListener { apply() }

        back_arrow.setOnClickListener { activity?.onBackPressed() }
    }

    private fun apply() {
        if (!agreementChecked) {
            phone_input_view.error = getString(R.string.accept_conditions_error)
            return
        }

        phone = phone_edit_text.value.replace("[^\\d]".toRegex(), "")

        if (phone == "7") {
            phone_input_view.error = getString(R.string.input_field_error)
            return
        }

        if (phone.length != 11) {
            phone_input_view.error = getString(R.string.wrong_format_error)
            return
        }

        viewModel.signUp(phone)
    }
}