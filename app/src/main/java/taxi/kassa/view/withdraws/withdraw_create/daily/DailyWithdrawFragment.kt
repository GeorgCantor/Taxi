package taxi.kassa.view.withdraws.withdraw_create.daily

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.get
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_daily_withdraw.*
import kotlinx.android.synthetic.main.fragment_success.*
import kotlinx.android.synthetic.main.item_account.view.*
import kotlinx.android.synthetic.main.keyboard.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import taxi.kassa.R
import taxi.kassa.util.*
import taxi.kassa.util.Constants.CITYMOBIL
import taxi.kassa.util.Constants.GETT
import taxi.kassa.util.Constants.NOT_FROM_PUSH
import taxi.kassa.util.Constants.PUSH_COUNTER
import taxi.kassa.util.Constants.TAXI
import taxi.kassa.util.Constants.YANDEX
import taxi.kassa.view.accounts_cards.accounts.AccountsAdapter

class DailyWithdrawFragment : Fragment() {

    private lateinit var viewModel: DailyWithdrawViewModel

    private val taxiType: String by lazy { arguments?.get(TAXI) as String }
    private var sourceId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(R.layout.fragment_daily_withdraw)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back_arrow.setOnClickListener { findNavController(this).popBackStack() }

        notification_image.setOnClickListener {
            findNavController(this).navigate(
                R.id.action_dailyWithdrawFragment_to_notificationsFragment,
                Bundle().apply { putString(NOT_FROM_PUSH, NOT_FROM_PUSH) }
            )
        }

        notification_count.setOnClickListener {
            findNavController(this).navigate(
                R.id.action_dailyWithdrawFragment_to_notificationsFragment,
                Bundle().apply { putString(NOT_FROM_PUSH, NOT_FROM_PUSH) }
            )
        }

        add_account_background.setOnClickListener {
            findNavController(this).navigate(R.id.action_dailyWithdrawFragment_to_accountsFragment)
        }

        next_button.setOnClickListener {
            accounts_block.gone()
            withdraw_block.visible()
        }

        send_request_button.setOnClickListener { sendRequest() }

        sum_edit_text.showSoftInputOnFocus = false

        sum_edit_text.setOnFocusChangeListener { _, hasFocus ->
            when (hasFocus) {
                true -> {
                    sum_input_view.error = getString(R.string.min_sum_hint)
                    keyboard.visible()
                }
                false -> keyboard.gone()
            }
        }

        sum_edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                sum_input_view.error = null
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        with(viewModel) {
            getOwnerData()

            isProgressVisible.observe(viewLifecycleOwner) { visible ->
                progress_bar.visibility = if (visible) VISIBLE else GONE
            }

            error.observe(viewLifecycleOwner) { context?.longToast(it) }

            showSuccessScreen.observe(viewLifecycleOwner) { show ->
                if (show) {
                    success_layout.visible()
                    success_title.text = getString(R.string.thanks_request_accepted)
                    success_message.gone()
                    back_arrow_success.setOnClickListener { activity?.onBackPressed() }
                    back_to_main_button.setOnClickListener {
                        findNavController(this@DailyWithdrawFragment).navigate(R.id.action_dailyWithdrawFragment_to_profileFragment)
                    }
                    back_button.setOnClickListener { activity?.onBackPressed() }
                }
            }

            accounts.observe(viewLifecycleOwner) {
                when (it.info.isNullOrEmpty()) {
                    true -> add_account_block.visible()
                    false -> {
                        accounts_block.visible()
                        accounts_recycler.setHasFixedSize(true)
                        accounts_recycler.adapter = AccountsAdapter(it.info, false, { account, view ->
                            setAccountId(account)

                            val items = mutableListOf<View>()
                            (0 until (accounts_recycler.adapter?.itemCount ?: 0)).map {
                                items.add(accounts_recycler[it])
                            }

                            items.map {
                                if (it == view) {
                                    it.account_background_outline.visible()
                                    it.account_background.invisible()
                                } else {
                                    it.account_background_outline.invisible()
                                    it.account_background.visible()
                                }
                            }
                        }, { _, _ ->
                        })
                    }
                }
            }

            responseOwner.observe(viewLifecycleOwner) { response ->
                response?.let {
                    when (taxiType) {
                        YANDEX -> {
                            sourceId = 1
                            taxi_icon.setImageResource(R.drawable.ic_yandex_mini)
                            taxi_name.text = getString(R.string.yandex_title)
                            balance_tv.text = getString(R.string.account_balance_format, it.balanceYandex)
                        }
                        GETT -> {
                            sourceId = 4
                            taxi_icon.setImageResource(R.drawable.ic_gett_mini)
                            taxi_name.text = getString(R.string.gett_title)
                            balance_tv.text = getString(R.string.account_balance_format, it.balanceGett)
                        }
                        CITYMOBIL -> {
                            sourceId = 3
                            taxi_icon.setImageResource(R.drawable.ic_citymobil_mini)
                            taxi_name.text = getString(R.string.citymobil_title)
                            balance_tv.text = getString(R.string.account_balance_format, it.balanceCity)
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
            sum_edit_text.setNumberClickListener(it.first, it.second)
        }

        erase_btn.setOnClickListener {
            val cursorPosition = sum_edit_text.selectionStart
            if (cursorPosition > 0) {
                sum_edit_text.text = sum_edit_text.text?.delete(cursorPosition - 1, cursorPosition)
                sum_edit_text.setSelection(cursorPosition - 1)
            }
        }

        apply_btn.setOnClickListener { sendRequest() }
    }

    private fun sendRequest() {
        val sum = sum_edit_text.value
        if (sum.isEmpty()) {
            sum_input_view.setErrorTextColor(ColorStateList.valueOf(getColor(requireContext(), R.color.error_red_color)))
            sum_input_view.error = getString(R.string.enter_sum_error)
            return
        }

        viewModel.createWithdraw(sourceId, sum)
        sum_edit_text.setText("")
    }
}