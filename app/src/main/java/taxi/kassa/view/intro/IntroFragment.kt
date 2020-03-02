package taxi.kassa.view.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        login_button.setOnClickListener {
            findNavController(this).navigate(R.id.action_introFragment_to_authPhoneFragment)
        }

        signup_button.setOnClickListener {
            findNavController(this).navigate(R.id.action_introFragment_to_registrationFragment)
        }
    }
}