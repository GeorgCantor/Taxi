package taxi.kassa.view.profile

import android.os.Build
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_intro.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.util.Constants.PHONE
import taxi.kassa.util.Constants.TOKEN
import taxi.kassa.util.Constants.accessToken
import taxi.kassa.util.PreferenceManager
import taxi.kassa.util.shortToast
import java.util.*

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var prefManager: PreferenceManager
    private var logoutPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel { parametersOf() }
        prefManager = PreferenceManager(requireActivity())
        accessToken = prefManager.getString(TOKEN) ?: ""
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

        viewModel.progressIsVisible.observe(viewLifecycleOwner, Observer { visible ->
            progress_bar.visibility = if (visible) View.VISIBLE else View.GONE
        })

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
            findNavController(this).navigate(R.id.action_profileFragment_to_balanceFragment)
        }

        withdrawal_applications_view.setOnClickListener {
            findNavController(this).navigate(R.id.action_profileFragment_to_withdrawsFragment)
        }

        accounts_and_cards_view.setOnClickListener {
            findNavController(this).navigate(R.id.action_profileFragment_to_accountsFragment)
        }

        exit_tv.setOnClickListener {
            logout()
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (!logoutPressed) {
            requireActivity().root_layout?.setBackgroundColor(getColor(requireContext(), R.color.login_background))
            requireActivity().intro_layout?.visibility = View.GONE
            activity?.finish()
        }
    }

    private fun logout() {
        logoutPressed = true
        prefManager.saveString(PHONE, "")
        prefManager.saveString(TOKEN, "")

        val navOption = NavOptions.Builder().setPopUpTo(R.id.introFragment, true).build()
        Navigation.findNavController(requireView()).navigate(R.id.introFragment, null, navOption)
    }
}