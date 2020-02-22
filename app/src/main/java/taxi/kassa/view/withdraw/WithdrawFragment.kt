package taxi.kassa.view.withdraw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_withdraw.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.model.responses.Withdraw
import taxi.kassa.util.Constants.WITHDRAWAL
import taxi.kassa.util.shortToast

class WithdrawFragment : Fragment() {

    private lateinit var viewModel: WithdrawViewModel

    private val withdraw: Withdraw by lazy { arguments?.get(WITHDRAWAL) as Withdraw }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel { parametersOf() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_withdraw, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAccounts()

        viewModel.error.observe(viewLifecycleOwner, Observer {
            activity?.shortToast(it)
        })

        viewModel.accounts.observe(viewLifecycleOwner, Observer {
            val account = it.info.first()
            bank_name_tv.text = account?.bankName
            order_tv.text = getString(R.string.order_format, account?.accountNumber)
            name_tv.text = account?.driverName
        })

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