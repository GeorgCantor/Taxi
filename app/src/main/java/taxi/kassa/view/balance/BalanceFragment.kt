package taxi.kassa.view.balance

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_balance.*
import org.koin.android.ext.android.inject
import taxi.kassa.R
import taxi.kassa.util.*
import taxi.kassa.util.Constants.CITYMOBIL
import taxi.kassa.util.Constants.GETT
import taxi.kassa.util.Constants.NOT_FROM_PUSH
import taxi.kassa.util.Constants.PUSH_COUNTER
import taxi.kassa.util.Constants.TAXI
import taxi.kassa.util.Constants.YANDEX

class BalanceFragment : Fragment(R.layout.fragment_balance) {

    private val viewModel by inject<BalanceViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                back()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)

        with(viewModel) {
            getUserData()

            isProgressVisible.observe(viewLifecycleOwner) { visible ->
                progress_bar.visibility = if (visible) VISIBLE else GONE
            }

            error.observe(viewLifecycleOwner) {
                context?.longToast(it)
                refresh_layout.isRefreshing = false
            }

            responseOwner.observe(viewLifecycleOwner) { response ->
                response?.let {
                    balance_tv.setFormattedText(R.string.balance_format, it.balanceTotal.toDouble())

                    yandex_amount.text = getString(R.string.withdraw_format, it.balanceYandex)
                    citymobil_amount.text = getString(R.string.withdraw_format, it.balanceCity)
                    gett_amount.text = getString(R.string.withdraw_format, it.balanceGett)
                    rosneft_amount.text = getString(R.string.withdraw_format, it.balanceFuel)

                    yandex_amount.setColor(it.balanceYandex, R.color.balance_green, R.color.balance_red)
                    withdraw_yandex_tv.setColor(it.balanceYandex, R.color.gray_intro_text, R.color.colorAccent)
                    citymobil_amount.setColor(it.balanceCity, R.color.balance_green, R.color.balance_red)
                    withdraw_citymobil_tv.setColor(it.balanceCity, R.color.gray_intro_text, R.color.colorAccent)
                    gett_amount.setColor(it.balanceGett, R.color.balance_green, R.color.balance_red)
                    withdraw_gett_tv.setColor(it.balanceGett, R.color.gray_intro_text, R.color.colorAccent)
                    rosneft_amount.setColor(it.balanceFuel, R.color.balance_green, R.color.balance_red)

                    withdraw_yandex_tv.isEnabled = it.balanceYandex.toFloat() > 0.0F
                    withdraw_citymobil_tv.isEnabled = it.balanceCity.toFloat() > 0.0F
                    withdraw_gett_tv.isEnabled = it.balanceGett.toFloat() > 0.0F
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

            refresh_layout.setOnRefreshListener { getUserData() }
        }

        val bundle = Bundle()

        with(findNavController(this)) {
            withdraw_yandex_tv.setOnClickListener {
                bundle.putString(TAXI, YANDEX)
                navigate(R.id.action_balanceFragment_to_withdrawCreateFragment, bundle)
            }

            withdraw_citymobil_tv.setOnClickListener {
                bundle.putString(TAXI, CITYMOBIL)
                navigate(R.id.action_balanceFragment_to_withdrawCreateFragment, bundle)
            }

            withdraw_gett_tv.setOnClickListener {
                bundle.putString(TAXI, GETT)
                navigate(R.id.action_balanceFragment_to_withdrawCreateFragment, bundle)
            }

            replenish_rosneft_tv.setOnClickListener {
                navigate(R.id.action_balanceFragment_to_fuelReplenishFragment)
            }

            notification_image.setOnClickListener {
                navigate(R.id.action_balanceFragment_to_notificationsFragment, Bundle().apply {
                    putString(NOT_FROM_PUSH, NOT_FROM_PUSH)
                })
            }

            notification_count.setOnClickListener {
                navigate(R.id.action_balanceFragment_to_notificationsFragment, Bundle().apply {
                    putString(NOT_FROM_PUSH, NOT_FROM_PUSH)
                })
            }
        }

        back_arrow.setOnClickListener { activity?.onBackPressed() }
    }

    private fun back() {
        findNavController(this).navigate(R.id.action_balanceFragment_to_profileFragment)
    }
}