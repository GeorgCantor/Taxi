package taxi.kassa.view.profile

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.util.Constants.PHONE
import taxi.kassa.util.Constants.SUPPORT_PHONE_NUMBER
import taxi.kassa.util.Constants.TOKEN
import taxi.kassa.util.Constants.accessToken
import taxi.kassa.util.PreferenceManager
import taxi.kassa.util.shortToast
import taxi.kassa.util.showTwoButtonsDialog
import taxi.kassa.view.MainActivity
import java.util.*

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var prefManager: PreferenceManager

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

        orders_view.setOnClickListener {
            findNavController(this).navigate(R.id.action_profileFragment_to_ordersFragment)
        }

        withdrawal_applications_view.setOnClickListener {
            findNavController(this).navigate(R.id.action_profileFragment_to_withdrawsFragment)
        }

        accounts_and_cards_view.setOnClickListener {
            findNavController(this).navigate(R.id.action_profileFragment_to_accountsFragment)
        }

        support_service_view.setOnClickListener {
            findNavController(this).navigate(R.id.action_profileFragment_to_supportFragment)
        }

        phone_image.setOnClickListener {
            context?.showTwoButtonsDialog(
                getString(R.string.support_service),
                getString(R.string.support_service_message),
                getString(R.string.cancel),
                getString(R.string.call)
            ) {
                makeCall()
            }
        }

        exit_tv.setOnClickListener {
            logout()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            makeCall()
        }
    }

    private fun makeCall() {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$SUPPORT_PHONE_NUMBER")

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), 10)
            return
        } else {
            try {
                startActivity(callIntent)
            } catch (ex: ActivityNotFoundException) {
                requireActivity().shortToast(getString(R.string.not_find_call_app))
            }
        }
    }

    private fun logout() {
        prefManager.saveString(PHONE, "")
        prefManager.saveString(TOKEN, "")

        activity?.finish()
        startActivity(Intent(requireActivity(), MainActivity::class.java))
    }
}