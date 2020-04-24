package taxi.kassa.view.withdraws

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.empty_withdraws_screen.*
import kotlinx.android.synthetic.main.fragment_withdraws.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.util.*
import taxi.kassa.util.Constants.NOT_FROM_PUSH
import taxi.kassa.util.Constants.PUSH_COUNTER
import taxi.kassa.util.Constants.WITHDRAW

class WithdrawsFragment : Fragment() {

    private lateinit var viewModel: WithdrawsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel { parametersOf() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_withdraws, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewModel) {
            isProgressVisible.observe(viewLifecycleOwner) { visible ->
                progress_bar.visibility = if (visible) VISIBLE else GONE
            }

            error.observe(viewLifecycleOwner) { context?.shortToast(it) }

            withdraws.observe(viewLifecycleOwner) {
                empty_withdraws.visibility = if (it.count ?: 0 > 0) GONE else VISIBLE

                withdraws_recycler.adapter =
                    WithdrawsAdapter(it.info ?: mutableListOf()) { withdraw ->
                        val bundle = Bundle()
                        bundle.putParcelable(WITHDRAW, withdraw)
                        findNavController(this@WithdrawsFragment).navigate(
                            R.id.action_withdrawsFragment_to_withdrawFragment,
                            bundle
                        )
                    }
            }

            notifications.observe(viewLifecycleOwner) {
                val oldPushesSize = PreferenceManager(requireContext()).getInt(PUSH_COUNTER)
                oldPushesSize?.let { oldSize ->
                    if (it.size > oldSize) {
                        notification_count.text = (it.size - oldSize).toString()
                        notification_count_empty.text = (it.size - oldSize).toString()
                        notification_count.visible()
                        notification_count_empty.visible()
                        notification_image.invisible()
                        notification_image_empty.invisible()
                    } else {
                        notification_count.invisible()
                        notification_count_empty.invisible()
                        notification_image.visible()
                        notification_image_empty.visible()
                    }
                }
            }
        }

        back_arrow.setOnClickListener { activity?.onBackPressed() }

        back_arrow_empty.setOnClickListener { activity?.onBackPressed() }

        back_button.setOnClickListener { activity?.onBackPressed() }

        notification_image.setOnClickListener { goToNotifications() }

        notification_count.setOnClickListener { goToNotifications() }

        notification_image_empty.setOnClickListener { goToNotifications() }

        notification_count_empty.setOnClickListener { goToNotifications() }

        add_account_button.setOnClickListener {
            findNavController(this).navigate(R.id.action_withdrawsFragment_to_accountsFragment)
        }
    }

    private fun goToNotifications() = findNavController(this).navigate(
        R.id.action_withdrawsFragment_to_notificationsFragment,
        Bundle().apply { putString(NOT_FROM_PUSH, NOT_FROM_PUSH) }
    )
}