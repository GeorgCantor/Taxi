package taxi.kassa.view.auth_code

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
            if (loggedIn) Navigation.findNavController(view).navigate(R.id.action_authCodeFragment_to_profileFragment)
        })

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

        val editTexts = listOf(input1, input2, input3, input4)

        val touchListener = View.OnTouchListener { _, _ ->
            true
        }

        editTexts.map {
            it.setOnTouchListener(touchListener)
        }

        num_0.setOnClickListener {
            editTexts.map {
                if (it.isFocused) {
                    it.text.insert(it.selectionStart, getString(R.string.num0))
                    return@setOnClickListener
                }
            }
        }

        num_1.setOnClickListener {
            editTexts.map {
                if (it.isFocused) {
                    it.text.insert(it.selectionStart, getString(R.string.num1))
                    return@setOnClickListener
                }
            }
        }

        num_2.setOnClickListener {
            editTexts.map {
                if (it.isFocused) {
                    it.text.insert(it.selectionStart, getString(R.string.num2))
                    return@setOnClickListener
                }
            }
        }

        num_3.setOnClickListener {
            editTexts.map {
                if (it.isFocused) {
                    it.text.insert(it.selectionStart, getString(R.string.num3))
                    return@setOnClickListener
                }
            }
        }

        num_4.setOnClickListener {
            editTexts.map {
                if (it.isFocused) {
                    it.text.insert(it.selectionStart, getString(R.string.num4))
                    return@setOnClickListener
                }
            }
        }

        num_5.setOnClickListener {
            editTexts.map {
                if (it.isFocused) {
                    it.text.insert(it.selectionStart, getString(R.string.num5))
                    return@setOnClickListener
                }
            }
        }

        num_6.setOnClickListener {
            editTexts.map {
                if (it.isFocused) {
                    it.text.insert(it.selectionStart, getString(R.string.num6))
                    return@setOnClickListener
                }
            }
        }

        num_7.setOnClickListener {
            editTexts.map {
                if (it.isFocused) {
                    it.text.insert(it.selectionStart, getString(R.string.num7))
                    return@setOnClickListener
                }
            }
        }

        num_8.setOnClickListener {
            editTexts.map {
                if (it.isFocused) {
                    it.text.insert(it.selectionStart, getString(R.string.num8))
                    return@setOnClickListener
                }
            }
        }

        num_9.setOnClickListener {
            editTexts.map {
                if (it.isFocused) {
                    it.text.insert(it.selectionStart, getString(R.string.num9))
                    return@setOnClickListener
                }
            }
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
            }

            override fun afterTextChanged(code: Editable) {
                inputCounter = 0
                if (code.isNotEmpty()) second.requestFocus()
            }
        })
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