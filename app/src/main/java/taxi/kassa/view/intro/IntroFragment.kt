package taxi.kassa.view.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
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

        btn_login.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_introFragment_to_authPhoneFragment)
        }

        btn_signup.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_introFragment_to_authSignUpFragment)
        }
    }
}