package taxi.kassa.view.auth.auth_sign_up

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.android.synthetic.main.fragment_auth_sign_up.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.util.Constants.PHONE_MASK
import taxi.kassa.util.showError

class AuthSignUpFragment : Fragment() {

    private lateinit var viewModel: AuthSignUpViewModel
    private var loginIsReady = false
    private var phone = ""

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

        val touchListener = View.OnTouchListener { _, _ ->
            true
        }

        phone_edit_text.setOnTouchListener(touchListener)

        phone_edit_text.addTextChangedListener(PhoneMaskListener())

        signup_button.setOnClickListener { apply() }

        viewModel.isSignUp.observe(viewLifecycleOwner, Observer { success ->
            if (success) findNavController(this).navigate(R.id.action_authSignUpFragment_to_successRequestFragment)
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
            error_tv.showError(getString(R.string.accept_conditions_error))
            return
        }

        phone = phone_edit_text.text.toString().replace("[^\\d]".toRegex(), "")

        if (phone == "7") {
            error_tv.showError(getString(R.string.input_field_error))
            return
        }

        if (phone.length != 11) {
            error_tv.showError(getString(R.string.wrong_format_error))
            return
        }

        viewModel.signUp(phone)
    }

    inner class PhoneMaskListener : MaskedTextChangedListener(PHONE_MASK, phone_edit_text, object : ValueListener {
        override fun onTextChanged(maskFilled: Boolean, extractedValue: String, formattedValue: String) {
        }
    })
}