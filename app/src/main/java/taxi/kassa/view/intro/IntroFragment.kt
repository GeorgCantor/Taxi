package taxi.kassa.view.intro

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_intro.*
import taxi.kassa.R

class IntroFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_intro, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        intro_registration_message.setText(
            Html.fromHtml(getString(R.string.intro_registration_message)),
            TextView.BufferType.SPANNABLE
        )

        with(findNavController(this)) {
            login_button.setOnClickListener {
                navigate(R.id.action_introFragment_to_authPhoneFragment)
            }

            signup_button.setOnClickListener {
                navigate(R.id.action_introFragment_to_registrationFragment)
            }
        }
    }
}