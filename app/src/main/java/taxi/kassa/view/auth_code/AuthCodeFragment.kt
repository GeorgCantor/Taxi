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

class AuthCodeFragment : Fragment() {

    private lateinit var viewModel: AuthCodeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_auth_code, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addChangingRequestFocus()

        val prefManager = PreferenceManager(requireActivity())
        val phone = prefManager.getString(PHONE)

        viewModel = getViewModel { parametersOf() }

        viewModel.error.observe(viewLifecycleOwner, Observer {
            error_tv.text = it
        })

        viewModel.token.observe(viewLifecycleOwner, Observer {
            prefManager.saveString(TOKEN, it)
            accessToken = it
        })

        viewModel.isLoggedIn.observe(viewLifecycleOwner, Observer { loggedIn ->
            if (loggedIn) Navigation.findNavController(view).navigate(R.id.profileFragment)
        })

        login_button.setOnClickListener {
            val code = "${input1.text}${input2.text}${input3.text}${input4.text}"
            viewModel.login(phone ?: "", code)
        }
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
                if (code.isNotEmpty()) second.requestFocus()
            }
        })
    }
}