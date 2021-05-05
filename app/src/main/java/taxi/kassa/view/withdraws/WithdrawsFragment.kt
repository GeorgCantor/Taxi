package taxi.kassa.view.withdraws

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.empty_withdraws_screen.*
import kotlinx.android.synthetic.main.fragment_withdraws.*
import org.koin.android.ext.android.inject
import taxi.kassa.R
import taxi.kassa.util.Constants.NOT_FROM_PUSH
import taxi.kassa.util.Constants.WITHDRAW
import taxi.kassa.util.checkSizes
import taxi.kassa.util.showToast

class WithdrawsFragment : Fragment(R.layout.fragment_withdraws) {

    private val viewModel by inject<WithdrawsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewModel) {
            getWithdrawsData()

            isProgressVisible.observe(viewLifecycleOwner) { progress_bar.isVisible = it }

            error.observe(viewLifecycleOwner) {
                context?.showToast(it)
                refresh_layout.isRefreshing = false
            }

            withdraws.observe(viewLifecycleOwner) {
                empty_withdraws.isVisible = it.count == 0

                withdraws_recycler.adapter =
                    WithdrawsAdapter(it.info ?: mutableListOf()) { withdraw ->
                        findNavController(this@WithdrawsFragment).navigate(
                            R.id.action_withdrawsFragment_to_withdrawFragment,
                            Bundle().apply { putParcelable(WITHDRAW, withdraw) }
                        )
                    }
                refresh_layout.isRefreshing = false
            }

            notifications.observe(viewLifecycleOwner) {
                context?.checkSizes(it, notification_count, notification_image)
                context?.checkSizes(it, notification_count_empty, notification_image_empty)
            }

            refresh_layout.setOnRefreshListener { getWithdrawsData() }
        }

        back_arrow.setOnClickListener { activity?.onBackPressed() }

        back_arrow_empty.setOnClickListener { activity?.onBackPressed() }

        back_button.setOnClickListener { activity?.onBackPressed() }

        notification_image.setOnClickListener { goToNotifications() }

        notification_count.setOnClickListener { goToNotifications() }

        notification_image_empty.setOnClickListener { goToNotifications() }

        notification_count_empty.setOnClickListener { goToNotifications() }

        add_account_button.setOnClickListener {
            findNavController(this).navigate(R.id.action_withdrawsFragment_to_accountsCardsFragment)
        }
    }

    private fun goToNotifications() = findNavController(this).navigate(
        R.id.action_withdrawsFragment_to_notificationsFragment,
        Bundle().apply { putString(NOT_FROM_PUSH, NOT_FROM_PUSH) }
    )
}