package taxi.kassa.view.withdraws.withdraw

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_withdraw.*
import org.koin.android.ext.android.inject
import taxi.kassa.R
import taxi.kassa.model.responses.Withdraw
import taxi.kassa.util.Constants.ALFABANK
import taxi.kassa.util.Constants.BINBANK
import taxi.kassa.util.Constants.DENIED
import taxi.kassa.util.Constants.SBERBANK
import taxi.kassa.util.Constants.TINKOFF
import taxi.kassa.util.Constants.VTB_BANK
import taxi.kassa.util.Constants.WITHDRAW
import taxi.kassa.util.Constants.WITHDRAWN
import taxi.kassa.util.setFormattedText
import taxi.kassa.util.showToast

class WithdrawFragment : Fragment(R.layout.fragment_withdraw) {

    private val viewModel by inject<WithdrawViewModel>()

    private val withdraw: Withdraw by lazy { arguments?.get(WITHDRAW) as Withdraw }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewModel) {
            isProgressVisible.observe(viewLifecycleOwner) { progress_bar.isVisible = it }

            error.observe(viewLifecycleOwner) { context?.showToast(it) }

            accounts.observe(viewLifecycleOwner) { accounts ->
                accounts?.let {
                    if (it.info?.isNotEmpty() == true) {
                        val account = it.info.firstOrNull()
                        bank_name_tv.text = account?.bankName
                        order_tv.text = getString(R.string.order_format, account?.accountNumber)
                        name_tv.text = account?.driverName

                        setBankIcon(account?.bankName ?: "")
                    }
                }
            }
        }

        back_arrow.setOnClickListener { activity?.onBackPressed() }

        when (withdraw.sourceId.toInt()) {
            1 -> {
                taxi_icon.setImageResource(R.drawable.ic_yandex_mini)
                taxi_name.setText(R.string.yandex_title)
            }
            2 -> {
                taxi_icon.setImageResource(R.drawable.ic_gett_mini)
                taxi_name.setText(R.string.gett_title)
            }
            3 -> {
                taxi_icon.setImageResource(R.drawable.ic_citymobil_mini)
                taxi_name.setText(R.string.citymobil_title)
            }
            else -> {
                taxi_icon.setImageResource(R.drawable.transparent)
                taxi_name.text = ""
            }
        }

        circle_image.setImageResource(
            when (withdraw.status) {
                WITHDRAWN -> R.drawable.ic_green_circle
                DENIED -> R.drawable.ic_red_circle
                else -> R.drawable.ic_yellow_circle
            }
        )

        status_tv.text = withdraw.status
        date_time_tv.text = withdraw.getWithdrawalDate()
        withdrawal_amount.setFormattedText(R.string.balance_format, withdraw.amount.toDouble())
        commission_amount.text = getString(R.string.balance_format, withdraw.amountFee)
        total_tv.setFormattedText(R.string.order_balance_format, withdraw.amount.toDouble() - withdraw.amountFee.toDouble())

        back_button.setOnClickListener { activity?.onBackPressed() }
    }

    private fun setBankIcon(bankName: String) {
        bank_icon.setImageResource(
            when {
                bankName.contains(ALFABANK, true) -> R.drawable.ic_alfa
                bankName.contains(BINBANK, true) -> R.drawable.ic_binbank
                bankName.contains(VTB_BANK, true) -> R.drawable.ic_vtb
                bankName.contains(SBERBANK, true) -> R.drawable.ic_sberbank
                bankName.contains(TINKOFF, true) -> R.drawable.ic_tinkoff
                else -> R.drawable.transparent
            }
        )
    }
}