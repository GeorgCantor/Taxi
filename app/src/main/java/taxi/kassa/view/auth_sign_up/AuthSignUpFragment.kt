package taxi.kassa.view.auth_sign_up

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_auth_sign_up.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.util.showError

class AuthSignUpFragment : Fragment() {

    private lateinit var viewModel: AuthSignUpViewModel
    private var loginIsReady = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_auth_sign_up, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = getViewModel { parametersOf() }

        loginIsReady = true

        viewModel.error.observe(viewLifecycleOwner, Observer {
            error_tv.text = it
        })

        login_checkbox.setOnCheckedChangeListener { _, isChecked ->
            loginIsReady = isChecked
        }

        phone_edit_text.addTextChangedListener(object : TextWatcher {
            var lengthBefore = 0

            override fun beforeTextChanged(
                sequence: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
                lengthBefore = sequence.length
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (phone_edit_text.length() <= 2) {
                    phone_edit_text.setSelection(4)
                }
            }

            override fun afterTextChanged(editable: Editable) {
                if (lengthBefore < editable.length) {
                    if (editable.length == 7) editable.append(") ")
                    if (editable.length == 12 || editable.length == 15) editable.append("-")
                }
            }
        })

        signup_button.setOnClickListener {
            if (!loginIsReady) {
                showError(context, error_tv, getString(R.string.accept_conditions_error), 5000, 0)
                return@setOnClickListener
            }

            val phone = phone_edit_text.text.toString().replace("[^\\d]".toRegex(), "")

            if (phone == "7") {
                showError(context, error_tv, getString(R.string.input_field_error), 5000, 0)
                return@setOnClickListener
            }

            if (phone.length != 11) {
                showError(context, error_tv, getString(R.string.wrong_format_error), 5000, 0)
                return@setOnClickListener
            }

            viewModel.signUp(phone)
        }

        viewModel.isSignUp.observe(viewLifecycleOwner, Observer { success ->
            if (success) findNavController(this).navigate(R.id.action_authSignUpFragment_to_successRequestFragment)
        })
    }
}