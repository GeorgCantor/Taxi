package taxi.kassa.view.withdraw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_withdraw.*
import taxi.kassa.R
import taxi.kassa.model.responses.Withdraw
import taxi.kassa.util.Constants.WITHDRAWAL

class WithdrawFragment : Fragment() {

    private val withdraw: Withdraw by lazy { arguments?.get(WITHDRAWAL) as Withdraw }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_withdraw, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back_arrow.setOnClickListener { activity?.onBackPressed() }

        circle_image.setImageResource(
            when (withdraw.status) {
                0 -> R.drawable.ic_yellow_circle
                1 -> R.drawable.ic_yellow_circle
                2 -> R.drawable.ic_green_circle
                -1 -> R.drawable.ic_red_circle
                else -> R.drawable.ic_yellow_circle
            }
        )

        status_tv.text = withdraw.getStatus()
        date_time_tv.text = withdraw.getWithdrawalDate()
        withdrawal_amount.text = getString(R.string.balance_format, withdraw.amount)
        commission_amount.text = getString(R.string.balance_format, "13.00")
        total_tv.text = getString(R.string.balance_format, "Итог: ${withdraw.amount}")

        back_button.setOnClickListener { activity?.onBackPressed() }
    }
}