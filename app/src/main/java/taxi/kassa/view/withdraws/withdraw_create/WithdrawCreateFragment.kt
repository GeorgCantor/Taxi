package taxi.kassa.view.withdraws.withdraw_create

import android.os.Bundle
import android.transition.TransitionManager.beginDelayedTransition
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_withdraw_create.*
import org.koin.android.ext.android.inject
import taxi.kassa.R
import taxi.kassa.util.*
import taxi.kassa.util.Constants.NOT_FROM_PUSH
import taxi.kassa.util.Constants.PUSH_COUNTER
import taxi.kassa.util.Constants.TAXI

class WithdrawCreateFragment : Fragment() {

    private val viewModel by inject<WithdrawCreateViewModel>()

    private val taxiType: String by lazy { arguments?.get(TAXI) as String }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(R.layout.fragment_withdraw_create)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back_arrow.setOnClickListener { findNavController(this).popBackStack() }

        notification_image.setOnClickListener {
            findNavController(this).navigate(
                R.id.action_withdrawCreateFragment_to_notificationsFragment,
                Bundle().apply { putString(NOT_FROM_PUSH, NOT_FROM_PUSH) }
            )
        }

        notification_count.setOnClickListener {
            findNavController(this).navigate(
                R.id.action_withdrawCreateFragment_to_notificationsFragment,
                Bundle().apply { putString(NOT_FROM_PUSH, NOT_FROM_PUSH) }
            )
        }

        daily_background.setOnClickListener {
            findNavController(this).navigate(
                R.id.action_withdrawCreateFragment_to_dailyWithdrawFragment,
                Bundle().apply { putString(TAXI, taxiType) }
            )
        }

        instant_background.setOnClickListener {
            findNavController(this).navigate(
                R.id.action_withdrawCreateFragment_to_instantWithdrawFragment,
                Bundle().apply { putString(TAXI, taxiType) }
            )
        }

        daily_what_is_it.setOnClickListener {
            context?.showOneButtonDialog(
                getString(R.string.daily_withdrawal),
                getString(R.string.daily_withdrawal_dialog_message),
                false
            ) { view, rootLayout ->
                beginDelayedTransition(rootLayout, view.getTransform(daily_what_is_it))
            }
        }

        instant_what_is_it.setOnClickListener {
            context?.showOneButtonDialog(
                getString(R.string.instant_withdrawal),
                getString(R.string.instant_withdrawal_dialog_message),
                false
            ) { view, rootLayout ->
                beginDelayedTransition(rootLayout, view.getTransform(instant_what_is_it))
            }
        }

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
    }
}