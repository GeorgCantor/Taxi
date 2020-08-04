package taxi.kassa.view.accounts_cards.accounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.view.get
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_accounts.*
import kotlinx.android.synthetic.main.keyboard.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import taxi.kassa.R
import taxi.kassa.util.*
import taxi.kassa.util.Constants.NOT_FROM_PUSH
import taxi.kassa.util.Constants.PUSH_COUNTER
import taxi.kassa.view.withdraws.withdraw_create.daily.AccountsAdapter

class AccountsFragment : Fragment() {

    private lateinit var viewModel: AccountsViewModel

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

        back_arrow.setOnClickListener { findNavController(this).popBackStack() }

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

        add_account.setOnClickListener {
            accounts_block.gone()
            add_account_block.visible()
        }

        close_image.setOnClickListener {
            add_account_block.gone()
            accounts_block.visible()
        }

        val editTexts = listOf<EditText>(
            surname_edit_text, name_edit_text, account_edit_text, bik_edit_text
        )

        add_account_button.setOnClickListener {
            editTexts.map {
                if (it.isEmpty()) {
                    context?.shortToast(getString(R.string.fill_all_fields))
                    return@setOnClickListener
                }
            }

            viewModel.createAccount(
                name_edit_text.value,
                middle_name_edit_text.value,
                surname_edit_text.value,
                account_edit_text.value,
                bik_edit_text.value
            )
            close_image.performClick()
        }

        with(viewModel) {
            getAccounts()

            isProgressVisible.observe(viewLifecycleOwner) { visible ->
                progress_bar.visibility = if (visible) VISIBLE else GONE
            }

            error.observe(viewLifecycleOwner) { context?.longToast(it) }

            creatingStatus.observe(viewLifecycleOwner) { context?.longToast(it) }

            deletionStatus.observe(viewLifecycleOwner) { context?.longToast(it) }

            accounts.observe(viewLifecycleOwner) {
                accounts_recycler.setHasFixedSize(true)
                it.info?.let {
                    accounts_recycler.adapter = AccountsAdapter(it, true, { _, _ ->
                    }, { account ->
                        context?.showTwoButtonsDialog(
                            getString(R.string.delete_account),
                            getString(R.string.delete_account_message),
                            getString(R.string.no),
                            getString(R.string.yes)
                        ) { deleteAccount(account.id) }
                    })
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
        }

        runDelayed(500) { accounts_recycler?.let { if (it.isNotEmpty()) it[0].performClick() } }

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

        val numberInputs = listOf<TextInputEditText>(account_edit_text, bik_edit_text)

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
                R.id.bik_edit_text-> keyboard.gone()
                R.id.account_edit_text -> bik_edit_text.requestFocus()
            }
        }
    }

    private fun setNumberClickListener(button: Button, resource: Int) {
        val editTexts = listOf<TextInputEditText>(account_edit_text, bik_edit_text)

        button.setOnClickListener {
            var focusedInput = account_edit_text
            editTexts.map {
                if (it.isFocused) focusedInput = it
            }

            focusedInput.text?.insert(focusedInput.selectionStart, getString(resource))
        }
    }
}