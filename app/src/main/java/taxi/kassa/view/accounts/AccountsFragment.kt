package taxi.kassa.view.accounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_accounts.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.util.shortToast
import taxi.kassa.util.showDialog

class AccountsFragment : Fragment() {

    private lateinit var viewModel: AccountsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel { parametersOf() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_accounts, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAccounts()

        val constraintSet = ConstraintSet()

        viewModel.error.observe(viewLifecycleOwner, Observer {
            activity?.shortToast(it)
        })

        viewModel.accounts.observe(viewLifecycleOwner, Observer {
            if (it.info.isNotEmpty()) {
                account_block.visibility = View.VISIBLE
                no_account_block.visibility = View.INVISIBLE
                val account = it.info.first()
                bank_name_tv.text = account?.bankName
                order_tv.text = getString(R.string.order_format, account?.accountNumber)
                name_tv.text = account?.driverName
            } else {
                account_block.visibility = View.INVISIBLE
                no_account_block.visibility = View.VISIBLE
            }
        })

        back_arrow.setOnClickListener { activity?.onBackPressed() }

        daily_withdrawal_tv.setOnClickListener {
            context?.showDialog(
                getString(R.string.daily_withdrawal),
                getString(R.string.daily_withdrawal_dialog_message)
            )
        }

        instant_withdrawal_tv.setOnClickListener {
            context?.showDialog(
                getString(R.string.instant_withdrawal),
                getString(R.string.instant_withdrawal_dialog_message)
            )
        }

        add_account_image.setOnClickListener {
            no_account_block.visibility = View.INVISIBLE
            account_block.visibility = View.INVISIBLE
            new_account_block.visibility = View.VISIBLE

            constraintSet.clone(parent_layout)
            constraintSet.connect(
                R.id.instant_withdrawal_tv,
                ConstraintSet.TOP,
                R.id.new_account_block,
                ConstraintSet.BOTTOM
            )
            constraintSet.applyTo(parent_layout)
        }

        close_image.setOnClickListener {
            no_account_block.visibility = View.VISIBLE
            account_block.visibility = View.INVISIBLE
            new_account_block.visibility = View.INVISIBLE

            constraintSet.clone(parent_layout)
            constraintSet.connect(
                R.id.instant_withdrawal_tv,
                ConstraintSet.TOP,
                R.id.account_block,
                ConstraintSet.BOTTOM
            )
            constraintSet.applyTo(parent_layout)
        }
    }
}