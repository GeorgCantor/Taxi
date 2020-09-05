package taxi.kassa.view.auth.auth_sign_up

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_auth_sign_up.*
import kotlinx.android.synthetic.main.fragment_success.*
import kotlinx.android.synthetic.main.keyboard.*
import org.koin.android.ext.android.inject
import taxi.kassa.R
import taxi.kassa.util.*

class AuthSignUpFragment : Fragment(R.layout.fragment_auth_sign_up) {

    private val viewModel by inject<AuthSignUpViewModel>()
    private var phone = ""

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

            isSignUp.observe(viewLifecycleOwner) { success ->
                if (success) {
                    signup_button.gone()
                    success_layout.visible()
                    success_title.text = getString(R.string.register_application_success)
                    success_message.text = getString(R.string.application_success_message)
                    back_arrow_success.setOnClickListener { activity?.onBackPressed() }
                    back_to_main_button.setOnClickListener { activity?.onBackPressed() }
                    back_to_main_button.text = getString(R.string.ok)
                    back_button.gone()
                }
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

        signup_button.setOnClickListener { apply() }

        back_arrow.setOnClickListener { activity?.onBackPressed() }
    }

    private fun apply() {
        if (!login_checkbox.isChecked) {
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