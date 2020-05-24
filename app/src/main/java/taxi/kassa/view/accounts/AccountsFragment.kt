package taxi.kassa.view.accounts

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.android.synthetic.main.fragment_accounts.*
import kotlinx.android.synthetic.main.keyboard.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.util.*
import taxi.kassa.util.Constants.CARD_MASK
import taxi.kassa.util.Constants.MASTERCARD
import taxi.kassa.util.Constants.NOT_FROM_PUSH
import taxi.kassa.util.Constants.PUSH_COUNTER
import taxi.kassa.util.Constants.VISA

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
    ): View? = container?.inflate(R.layout.fragment_accounts)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                back()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)

        with(viewModel) {
            getAccounts()

            isProgressVisible.observe(viewLifecycleOwner) { visible ->
                progress_bar.visibility = if (visible) VISIBLE else GONE
            }

            error.observe(viewLifecycleOwner) { context?.longToast(it) }

            creatingStatus.observe(viewLifecycleOwner) { status ->
                status?.let {
                    context?.shortToast(it)
                    viewModel.getAccounts()
                }
            }

            deletionStatus.observe(viewLifecycleOwner) { status ->
                status?.let {
                    context?.shortToast(it)
                    viewModel.getAccounts()
                }
            }

            accounts.observe(viewLifecycleOwner) {
                if (it.info?.isNotEmpty() == true) {
                    account_block.visible()
                    no_account_block.invisible()
                    val account = it.info.firstOrNull()
                    bank_name_tv.text = account?.bankName
                    order_tv.text = getString(R.string.order_format, account?.accountNumber)
                    name_tv.text = account?.driverName
                } else {
                    account_block.invisible()
                    no_account_block.visible()
                }
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

            cards.observe(viewLifecycleOwner) {
                cards_recycler.setHasFixedSize(true)
                cards_recycler.adapter = AccountsCardsAdapter(it)
            }
        }

        val numberInputs = listOf<TextInputEditText>(
            account_edit_text,
            bik_edit_text,
            card_edit_text
        )

        numberInputs.map {
            it.showSoftInputOnFocus = false

            it.setOnFocusChangeListener { _, hasFocus ->
                when (hasFocus) {
                    true -> {
                        requireView().hideKeyboard()
                        keyboard.visible()
                    }
                    false -> {
                        keyboard.gone()
                    }
                }

                it.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    if (it.text?.isNotBlank() == true) R.drawable.ic_check_green else 0,
                    0
                )
            }

            it.setOnClickListener { if (keyboard.visibility == GONE) keyboard.visible() }
        }

        val keyboardPairs = mutableListOf<Pair<Button, Int>>(
            Pair(num_0, R.string.num0),
            Pair(num_1, R.string.num1),
            Pair(num_2, R.string.num2),
            Pair(num_3, R.string.num3),
            Pair(num_4, R.string.num4),
            Pair(num_5, R.string.num5),
            Pair(num_6, R.string.num6),
            Pair(num_7, R.string.num7),
            Pair(num_8, R.string.num8),
            Pair(num_9, R.string.num9)
        )

        keyboardPairs.map {
            setNumberClickListener(it.first, it.second)
        }

        erase_btn.setOnClickListener {
            var focusedInput = account_edit_text
            numberInputs.map {
                if (it.isFocused) focusedInput = it
            }

            val cursorPosition = focusedInput.selectionStart
            if (cursorPosition > 0) {
                focusedInput.text = focusedInput.text?.delete(cursorPosition - 1, cursorPosition)
                focusedInput.setSelection(cursorPosition - 1)
            }
        }

        apply_btn.setOnClickListener {
            var focusedInput = account_edit_text
            numberInputs.map {
                if (it.isFocused) focusedInput = it
            }

            when (focusedInput.id) {
                R.id.card_edit_text, R.id.bik_edit_text-> keyboard.gone()
                R.id.account_edit_text -> bik_edit_text.requestFocus()
            }
            openNewCard()
        }

        val editTexts = listOf<EditText>(
            name_edit_text, surname_edit_text, account_edit_text, bik_edit_text
        )

        add_account_button.setOnClickListener {
            editTexts.map {
                if (it.isEmpty()) {
                    context?.shortToast(getString(R.string.fill_all_fields))
                    return@setOnClickListener
                }
            }

            viewModel.createAccount(
                name_edit_text.text.toString(),
                middle_name_edit_text.text.toString(),
                surname_edit_text.text.toString(),
                account_edit_text.text.toString(),
                bik_edit_text.text.toString()
            )
            close_image.performClick()
        }

        notification_image.setOnClickListener {
            findNavController(this).navigate(
                R.id.action_accountsFragment_to_notificationsFragment,
                Bundle().apply { putString(NOT_FROM_PUSH, NOT_FROM_PUSH) }
            )
        }

        notification_count.setOnClickListener {
            findNavController(this).navigate(
                R.id.action_accountsFragment_to_notificationsFragment,
                Bundle().apply { putString(NOT_FROM_PUSH, NOT_FROM_PUSH) }
            )
        }

        back_arrow.setOnClickListener { findNavController(this).popBackStack() }

        daily_withdrawal_tv.setOnClickListener {
            context?.showOneButtonDialog(
                getString(R.string.daily_withdrawal),
                getString(R.string.daily_withdrawal_dialog_message),
                false
            )
        }

        instant_withdrawal_tv.setOnClickListener {
            context?.showOneButtonDialog(
                getString(R.string.instant_withdrawal),
                getString(R.string.instant_withdrawal_dialog_message),
                false
            )
        }

        add_account_image.setOnClickListener {
            no_account_block.invisible()
            account_block.invisible()
            new_account_block.visible()

            changeConstraint(
                parent_layout,
                R.id.instant_withdrawal_tv,
                ConstraintSet.TOP,
                R.id.new_account_block,
                ConstraintSet.BOTTOM,
                90
            )
        }

        close_image.setOnClickListener {
            no_account_block.visible()
            account_block.invisible()
            new_account_block.invisible()

            changeConstraint(
                parent_layout,
                R.id.instant_withdrawal_tv,
                ConstraintSet.TOP,
                R.id.account_block,
                ConstraintSet.BOTTOM,
                90
            )
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
                when ((editable?.toString()?.replace(" ", "") ?: "").getCardType()) {
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

        card_edit_text.addTextChangedListener(CardMaskListener())

        add_card_button.setOnClickListener { openNewCard() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireView().hideKeyboard()
    }

    private fun setNumberClickListener(button: Button, resource: Int) {
        val editTexts = listOf<TextInputEditText>(
            account_edit_text,
            bik_edit_text,
            card_edit_text
        )

        button.setOnClickListener {
            var focusedInput = account_edit_text
            editTexts.map {
                if (it.isFocused) focusedInput = it
            }

            focusedInput.text?.insert(focusedInput.selectionStart, getString(resource))
        }
    }

    private fun openNewCard() {
        if (card_edit_text.isEmpty()) card_input_view.error = getString(R.string.input_error)
    }

    private fun back() {
        when (keyboard.visibility) {
            VISIBLE -> keyboard.gone()
            GONE -> findNavController(this).popBackStack()
        }
    }

    inner class CardMaskListener : MaskedTextChangedListener(CARD_MASK, card_edit_text, object : ValueListener {
        override fun onTextChanged(maskFilled: Boolean, extractedValue: String, formattedValue: String) {
            card_input_view.error = null
        }
    })
}