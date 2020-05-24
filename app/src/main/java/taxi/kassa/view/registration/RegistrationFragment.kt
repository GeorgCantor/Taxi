package taxi.kassa.view.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_registration.*
import taxi.kassa.R
import taxi.kassa.util.inflate
import taxi.kassa.util.showOneButtonDialog

class RegistrationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(R.layout.fragment_registration)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back_arrow.setOnClickListener { activity?.onBackPressed() }

        with(findNavController(this)) {
            set_phone_number_button.setOnClickListener {
                navigate(R.id.action_registrationFragment_to_authSignUpFragment)
            }

            download_documents_button.setOnClickListener {
                navigate(R.id.action_registrationFragment_to_registrationSelectionFragment)
            }
        }

        view_terms.setOnClickListener {
            context?.showOneButtonDialog(
                getString(R.string.connection_terms),
                getString(R.string.terms_description),
                true
            )
        }
    }
}