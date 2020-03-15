package taxi.kassa.view.auth.auth_phone

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
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

        val touchListener = OnTouchListener { _, _ ->
            true
        }

        phone_edit_text.setOnTouchListener(touchListener)

        loginIsReady = true

        viewModel.progressIsVisible.observe(viewLifecycleOwner, Observer { visible ->
            progress_bar.visibility = if (visible) VISIBLE else GONE
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            showError(context, error_tv, it, 5000)
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
                try {
                    if (phone_edit_text.length() <= 2) {
                        phone_edit_text.setSelection(4)
                    }
                } catch (e: IndexOutOfBoundsException) {
                }
            }

            override fun afterTextChanged(editable: Editable) {
                if (lengthBefore < editable.length) {
                    when (editable.length) {
                        1, 2, 3, 4 -> phone_edit_text.setText(getString(R.string.phone_start_symbols))
                        7 -> editable.append(") ")
                        12, 15 -> editable.append("-")
                    }
                    phone_edit_text.setSelection(phone_edit_text.length())
                }
            }
        })

        login_button.setOnClickListener { apply() }

        viewModel.isLoggedIn.observe(viewLifecycleOwner, Observer { loggedIn ->
            if (loggedIn) Navigation.findNavController(view).navigate(R.id.action_authPhoneFragment_to_authCodeFragment)
        })

        num_0.setOnClickListener { phone_edit_text.text.insert(phone_edit_text.selectionStart, getString(R.string.num0)) }

        num_1.setOnClickListener { phone_edit_text.text.insert(phone_edit_text.selectionStart, getString(R.string.num1)) }

        num_2.setOnClickListener { phone_edit_text.text.insert(phone_edit_text.selectionStart, getString(R.string.num2)) }

        num_3.setOnClickListener { phone_edit_text.text.insert(phone_edit_text.selectionStart, getString(R.string.num3)) }

        num_4.setOnClickListener { phone_edit_text.text.insert(phone_edit_text.selectionStart, getString(R.string.num4)) }

        num_5.setOnClickListener { phone_edit_text.text.insert(phone_edit_text.selectionStart, getString(R.string.num5)) }

        num_6.setOnClickListener { phone_edit_text.text.insert(phone_edit_text.selectionStart, getString(R.string.num6)) }

        num_7.setOnClickListener { phone_edit_text.text.insert(phone_edit_text.selectionStart, getString(R.string.num7)) }

        num_8.setOnClickListener { phone_edit_text.text.insert(phone_edit_text.selectionStart, getString(R.string.num8)) }

        num_9.setOnClickListener { phone_edit_text.text.insert(phone_edit_text.selectionStart, getString(R.string.num9)) }

        erase_btn.setOnClickListener {
            val cursorPosition = phone_edit_text.selectionStart
            if (cursorPosition > 0) {
                phone_edit_text.text = phone_edit_text.text.delete(cursorPosition - 1, cursorPosition)
                phone_edit_text.setSelection(cursorPosition - 1)
            }
        }

        apply_btn.setOnClickListener { apply() }
    }

    private fun apply() {
        if (!loginIsReady) {
            showError(context, error_tv, getString(R.string.accept_conditions_error), 5000)
            return
        }

        val phone: String = phone_edit_text.text.toString().replace("[^\\d]", "")
        PreferenceManager(requireActivity()).saveString(PHONE, phone)

        viewModel.login(phone)
    }
}