package taxi.kassa.view.support

import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_support.*
import org.koin.android.ext.android.inject
import taxi.kassa.R
import taxi.kassa.util.*
import taxi.kassa.util.Constants.MESSAGES_COUNTER
import taxi.kassa.util.Constants.NOT_FROM_PUSH
import taxi.kassa.util.Constants.PUSH_COUNTER

class SupportFragment : Fragment(R.layout.fragment_support) {

    private val viewModel by inject<SupportViewModel>()

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
                    putString(NOT_FROM_PUSH, NOT_FROM_PUSH)
                })
            }

            notification_count.setOnClickListener {
                navigate(R.id.action_supportFragment_to_notificationsFragment, Bundle().apply {
                    putString(NOT_FROM_PUSH, NOT_FROM_PUSH)
                })
            }
        }

        back_arrow.setOnClickListener { activity?.onBackPressed() }

        call_button.setOnClickListener { requireActivity().makeCall(this) }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
            requireActivity().makeCall(this)
        }
    }
}