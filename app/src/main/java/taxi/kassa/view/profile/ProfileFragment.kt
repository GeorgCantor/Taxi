package taxi.kassa.view.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_profile.*
import taxi.kassa.R
import taxi.kassa.util.Constants.PHONE
import taxi.kassa.util.Constants.TOKEN
import taxi.kassa.util.PreferenceManager

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exit_tv.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        val prefManager = PreferenceManager(requireActivity())
        prefManager.saveString(PHONE, "")
        prefManager.saveString(TOKEN, "")

        val navOption = NavOptions.Builder().setPopUpTo(R.id.introFragment, true).build()
        Navigation.findNavController(requireView()).navigate(R.id.introFragment, null, navOption)
    }
}