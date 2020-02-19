package taxi.kassa.view.profile

import android.os.Build
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.util.Constants.PHONE
import taxi.kassa.util.Constants.TOKEN
import taxi.kassa.util.PreferenceManager
import taxi.kassa.util.shortToast
import java.util.*

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    private var logoutPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel { parametersOf() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_profile, container, false)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserInfo()

        viewModel.error.observe(viewLifecycleOwner, Observer {
            activity?.shortToast(it)
        })

        viewModel.responseOwner.observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                name_tv.text = it.fullName
                number_tv.text = getString(
                    R.string.profile_format,
                    PhoneNumberUtils.formatNumber(it.phone, Locale.getDefault().country)
                )
                balance_tv.text = getString(R.string.balance_format, it.balanceTotal)
            }
        })

        balance_view.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_balanceFragment)
        }

        exit_tv.setOnClickListener {
            logout()
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (!logoutPressed) activity?.finish()
    }

    private fun logout() {
        logoutPressed = true
        val prefManager = PreferenceManager(requireActivity())
        prefManager.saveString(PHONE, "")
        prefManager.saveString(TOKEN, "")

        Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_introFragment)
    }
}