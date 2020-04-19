package taxi.kassa.view.auth.auth_sign_up

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.android.synthetic.main.fragment_auth_sign_up.*
import kotlinx.android.synthetic.main.keyboard.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.util.Constants.PHONE_MASK
import taxi.kassa.util.observe
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

        viewModel.error.observe(viewLifecycleOwner) {
            error_tv.text = it
        }

        viewModel.isSignUp.observe(viewLifecycleOwner) { success ->
            if (success) findNavController(this).navigate(R.id.action_authSignUpFragment_to_successRequestFragment)
        }

        login_checkbox.setOnCheckedChangeListener { _, isChecked ->
            loginIsReady = isChecked
        }

        val touchListener = View.OnTouchListener { _, _ ->
            true
        }

        with(phone_edit_text) {
            setOnTouchListener(touchListener)
            addTextChangedListener(PhoneMaskListener())
        }

        signup_button.setOnClickListener { apply() }

        val keyboardPairs = mutableListOf<Pair<Button, Int>>(
            Pair(num_0, R.string.num0),
            Pair(num_1, R.string.num1),
            Pair(num_2, R.string.num2),
            Pair(num_3, R.string.num3),
            Pair(num_4, R.string.num4),
            Pair(num_5, R.string.num5),
            Pair(num_6, R.string.num6),
            Pair(num_7, R.string.num7),
            Pair(num_8, R.string.num8),
            Pair(num_9, R.string.num9)
        )

        keyboardPairs.map {
            setNumberClickListener(it.first, it.second)
        }

        erase_btn.setOnClickListener {
            val cursorPosition = phone_edit_text.selectionStart
            if (cursorPosition > 0) {
                phone_edit_text.text = phone_edit_text.text.delete(cursorPosition - 1, cursorPosition)
                phone_edit_text.setSelection(cursorPosition - 1)
            }
        }

        apply_btn.setOnClickListener { apply() }
    }

    private fun setNumberClickListener(button: Button, resource: Int) {
        button.setOnClickListener {
            phone_edit_text.text.insert(phone_edit_text.selectionStart, getString(resource))
        }
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
            error_tv.text = ""
        }
    })
}