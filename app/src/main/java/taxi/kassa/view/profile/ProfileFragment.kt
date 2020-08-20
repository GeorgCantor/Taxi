package taxi.kassa.view.profile

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.PhoneNumberUtils.formatNumber
import android.transition.TransitionManager.beginDelayedTransition
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet.BOTTOM
import androidx.constraintlayout.widget.ConstraintSet.TOP
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.android.ext.android.inject
import taxi.kassa.R
import taxi.kassa.util.*
import taxi.kassa.util.Constants.MESSAGES_COUNTER
import taxi.kassa.util.Constants.NOT_FROM_PUSH
import taxi.kassa.util.Constants.PHONE
import taxi.kassa.util.Constants.PUSH_COUNTER
import taxi.kassa.util.Constants.TOKEN
import taxi.kassa.view.MainActivity
import java.util.*

class ProfileFragment : Fragment() {

    private val viewModel by inject<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(R.layout.fragment_profile)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLogoutButtonConstraint()

        with(viewModel) {
            getUserData()

            isNetworkAvailable.observe(viewLifecycleOwner) { available ->
                if (!available) context?.longToast(getString(R.string.internet_unavailable))
            }

            isProgressVisible.observe(viewLifecycleOwner) { visible ->
                progress_bar.visibility = if (visible) VISIBLE else GONE
            }

            error.observe(viewLifecycleOwner) {
                context?.shortToast(it)
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
                    550L.runDelayed { phone_image?.visible() }
                },
                { requireActivity().makeCall(this) }
            )
            beginDelayedTransition(parent_layout, it.getTransform(dialogView))
            phone_image.gone()
        }

        exit_tv.setOnClickListener {
            val dialogView = requireContext().showTwoButtonsDialog(
                getString(R.string.exit),
                getString(R.string.exit_message),
                getString(R.string.no),
                getString(R.string.yes),
                { view, rootLayout ->
                    beginDelayedTransition(rootLayout, view.getTransform(exit_tv))
                    550L.runDelayed { exit_tv?.visible() }
                },
                { logout() }
            )
            beginDelayedTransition(parent_layout, it.getTransform(dialogView))
            exit_tv.gone()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkInternet()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requireActivity().makeCall(this)
        }
    }

    private fun logout() {
        viewModel.saveToPrefs(PHONE, "")
        viewModel.saveToPrefs(TOKEN, "")

        activity?.finish()
        startActivity(Intent(requireActivity(), MainActivity::class.java))
    }

    private fun setLogoutButtonConstraint() {
        if (requireContext().getScreenSize() < 5.5) {
            parent_layout.changeConstraint(R.id.exit_tv, TOP, R.id.bottom_line, BOTTOM, 40)
        }
    }
}