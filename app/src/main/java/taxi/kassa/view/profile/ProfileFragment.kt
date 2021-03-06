package taxi.kassa.view.profile

import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.telephony.PhoneNumberUtils.formatNumber
import android.transition.TransitionManager.beginDelayedTransition
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.android.ext.android.inject
import taxi.kassa.R
import taxi.kassa.util.*
import taxi.kassa.util.Constants.MESSAGES_COUNTER
import taxi.kassa.util.Constants.NOT_FROM_PUSH
import java.util.*

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel by inject<ProfileViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        name_tv.oneClick()

        with(viewModel) {
            getUserData()

            isProgressVisible.observe(viewLifecycleOwner) { progress_bar.isVisible = it }

            error.observe(viewLifecycleOwner) {
                context?.showToast(it)
                refresh_layout.isRefreshing = false
            }

            responseOwner.observe(viewLifecycleOwner) { response ->
                response?.let {
                    name_tv.text = it.fullName
                    number_tv.text = getString(
                        R.string.profile_format,
                        formatNumber(it.phone, Locale.getDefault().country)
                    ).replaceFirst(" ", "(").replace(" ", ")")

                    balance_tv.setFormattedText(R.string.balance_format, it.balanceTotal.toDouble())
                }
                refresh_layout.isRefreshing = false
            }

            notifications.observe(viewLifecycleOwner) {
                context?.checkSizes(it, notification_count, notification_image)
            }

            incomingMessages.observe(viewLifecycleOwner) {
                val readMessages = PreferenceManager(requireContext()).getInt(MESSAGES_COUNTER)
                val unreadMessages = it.size - (readMessages ?: 0)

                if (unreadMessages > 0) {
                    message_counter.visible()
                    message_counter.text = getString(R.string.profile_format, unreadMessages.toString())
                } else {
                    message_counter.gone()
                }
            }

            refresh_layout.setOnRefreshListener { getUserData() }
        }

        with(findNavController(this)) {
            balance_view.setOnClickListener {
                navigate(R.id.action_profileFragment_to_balanceFragment)
            }

            orders_view.setOnClickListener {
                navigate(R.id.action_profileFragment_to_ordersFragment)
            }

            withdrawal_applications_view.setOnClickListener {
                navigate(R.id.action_profileFragment_to_withdrawsFragment)
            }

            accounts_and_cards_view.setOnClickListener {
                navigate(R.id.action_profileFragment_to_accountsCardsFragment)
            }

            support_service_view.setOnClickListener {
                navigate(R.id.action_profileFragment_to_supportFragment)
            }

            notification_image.setOnClickListener {
                navigate(R.id.action_profileFragment_to_notificationsFragment, Bundle().apply {
                    putString(NOT_FROM_PUSH, NOT_FROM_PUSH)
                })
            }

            notification_count.setOnClickListener {
                navigate(R.id.action_profileFragment_to_notificationsFragment, Bundle().apply {
                    putString(NOT_FROM_PUSH, NOT_FROM_PUSH)
                })
            }
        }

        phone_image.setOnClickListener {
            val dialogView = requireContext().showTwoButtonsDialog(
                getString(R.string.support_service),
                getString(R.string.support_service_message),
                getString(R.string.cancel),
                getString(R.string.call),
                { view, rootLayout ->
                    beginDelayedTransition(rootLayout, view.getTransform(phone_image))
                },
                { requireActivity().makeCall(this) }
            )
            beginDelayedTransition(parent_layout, it.getTransform(dialogView))
        }

        exit_tv.setOnClickListener {
            val dialogView = requireContext().showTwoButtonsDialog(
                getString(R.string.exit),
                getString(R.string.exit_message),
                getString(R.string.no),
                getString(R.string.yes),
                { view, rootLayout ->
                    beginDelayedTransition(rootLayout, view.getTransform(exit_tv))
                },
                { logout() }
            )
            beginDelayedTransition(parent_layout, it.getTransform(dialogView))
        }
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

    private fun logout() {
        viewModel.removePhoneToken()
        activity?.restart()
    }
}