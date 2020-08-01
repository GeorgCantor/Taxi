package taxi.kassa.view.withdraws.withdraw_create.daily

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_daily_withdraw.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import taxi.kassa.R
import taxi.kassa.util.*
import taxi.kassa.util.Constants.CITYMOBIL
import taxi.kassa.util.Constants.GETT
import taxi.kassa.util.Constants.NOT_FROM_PUSH
import taxi.kassa.util.Constants.TAXI
import taxi.kassa.util.Constants.YANDEX

class DailyWithdrawFragment : Fragment() {

    private lateinit var viewModel: DailyWithdrawViewModel

    private val taxiType: String by lazy { arguments?.get(TAXI) as String }
    private var sourceId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(R.layout.fragment_daily_withdraw)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back_arrow.setOnClickListener { findNavController(this).popBackStack() }

        notification_image.setOnClickListener {
            findNavController(this).navigate(
                R.id.action_dailyWithdrawFragment_to_notificationsFragment,
                Bundle().apply { putString(NOT_FROM_PUSH, NOT_FROM_PUSH) }
            )
        }

        notification_count.setOnClickListener {
            findNavController(this).navigate(
                R.id.action_dailyWithdrawFragment_to_notificationsFragment,
                Bundle().apply { putString(NOT_FROM_PUSH, NOT_FROM_PUSH) }
            )
        }

        add_account_background.setOnClickListener {
            findNavController(this).navigate(R.id.action_dailyWithdrawFragment_to_accountsFragment)
        }

        next_button.setOnClickListener {
            accounts_block.gone()
            withdraw_block.visible()
        }

        sum_input_view.error = getString(R.string.max_sum_hint)

        with(viewModel) {
            isProgressVisible.observe(viewLifecycleOwner) { visible ->
                progress_bar.visibility = if (visible) VISIBLE else GONE
            }

            error.observe(viewLifecycleOwner) { context?.longToast(it) }

            creatingStatus.observe(viewLifecycleOwner) { status ->
                status?.let { context?.longToast(it) }
            }

            accounts.observe(viewLifecycleOwner) {
                when (it.info.isNullOrEmpty()) {
                    true -> add_account_block.visible()
                    false -> {
                        accounts_block.visible()
                        accounts_recycler.setHasFixedSize(true)
                        accounts_recycler.adapter = AccountsAdapter(it.info) {
                            context?.longToast(it.accountNumber)
                        }
                    }
                }
            }

            responseOwner.observe(viewLifecycleOwner) { response ->
                response?.let {
                    when (taxiType) {
                        YANDEX -> {
                            taxi_icon.setImageResource(R.drawable.ic_yandex_mini)
                            taxi_name.text = getString(R.string.yandex_title)
                            balance_tv.text = getString(R.string.account_balance_format, it.balanceYandex)
                        }
                        GETT -> {
                            taxi_icon.setImageResource(R.drawable.ic_gett_mini)
                            taxi_name.text = getString(R.string.gett_title)
                            balance_tv.text = getString(R.string.account_balance_format, it.balanceGett)
                        }
                        CITYMOBIL -> {
                            taxi_icon.setImageResource(R.drawable.ic_citymobil_mini)
                            taxi_name.text = getString(R.string.citymobil_title)
                            balance_tv.text = getString(R.string.account_balance_format, it.balanceCity)
                        }
                    }
                }
            }

            notifications.observe(viewLifecycleOwner) {
                val oldPushesSize = PreferenceManager(requireContext()).getInt(Constants.PUSH_COUNTER)
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
}