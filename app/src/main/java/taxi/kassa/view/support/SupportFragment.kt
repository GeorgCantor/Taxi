package taxi.kassa.view.support

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_support.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.util.*
import taxi.kassa.util.Constants.ARG_NOTIF_OPEN
import taxi.kassa.util.Constants.MESSAGES_COUNTER
import taxi.kassa.util.Constants.PUSH_COUNTER

class SupportFragment : Fragment() {

    private lateinit var viewModel: SupportViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel { parametersOf() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_support, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.notifications.observe(viewLifecycleOwner) {
            val oldPushesSize = PreferenceManager(requireContext()).getInt(PUSH_COUNTER)
            oldPushesSize?.let { oldSize ->
                if (it.size > oldSize) {
                    notification_count.text = (it.size - oldSize).toString()
                    notification_count.visible()
                    notification_image.invisible()
                } else {
                    notification_count.invisible()
                    notification_image.visible()
                }
            }
        }

        viewModel.incomingMessages.observe(viewLifecycleOwner) {
            val readMessages = PreferenceManager(requireContext()).getInt(MESSAGES_COUNTER)
            val unreadMessages = it.size - (readMessages ?: 0)

            if (unreadMessages > 0) {
                message_counter.visible()
                message_counter.text = getString(R.string.profile_format, unreadMessages.toString())
            } else {
                message_counter.gone()
            }
        }

        with(findNavController(this)) {
            write_to_us_view.setOnClickListener { navigate(R.id.action_supportFragment_to_writeMessageFragment) }

            chat_history_view.setOnClickListener { navigate(R.id.action_supportFragment_to_chatHistoryFragment) }

            notification_image.setOnClickListener {
                navigate(R.id.action_supportFragment_to_notificationsFragment, Bundle().apply {
                    putString(ARG_NOTIF_OPEN, ARG_NOTIF_OPEN)
                })
            }

            notification_count.setOnClickListener {
                navigate(R.id.action_supportFragment_to_notificationsFragment, Bundle().apply {
                    putString(ARG_NOTIF_OPEN, ARG_NOTIF_OPEN)
                })
            }
        }

        back_arrow.setOnClickListener { activity?.onBackPressed() }

        call_button.setOnClickListener { makeCall() }
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
        callIntent.data = Uri.parse("tel:${Constants.SUPPORT_PHONE_NUMBER}")

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
}