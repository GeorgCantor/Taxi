package taxi.kassa.view.withdraws.withdraw_create

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.core.view.get
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_withdraw_create.*
import kotlinx.android.synthetic.main.item_card.view.*
import kotlinx.android.synthetic.main.keyboard.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.util.*
import taxi.kassa.util.Constants.CITYMOBIL
import taxi.kassa.util.Constants.GETT
import taxi.kassa.util.Constants.NOT_FROM_PUSH
import taxi.kassa.util.Constants.PUSH_COUNTER
import taxi.kassa.util.Constants.TAXI
import taxi.kassa.util.Constants.YANDEX

class WithdrawCreateFragment : Fragment() {

    private lateinit var viewModel: WithdrawCreateViewModel

    private val taxiType: String by lazy { arguments?.get(TAXI) as String }
    private var sourceId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel { parametersOf() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(R.layout.fragment_withdraw_create)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                back()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)

        with(viewModel) {
            isProgressVisible.observe(viewLifecycleOwner) { visible ->
                progress_bar.visibility = if (visible) VISIBLE else GONE
            }

            error.observe(viewLifecycleOwner) { context?.shortToast(it) }

            creatingStatus.observe(viewLifecycleOwner) { status ->
                status?.let { context?.shortToast(it) }
            }

            deletionStatus.observe(viewLifecycleOwner) { status ->
                status?.let { context?.shortToast(it) }
            }

            accounts.observe(viewLifecycleOwner) {
                if (it.info?.isNotEmpty() == true) {
                    val account = it.info.firstOrNull()
                    bank_name_tv.text = account?.bankName
                    order_tv.text = getString(R.string.order_format, account?.accountNumber)
                    name_tv.text = account?.driverName
                }
            }

            responseOwner.observe(viewLifecycleOwner) { response ->
                response?.let {
                    when (taxiType) {
                        YANDEX -> {
                            taxi_icon.setImageResource(R.drawable.ic_yandex_mini)
                            taxi_name.text = getString(R.string.yandex_title)
                            balance_tv.text = getString(R.string.withdraw_create_format, response.balanceYandex)
                        }
                        GETT -> {
                            taxi_icon.setImageResource(R.drawable.ic_gett_mini)
                            taxi_name.text = getString(R.string.gett_title)
                            balance_tv.text = getString(R.string.withdraw_create_format, response.balanceGett)
                        }
                        CITYMOBIL -> {
                            taxi_icon.setImageResource(R.drawable.ic_citymobil_mini)
                            taxi_name.text = getString(R.string.citymobil_title)
                            balance_tv.text = getString(R.string.withdraw_create_format, response.balanceCity)
                        }
                    }
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

            cards.observe(viewLifecycleOwner) { cards ->
                cards_recycler.setHasFixedSize(true)
                cards_recycler.adapter = WithdrawCardsAdapter(cards) { card ->
                    card.check_icon.visible()
                    card.green_background.visible()

                    cards_recycler.adapter?.itemCount?.let {
                        if (it > 1) {
                            for (i in 0 until it) {
                                val item = cards_recycler[i]
                                if (item != card) {
                                    item.check_icon.gone()
                                    item.green_background.gone()
                                }
                            }
                        }
                    }
                }
            }
        }

        runDelayed(500) { cards_recycler?.let { if (it.isNotEmpty()) it[0].performClick() } }

        sum_edit_text.showSoftInputOnFocus = false

        sum_edit_text.setOnClickListener { keyboard.visible() }

        sum_edit_text.setOnFocusChangeListener { _, hasFocus ->
            when (hasFocus) {
                true -> {
                    keyboard.visible()
                    runDelayed(100) { scroll_view.scrollTo(0, scroll_view.bottom) }
                }
                false -> keyboard.gone()
            }
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
            val cursorPosition = sum_edit_text.selectionStart
            if (cursorPosition > 0) {
                sum_edit_text.text = sum_edit_text.text?.delete(cursorPosition - 1, cursorPosition)
                sum_edit_text.setSelection(cursorPosition - 1)
            }
        }

        apply_btn.setOnClickListener { sendRequest() }

        minus_image.setOnClickListener {
            account_block.gone()
            minus_image.gone()
            plus_image.visible()
        }

        plus_image.setOnClickListener {
            account_block.visible()
            minus_image.visible()
            plus_image.gone()
        }

        minus_card_image.setOnClickListener {
            cards_recycler.gone()
            add_card_tv.gone()
            minus_card_image.gone()
            plus_card_image.visible()
        }

        plus_card_image.setOnClickListener {
            cards_recycler.visible()
            add_card_tv.visible()
            minus_card_image.visible()
            plus_card_image.gone()
        }

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

        add_card_tv.setOnClickListener {
            findNavController(this).navigate(R.id.action_withdrawCreateFragment_to_accountsFragment)
        }

        send_request_button.setOnClickListener { sendRequest() }

        sum_edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                sum_input_view.error = null
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        notification_image.setOnClickListener {
            findNavController(this).navigate(
                R.id.action_withdrawCreateFragment_to_notificationsFragment,
                Bundle().apply { putString(NOT_FROM_PUSH, NOT_FROM_PUSH) }
            )
        }

        notification_count.setOnClickListener {
            findNavController(this).navigate(
                R.id.action_withdrawCreateFragment_to_notificationsFragment,
                Bundle().apply { putString(NOT_FROM_PUSH, NOT_FROM_PUSH) }
            )
        }

        back_arrow.setOnClickListener { findNavController(this).popBackStack() }
    }

    private fun setNumberClickListener(button: Button, resource: Int) {
        button.setOnClickListener {
            sum_edit_text.text?.insert(sum_edit_text.selectionStart, getString(resource))
        }
    }

    private fun sendRequest() {
        val sum = sum_edit_text.text.toString()
        if (sum.isEmpty()) {
            sum_input_view.error = getString(R.string.input_error)
            return
        }

        viewModel.createWithdraw(sourceId, sum)
    }

    private fun back() {
        when (keyboard.visibility) {
            VISIBLE -> sum_edit_text.clearFocus()
            GONE -> findNavController(this).popBackStack()
        }
    }
}