package taxi.kassa.view.auth_phone

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_auth_phone.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.util.shortToast

class AuthPhoneFragment : Fragment() {

    private lateinit var viewModel: AuthPhoneViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_auth_phone, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = getViewModel { parametersOf() }

        viewModel.error.observe(viewLifecycleOwner, Observer {
            tv_error.text = it
        })

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

        btn_login.setOnClickListener {
            val phone: String = input_login.text.toString().replace("[^\\d]", "")
            viewModel.login(phone)
        }

        viewModel.isLoggedIn.observe(viewLifecycleOwner, Observer {
            requireActivity().shortToast(if (it == true) "Success" else "Not success")
        })
    }
}