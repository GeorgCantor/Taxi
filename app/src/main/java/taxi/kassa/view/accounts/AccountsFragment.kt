package taxi.kassa.view.accounts

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_accounts.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.util.*
import taxi.kassa.util.Constants.PUSH_COUNTER

class AccountsFragment : Fragment() {

    companion object {
        private const val MASTERCARD = "Mastercard"
        private const val VISA = "Visa"
    }

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

        viewModel.progressIsVisible.observe(viewLifecycleOwner, Observer { visible ->
            progress_bar.visibility = if (visible) VISIBLE else GONE
        })

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
                account_block.visible()
                no_account_block.invisible()
                val account = it.info.first()
                bank_name_tv.text = account.bankName
                order_tv.text = getString(R.string.order_format, account.accountNumber)
                name_tv.text = account.driverName
            } else {
                account_block.invisible()
                no_account_block.visible()
            }
        })

        viewModel.notifications.observe(viewLifecycleOwner, Observer {
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
        })

        viewModel.cards.observe(viewLifecycleOwner, Observer {
            cards_recycler.setHasFixedSize(true)
            cards_recycler.adapter = AccountsCardsAdapter(it)
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

        notification_image.setOnClickListener {
            findNavController(this).navigate(R.id.action_accountsFragment_to_notificationsFragment)
        }

        notification_count.setOnClickListener {
            findNavController(this).navigate(R.id.action_accountsFragment_to_notificationsFragment)
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

        val constraintSet = ConstraintSet()

        add_account_image.setOnClickListener {
            no_account_block.invisible()
            account_block.invisible()
            new_account_block.visible()

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
            no_account_block.visible()
            account_block.invisible()
            new_account_block.invisible()

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
            no_card_block.invisible()
            new_card_block.visible()
        }

        card_close_image.setOnClickListener {
            no_card_block.visible()
            new_card_block.invisible()
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

        card_edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                when (editable?.length) {
                    4, 9, 14 -> editable.append(" ")
                }

                when (getCardType(editable?.toString()?.replace(" ", "") ?: "")) {
                    VISA -> {
                        card_edit_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visa, 0)
                    }
                    MASTERCARD -> {
                        card_edit_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mastercard, 0)
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireView().hideKeyboard()
    }

    private fun getCardType(number: String): String {
        val visa = Regex("^4[0-9]{12}(?:[0-9]{3})?$")
        val mastercard = Regex("^5[1-5][0-9]{14}$")

        return when {
            visa.matches(number) -> VISA
            mastercard.matches(number) -> MASTERCARD
            else -> "Unknown"
        }
    }
}