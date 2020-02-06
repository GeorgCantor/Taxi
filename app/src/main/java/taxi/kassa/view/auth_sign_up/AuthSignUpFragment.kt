package taxi.kassa.view.auth_sign_up

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_auth_sign_up.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.util.shortToast
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
            tv_error.text = it
        })

        login_checkbox.setOnCheckedChangeListener { _, isChecked ->
            loginIsReady = isChecked
        }

        input_login.addTextChangedListener(object : TextWatcher {
            var length_before = 0
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
                length_before = s.length
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (input_login.length() <= 2) {
                    input_login.setText("+7 ")
                    input_login.setSelection(3)
                }
            }

            override fun afterTextChanged(s: Editable) {
                if (length_before < s.length) {
                    if (s.length == 6) s.append(" ")
                    if (s.length == 10 || s.length == 13) s.append("-")
                    if (s.length > 6) {
                        if (Character.isDigit(s[6])) s.insert(6, "-")
                    }
                    if (s.length > 10) {
                        if (Character.isDigit(s[10])) s.insert(10, "-")
                    }
                }
            }
        })

        btn_signup.setOnClickListener {
            if (!loginIsReady) {
                showError(context, tv_error, getString(R.string.accept_conditions_error), 5000, 0)
                return@setOnClickListener
            }

            val phone = input_login.text.toString().replace("[^\\d]".toRegex(), "")

            if (phone == "7") {
                showError(context, tv_error, getString(R.string.input_field_error), 5000, 0)
                return@setOnClickListener
            }

            if (phone.length != 11) {
                showError(context, tv_error, getString(R.string.wrong_format_error), 5000, 0)
                return@setOnClickListener
            }

            viewModel.signUp(phone)
        }

        viewModel.isSignUp.observe(viewLifecycleOwner, Observer {
            requireActivity().shortToast(if (it == true) "Success" else "Not success")
        })
    }
}