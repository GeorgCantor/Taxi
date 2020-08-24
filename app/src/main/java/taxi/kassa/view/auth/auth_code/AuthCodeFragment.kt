package taxi.kassa.view.auth.auth_code

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_auth_code.*
import kotlinx.android.synthetic.main.keyboard.*
import org.koin.android.ext.android.inject
import taxi.kassa.R
import taxi.kassa.util.Constants.PHONE
import taxi.kassa.util.Constants.accessToken
import taxi.kassa.util.observe

class AuthCodeFragment : Fragment(R.layout.fragment_auth_code) {

    private val viewModel by inject<AuthCodeViewModel>()
    private lateinit var editTexts: List<EditText>
    private var phone = ""
    private var inputCounter = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addChangingRequestFocus()

        phone = viewModel.getFromPrefs(PHONE) ?: ""

        with(viewModel) {
            isProgressVisible.observe(viewLifecycleOwner) { visible ->
                progress_bar.visibility = if (visible) VISIBLE else GONE
            }

            error.observe(viewLifecycleOwner) { error_tv.text = it }

            token.observe(viewLifecycleOwner) {
                accessToken = it
            }

            isLoggedIn.observe(viewLifecycleOwner) { loggedIn ->
                if (loggedIn) Navigation.findNavController(view).navigate(R.id.action_authCodeFragment_to_profileFragment)
            }
        }

        login_button.setOnClickListener { login() }

        input4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                login()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        editTexts = listOf(input1, input2, input3, input4)

        editTexts.map {
            it.showSoftInputOnFocus = false
        }

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
            editTexts.map {
                if (it.isFocused) {
                    val cursorPosition = it.selectionStart
                    if (cursorPosition > 0) {
                        it.text = it.text.delete(cursorPosition - 1, cursorPosition)
                        it.setSelection(cursorPosition - 1)
                    }
                    inputCounter++
                    if (inputCounter == 2) {
                        when (it) {
                            input4 -> input3.requestFocus()
                            input3 -> input2.requestFocus()
                            input2 -> input1.requestFocus()
                        }
                        inputCounter = 0
                    }
                    return@setOnClickListener
                }
            }
        }

        apply_btn.setOnClickListener { login() }

        back_arrow.setOnClickListener { activity?.onBackPressed() }
    }

    private fun addChangingRequestFocus() {
        val editTextPairs = mutableListOf<Pair<EditText, EditText>>(
            Pair(input1, input2),
            Pair(input2, input3),
            Pair(input3, input4)
        )

        editTextPairs.map {
            setTextChangedListener(it.first, it.second)
        }
    }

    private fun setTextChangedListener(first: EditText, second: EditText) {
        first.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                error_tv.text = ""
            }

            override fun afterTextChanged(code: Editable) {
                inputCounter = 0
                if (code.isNotEmpty()) second.requestFocus()
            }
        })
    }

    private fun setNumberClickListener(button: Button, resource: Int) {
        button.setOnClickListener {
            editTexts.map {
                if (it.isFocused) {
                    it.text.insert(it.selectionStart, getString(resource))
                    return@setOnClickListener
                }
            }
        }
    }

    private fun login() {
        val code = "${input1.text}${input2.text}${input3.text}${input4.text}"
        if (code.isEmpty()) {
            error_tv.text = getString(R.string.input_code)
            return
        }
        viewModel.login(phone, code)
    }
}