package taxi.kassa.view.accounts_cards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_accounts.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import taxi.kassa.R
import taxi.kassa.util.*
import taxi.kassa.util.Constants.NOT_FROM_PUSH
import taxi.kassa.util.Constants.PUSH_COUNTER

class AccountsCardsFragment : Fragment() {

    private lateinit var viewModel: AccountsCardsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(R.layout.fragment_accounts)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewModel) {
            error.observe(viewLifecycleOwner) { context?.longToast(it) }

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

//            cards.observe(viewLifecycleOwner) {
//                cards_daily_recycler.setHasFixedSize(true)
//                cards_daily_recycler.adapter = AccountsCardsAdapter(it) {
//                    context?.showTwoButtonsDialog(
//                        getString(R.string.delete_card),
//                        getString(R.string.delete_card_message),
//                        getString(R.string.no),
//                        getString(R.string.yes)
//                    ) { viewModel.deleteCard(it.id?.toInt() ?: 0) }
//                }
//                cards_daily_recycler.layoutParams.height = it.size * 250
//
//                cards_recycler.setHasFixedSize(true)
//                cards_recycler.adapter = AccountsCardsAdapter(it) {
//                    context?.showTwoButtonsDialog(
//                        getString(R.string.delete_card),
//                        getString(R.string.delete_card_message),
//                        getString(R.string.no),
//                        getString(R.string.yes)
//                    ) { viewModel.deleteCard(it.id?.toInt() ?: 0) }
//                }
//                cards_recycler.layoutParams.height = it.size * 250
//            }

//            isCardAdded.observe(viewLifecycleOwner) { added ->
//                if (added) {
//                    context?.longToast(getString(R.string.card_added))
//                    findNavController(this@AccountsFragment).navigate(R.id.action_accountsFragment_self)
//                }
//            }
        }

//        val numberInputs = listOf<TextInputEditText>(
//            account_edit_text,
//            bik_edit_text,
//            card_daily_edit_text
//        )
//
//        numberInputs.map {
//            it.showSoftInputOnFocus = false
//
//            it.setOnFocusChangeListener { _, hasFocus ->
//                when (hasFocus) {
//                    true -> {
//                        requireView().hideKeyboard()
//                        keyboard.visible()
//                    }
//                    false -> {
//                        keyboard.gone()
//                    }
//                }
//
//                it.setCompoundDrawablesWithIntrinsicBounds(
//                    0,
//                    0,
//                    if (it.text?.isNotBlank() == true) R.drawable.ic_check_green else 0,
//                    0
//                )
//            }
//
//            it.setOnClickListener { if (keyboard.visibility == GONE) keyboard.visible() }
//        }

//        apply_btn.setOnClickListener {
//            var focusedInput = account_edit_text
//            numberInputs.map {
//                if (it.isFocused) focusedInput = it
//            }
//
//            when (focusedInput.id) {
//                R.id.bik_edit_text-> keyboard.gone()
//                R.id.account_edit_text -> bik_edit_text.requestFocus()
//            }
//        }

//        val editTexts = listOf<EditText>(
//            name_edit_text, surname_edit_text, account_edit_text, bik_edit_text
//        )

//        add_account_button.setOnClickListener {
//            editTexts.map {
//                if (it.isEmpty()) {
//                    context?.shortToast(getString(R.string.fill_all_fields))
//                    return@setOnClickListener
//                }
//            }
//
//            viewModel.createAccount(
//                name_edit_text.value,
//                middle_name_edit_text.value,
//                surname_edit_text.value,
//                account_edit_text.value,
//                bik_edit_text.value
//            )
//            close_image.performClick()
//        }

        notification_image.setOnClickListener {
            findNavController(this).navigate(
                R.id.action_accountsCardsFragment_to_notificationsFragment,
                Bundle().apply { putString(NOT_FROM_PUSH, NOT_FROM_PUSH) }
            )
        }

        notification_count.setOnClickListener {
            findNavController(this).navigate(
                R.id.action_accountsCardsFragment_to_notificationsFragment,
                Bundle().apply { putString(NOT_FROM_PUSH, NOT_FROM_PUSH) }
            )
        }

        back_arrow.setOnClickListener { findNavController(this).popBackStack() }

//        delete_icon.setOnClickListener {
//            context?.showTwoButtonsDialog(
//                getString(R.string.delete_account),
//                getString(R.string.delete_account_message),
//                getString(R.string.no),
//                getString(R.string.yes)
//            ) {
//                viewModel.deleteAccount()
//            }
//        }
    }
}