package taxi.kassa.view.accounts_cards.accounts

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionManager.beginDelayedTransition
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintSet.BOTTOM
import androidx.constraintlayout.widget.ConstraintSet.TOP
import androidx.core.view.get
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_accounts.*
import kotlinx.android.synthetic.main.fragment_success.*
import kotlinx.android.synthetic.main.keyboard.*
import org.koin.android.ext.android.inject
import taxi.kassa.R
import taxi.kassa.util.*
import taxi.kassa.util.Constants.NOT_FROM_PUSH
import taxi.kassa.util.Constants.PUSH_COUNTER

class AccountsFragment : Fragment(R.layout.fragment_accounts) {

    private val viewModel by inject<AccountsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                back()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)

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
            beginDelayedTransition(parent_layout, it.getTransform(add_account_block))
            add_account_block.visible()
        }

        close_image.setOnClickListener {
            add_account_block.gone()
            beginDelayedTransition(parent_layout, add_account_block.getTransform(add_account))
            accounts_block.visible()
        }

        val fioEditTexts = listOf<EditText>(
            surname_edit_text, name_edit_text, middle_name_edit_text
        )

        fioEditTexts.map {
            it.setOnFocusChangeListener { _, hasFocus ->
                when (hasFocus) {
                    true -> {
                        parent_layout.changeConstraint(
                            R.id.scroll_view,
                            TOP,
                            R.id.parent_layout,
                            TOP,
                            0
                        )
                    }
                    false -> {
                        parent_layout.changeConstraint(
                            R.id.scroll_view,
                            TOP,
                            R.id.accounts_cards_title,
                            BOTTOM,
                            0
                        )

                        it.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            if (it.text?.isNotBlank() == true) R.drawable.ic_check_green else 0,
                            0
                        )
                    }
                }
            }
        }

        account_edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(chars: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(chars: CharSequence?, p1: Int, p2: Int, p3: Int) {
                when (chars?.length ?: 0 < 20) {
                    true -> {
                        account_input_view.error = getString(R.string.account_number_hint)
                        account_edit_text.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            0,
                            0
                        )
                    }
                    false -> {
                        account_input_view.error = null
                        account_edit_text.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.ic_check_green,
                            0
                        )
                    }
                }
            }
        })

        val editTexts = listOf<EditText>(
            surname_edit_text, name_edit_text, account_edit_text, bik_edit_text
        )

        add_account_button.setOnClickListener {
            editTexts.map {
                if (it.isEmpty() || account_edit_text.value.length < 20) {
                    context?.showToast(getString(R.string.fill_all_fields))
                    return@setOnClickListener
                }
            }

            viewModel.createAccount(
                name_edit_text.value,
                surname_edit_text.value,
                middle_name_edit_text.value,
                account_edit_text.value,
                bik_edit_text.value
            )
            keyboard.gone()
        }

        with(viewModel) {
            getAccounts()

            isProgressVisible.observe(viewLifecycleOwner) { progress_bar.setVisibility(it) }

            error.observe(viewLifecycleOwner) { context?.showToast(it) }

            showSuccessScreen.observe(viewLifecycleOwner) { show ->
                if (show) {
                    success_layout.visible()
                    success_title.text = getString(R.string.account_added)
                    success_message.gone()
                    back_arrow_success.setOnClickListener { activity?.onBackPressed() }
                    back_to_main_button.setOnClickListener {
                        findNavController(this@AccountsFragment).navigate(R.id.action_accountsFragment_to_profileFragment)
                    }
                    back_button.setOnClickListener { activity?.onBackPressed() }
                }
            }

            deletionStatus.observe(viewLifecycleOwner) { context?.showToast(it) }

            accounts.observe(viewLifecycleOwner) {
                accounts_recycler.setHasFixedSize(true)
                it?.let {
                    accounts_recycler.adapter = AccountsAdapter(it, true, { _, _ ->
                    }, { account, deleteIcon ->
                        context?.showTwoButtonsDialog(
                            getString(R.string.delete_account),
                            getString(R.string.delete_account_message),
                            getString(R.string.no),
                            getString(R.string.yes),
                            { view, rootLayout ->
                                beginDelayedTransition(rootLayout, view.getTransform(deleteIcon))
                            },
                            { deleteAccount(account.id) }
                        )
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

        500L.runDelayed { accounts_recycler?.let { if (it.isNotEmpty()) it[0].performClick() } }

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

        // to open the keyboard again
        bik_edit_text.apply {
            setOnClickListener {
                clearFocus()
                requestFocus()
            }
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

                if (it.id == R.id.bik_edit_text) {
                    it.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        if (it.text?.isNotBlank() == true) R.drawable.ic_check_green else 0,
                        0
                    )
                }
            }
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

    private fun back() {
        when (keyboard.visibility) {
            VISIBLE -> keyboard.gone()
            GONE -> findNavController(this).popBackStack()
        }
    }
}