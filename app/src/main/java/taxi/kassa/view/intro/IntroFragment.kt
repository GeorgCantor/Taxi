package taxi.kassa.view.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_intro.*
import taxi.kassa.R
import taxi.kassa.util.inflate
import taxi.kassa.util.setMultiColoredText

class IntroFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(R.layout.fragment_intro)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        intro_registration_message.setMultiColoredText(R.string.intro_registration_message)

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