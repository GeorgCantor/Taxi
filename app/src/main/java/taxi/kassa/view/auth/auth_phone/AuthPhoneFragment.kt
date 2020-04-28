package taxi.kassa.view.auth.auth_phone

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.android.synthetic.main.fragment_auth_phone.*
import kotlinx.android.synthetic.main.keyboard.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.util.Constants.PHONE
import taxi.kassa.util.Constants.PHONE_MASK
import taxi.kassa.util.PreferenceManager
import taxi.kassa.util.longToast
import taxi.kassa.util.observe
import taxi.kassa.util.showError

class AuthPhoneFragment : Fragment() {

    private lateinit var viewModel: AuthPhoneViewModel
    private var loginIsReady = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_auth_phone, container, false)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = getViewModel { parametersOf() }

        phone_edit_text.showSoftInputOnFocus = false

        loginIsReady = true

        with(viewModel) {
            isNetworkAvailable.observe(viewLifecycleOwner) { available ->
                if (!available) context?.longToast(getString(R.string.internet_unavailable))
            }

            isProgressVisible.observe(viewLifecycleOwner) { visible ->
                progress_bar.visibility = if (visible) VISIBLE else GONE
            }

            error.observe(viewLifecycleOwner) { error_tv.showError(it) }

            isLoggedIn.observe(viewLifecycleOwner) { loggedIn ->
                if (loggedIn) Navigation.findNavController(view).navigate(R.id.action_authPhoneFragment_to_authCodeFragment)
            }
        }

        login_checkbox.setOnCheckedChangeListener { _, isChecked ->
            loginIsReady = isChecked
        }

        phone_edit_text.addTextChangedListener(PhoneMaskListener())

        login_button.setOnClickListener { apply() }

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

        back_arrow.setOnClickListener { activity?.onBackPressed() }
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

        val phone: String = phone_edit_text.text.toString().replace("[^\\d]", "")
        PreferenceManager(requireContext()).saveString(PHONE, phone)

        viewModel.login(phone)
    }

    inner class PhoneMaskListener : MaskedTextChangedListener(PHONE_MASK, phone_edit_text, object : ValueListener {
        override fun onTextChanged(maskFilled: Boolean, extractedValue: String, formattedValue: String) {
            error_tv.text = ""
        }
    })
}