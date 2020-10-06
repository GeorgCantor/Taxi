package taxi.kassa.view.withdraws.withdraw_create.instant

import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionManager.beginDelayedTransition
import android.view.View
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.get
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_instant_withdraw.*
import kotlinx.android.synthetic.main.fragment_success.*
import kotlinx.android.synthetic.main.item_card.view.*
import kotlinx.android.synthetic.main.keyboard.*
import org.koin.android.ext.android.inject
import taxi.kassa.R
import taxi.kassa.util.*
import taxi.kassa.util.Constants.CITYMOBIL
import taxi.kassa.util.Constants.GETT
import taxi.kassa.util.Constants.NOT_FROM_PUSH
import taxi.kassa.util.Constants.PUSH_COUNTER
import taxi.kassa.util.Constants.TAXI
import taxi.kassa.util.Constants.YANDEX
import taxi.kassa.view.accounts_cards.cards.CardsAdapter

class InstantWithdrawFragment : Fragment(R.layout.fragment_instant_withdraw) {

    private val viewModel by inject<InstantWithdrawViewModel>()

    private val taxiType: String by lazy { arguments?.get(TAXI) as String }
    private var sourceId = 1
    private var cardId = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back_arrow.setOnClickListener { findNavController(this).popBackStack() }

        notification_image.setOnClickListener {
            findNavController(this).navigate(
                R.id.action_instantWithdrawFragment_to_notificationsFragment,
                Bundle().apply { putString(NOT_FROM_PUSH, NOT_FROM_PUSH) }
            )
        }

        notification_count.setOnClickListener {
            findNavController(this).navigate(
                R.id.action_instantWithdrawFragment_to_notificationsFragment,
                Bundle().apply { putString(NOT_FROM_PUSH, NOT_FROM_PUSH) }
            )
        }

        call_background.setOnClickListener { activity?.makeCall(this) }

        next_button.setOnClickListener {
            cards_block.gone()
            beginDelayedTransition(parent_layout, progress_bar.getTransform(withdraw_block))
            withdraw_block.visible()
        }

        transfer_button.setOnClickListener { sendRequest() }

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

            isProgressVisible.observe(viewLifecycleOwner) { progress_bar.setVisibility(it) }

            error.observe(viewLifecycleOwner) { context?.longToast(it) }

            showSuccessScreen.observe(viewLifecycleOwner) { show ->
                if (show) {
                    success_layout.visible()
                    success_title.text = getString(R.string.thanks_request_accepted)
                    success_message.gone()
                    back_arrow_success.setOnClickListener { activity?.onBackPressed() }
                    back_to_main_button.setOnClickListener {
                        findNavController(this@InstantWithdrawFragment).navigate(R.id.action_instantWithdrawFragment_to_profileFragment)
                    }
                    back_button.setOnClickListener { activity?.onBackPressed() }
                }
            }

            cards.observe(viewLifecycleOwner) {
                when (it.isNullOrEmpty()) {
                    true -> call_dispatcher_block.visible()
                    false -> {
                        cards_block.visible()
                        cards_recycler.setHasFixedSize(true)
                        cards_recycler.adapter = CardsAdapter(it) { card, view ->
                            cardId = card.id?.toInt() ?: 1

                            val items = mutableListOf<View>()
                            (0 until (cards_recycler.adapter?.itemCount ?: 0)).map {
                                items.add(cards_recycler[it])
                            }

                            items.map {
                                if (it == view) {
                                    it.card_background_outline.visible()
                                    it.card_background.invisible()
                                } else {
                                    it.card_background_outline.invisible()
                                    it.card_background.visible()
                                }
                            }
                        }
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
                        CITYMOBIL -> {
                            sourceId = 2
                            taxi_icon.setImageResource(R.drawable.ic_citymobil_mini)
                            taxi_name.text = getString(R.string.citymobil_title)
                            balance_tv.text = getString(R.string.account_balance_format, it.balanceCity)
                        }
                        GETT -> {
                            sourceId = 3
                            taxi_icon.setImageResource(R.drawable.ic_gett_mini)
                            taxi_name.text = getString(R.string.gett_title)
                            balance_tv.text = getString(R.string.account_balance_format, it.balanceGett)
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

        500L.runDelayed { cards_recycler?.let { if (it.isNotEmpty()) it[0].performClick() } }

        sum_edit_text.setKeyboard(
            arrayOf(num_0, num_1, num_2, num_3, num_4, num_5, num_6, num_7, num_8, num_9, erase_btn, apply_btn)
        ) { sendRequest() }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
            activity?.makeCall(this)
        }
    }

    private fun sendRequest() {
        val sum = sum_edit_text.value
        if (sum.isEmpty()) {
            sum_input_view.setErrorTextColor(ColorStateList.valueOf(getColor(requireContext(), R.color.error_red_color)))
            sum_input_view.error = getString(R.string.enter_sum_error)
            return
        }

        viewModel.createWithdraw(sourceId, sum, cardId)
        sum_edit_text.setText("")
    }
}