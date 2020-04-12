package taxi.kassa.view.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_registration_selection.*
import taxi.kassa.R
import taxi.kassa.util.Constants.CITYMOBIL
import taxi.kassa.util.Constants.CONNECTION
import taxi.kassa.util.Constants.GETT
import taxi.kassa.util.Constants.YANDEX

class RegistrationSelectionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_registration_selection, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = Bundle()

        with(findNavController(this)) {
            yandex_background.setOnClickListener {
                bundle.putString(CONNECTION, YANDEX)
                navigate(R.id.action_registrationSelectionFragment_to_connectionFragment, bundle)
            }

            gett_background.setOnClickListener {
                bundle.putString(CONNECTION, GETT)
                navigate(R.id.action_registrationSelectionFragment_to_connectionFragment, bundle)
            }

            city_background.setOnClickListener {
                bundle.putString(CONNECTION, CITYMOBIL)
                navigate(R.id.action_registrationSelectionFragment_to_connectionFragment, bundle)
            }
        }

        back_arrow.setOnClickListener { activity?.onBackPressed() }
    }
}