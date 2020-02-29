package taxi.kassa.view.auth_phone

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_auth_phone.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.util.Constants.PHONE
import taxi.kassa.util.PreferenceManager
import taxi.kassa.util.hideKeyboard
import taxi.kassa.util.showError

class AuthPhoneFragment : Fragment() {

    private lateinit var viewModel: AuthPhoneViewModel
    private var loginIsReady = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_auth_phone, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = getViewModel { parametersOf() }

        loginIsReady = true

        viewModel.error.observe(viewLifecycleOwner, Observer {
            showError(context, error_tv, it, 5000, 0)
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

        login_button.setOnClickListener {
            if (!loginIsReady) {
                showError(context, error_tv, getString(R.string.accept_conditions_error), 5000, 0)
                return@setOnClickListener
            }

            val phone: String = phone_edit_text.text.toString().replace("[^\\d]", "")
            PreferenceManager(requireActivity()).saveString(PHONE, phone)

            viewModel.login(phone)
        }

        viewModel.isLoggedIn.observe(viewLifecycleOwner, Observer { loggedIn ->
            if (loggedIn) Navigation.findNavController(view).navigate(R.id.action_authPhoneFragment_to_authCodeFragment)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideKeyboard(requireView())
    }
}