package taxi.kassa.view.accounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_accounts.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.util.getStringAfterSpace
import taxi.kassa.util.shortToast
import taxi.kassa.util.showOneButtonDialog
import taxi.kassa.util.showTwoButtonsDialog

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

        viewModel.creatingStatus.observe(viewLifecycleOwner, Observer { status ->
            status?.let {
                activity?.shortToast(it)
                viewModel.getAccounts()
            }
        })

        viewModel.deletionStatus.observe(viewLifecycleOwner, Observer { status ->
            status?.let {
                activity?.shortToast(it)
                viewModel.getAccounts()
            }
        })

        viewModel.accounts.observe(viewLifecycleOwner, Observer {
            if (it.info?.isNotEmpty() == true) {
                account_block.visibility = VISIBLE
                no_account_block.visibility = INVISIBLE
                val account = it.info.first()
                bank_name_tv.text = account.bankName
                order_tv.text = getString(R.string.order_format, account.accountNumber)
                name_tv.text = account.driverName
            } else {
                account_block.visibility = INVISIBLE
                no_account_block.visibility = VISIBLE
            }
        })

        val editTexts = listOf<EditText>(
            name_edit_text, surname_edit_text, account_edit_text, bik_edit_text
        )

        add_account_button.setOnClickListener {
            editTexts.map {
                if (it.text.isEmpty()) {
                    activity?.shortToast(getString(R.string.fill_all_fields))
                    return@setOnClickListener
                }
            }

            val firstName = name_edit_text.text.toString().substringBefore(" ")
            val middleName = name_edit_text.text.toString().getStringAfterSpace()

            viewModel.createAccount(
                firstName,
                middleName,
                surname_edit_text.text.toString(),
                account_edit_text.text.toString(),
                bik_edit_text.text.toString()
            )
            close_image.performClick()
        }

        back_arrow.setOnClickListener { activity?.onBackPressed() }

        daily_withdrawal_tv.setOnClickListener {
            context?.showOneButtonDialog(
                getString(R.string.daily_withdrawal),
                getString(R.string.daily_withdrawal_dialog_message)
            )
        }

        instant_withdrawal_tv.setOnClickListener {
            context?.showOneButtonDialog(
                getString(R.string.instant_withdrawal),
                getString(R.string.instant_withdrawal_dialog_message)
            )
        }

        add_account_image.setOnClickListener {
            no_account_block.visibility = INVISIBLE
            account_block.visibility = INVISIBLE
            new_account_block.visibility = VISIBLE

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
            no_account_block.visibility = VISIBLE
            account_block.visibility = INVISIBLE
            new_account_block.visibility = INVISIBLE

            constraintSet.clone(parent_layout)
            constraintSet.connect(
                R.id.instant_withdrawal_tv,
                ConstraintSet.TOP,
                R.id.account_block,
                ConstraintSet.BOTTOM
            )
            constraintSet.applyTo(parent_layout)
        }

        add_card_image.setOnClickListener {
            no_card_block.visibility = INVISIBLE
            new_card_block.visibility = VISIBLE
        }

        card_close_image.setOnClickListener {
            no_card_block.visibility = VISIBLE
            new_card_block.visibility = INVISIBLE
        }

        delete_icon.setOnClickListener {
            context?.showTwoButtonsDialog(
                getString(R.string.delete_account),
                getString(R.string.delete_account_message),
                getString(R.string.no),
                getString(R.string.yes)
            ) {
                viewModel.deleteAccount()
            }
        }
    }
}