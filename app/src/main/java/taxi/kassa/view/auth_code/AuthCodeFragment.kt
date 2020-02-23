package taxi.kassa.view.auth_code

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_auth_code.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.util.Constants.PHONE
import taxi.kassa.util.Constants.TOKEN
import taxi.kassa.util.Constants.accessToken
import taxi.kassa.util.PreferenceManager
import taxi.kassa.util.hideKeyboard
import taxi.kassa.util.showError

class AuthCodeFragment : Fragment() {

    private lateinit var viewModel: AuthCodeViewModel
    private var phone = ""
    private var inputCounter = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_auth_code, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addChangingRequestFocus()

        val prefManager = PreferenceManager(requireActivity())
        phone = prefManager.getString(PHONE) ?: ""

        viewModel = getViewModel { parametersOf() }

        viewModel.error.observe(viewLifecycleOwner, Observer {
            showError(context, error_tv, it, 5000, 0)
        })

        viewModel.token.observe(viewLifecycleOwner, Observer {
            prefManager.saveString(TOKEN, it)
            accessToken = it
        })

        viewModel.isLoggedIn.observe(viewLifecycleOwner, Observer { loggedIn ->
            try {
                if (loggedIn) Navigation.findNavController(view).navigate(R.id.action_authCodeFragment_to_profileFragment)
            } catch (e: IllegalArgumentException) {
            }
        })

        login_button.setOnClickListener {
            login()
        }

        input4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                login()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideKeyboard(requireView())
    }

    private fun addChangingRequestFocus() {
        val editTextPairs = mutableListOf<Pair<EditText, EditText>>(
            Pair(input1, input2),
            Pair(input2, input3),
            Pair(input3, input4)
        )

        editTextPairs.map {
            setTextChangedListener(it.first, it.second)
            setDeleteListener(it.second, it.first)
        }
    }

    private fun setTextChangedListener(first: EditText, second: EditText) {
        first.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(code: Editable) {
                if (code.isNotEmpty()) second.requestFocus()
            }
        })
    }

    // move the cursor to the previous input if the current input is empty and user click delete
    private fun setDeleteListener(current: EditText, previous: EditText) {
        current.setOnKeyListener { _, intCode, _ ->
            when (intCode) {
                KeyEvent.KEYCODE_DEL -> {
                    if (current.text.isEmpty()) {
                        inputCounter++
                        if (inputCounter == 2) {
                            previous.requestFocus()
                            inputCounter = 0
                        }
                    }
                }
            }
            return@setOnKeyListener false
        }
    }

    private fun login() {
        val code = "${input1.text}${input2.text}${input3.text}${input4.text}"
        if (code.isEmpty()) {
            showError(context, error_tv, getString(R.string.input_code), 5000, 0)
            return
        }
        viewModel.login(phone, code)
    }
}