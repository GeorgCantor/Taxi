package taxi.kassa.view.withdraws.withdraw_create

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_withdraw_create.*
import kotlinx.android.synthetic.main.item_taxi.view.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.model.Taxi
import taxi.kassa.util.Constants.CITYMOBIL
import taxi.kassa.util.Constants.GETT
import taxi.kassa.util.Constants.PUSH_COUNTER
import taxi.kassa.util.Constants.TAXI
import taxi.kassa.util.Constants.YANDEX
import taxi.kassa.util.PreferenceManager
import taxi.kassa.util.shortToast
import taxi.kassa.util.showOneButtonDialog
import taxi.kassa.util.showTwoButtonsDialog

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
    ): View? = inflater.inflate(R.layout.fragment_withdraw_create, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserInfo()
        viewModel.getAccounts()

        val constraintSet = ConstraintSet()

        viewModel.error.observe(viewLifecycleOwner, Observer {
            activity?.shortToast(it)
        })

        viewModel.creatingStatus.observe(viewLifecycleOwner, Observer { status ->
            status?.let {
                activity?.shortToast(it)
            }
        })

        viewModel.deletionStatus.observe(viewLifecycleOwner, Observer { status ->
            status?.let {
                activity?.shortToast(it)
            }
        })

        viewModel.accounts.observe(viewLifecycleOwner, Observer {
            if (it.info?.isNotEmpty() == true) {
                val account = it.info.first()
                bank_name_tv.text = account.bankName
                order_tv.text = getString(R.string.order_format, account.accountNumber)
                name_tv.text = account.driverName
            }
        })

        viewModel.responseOwner.observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                val taxis = mutableListOf<Taxi>()
                taxis.add(Taxi(R.drawable.ic_yandex, getString(R.string.yandex_title), it.balanceYandex))
                taxis.add(Taxi(R.drawable.ic_gett, getString(R.string.gett_title), it.balanceGett))
                taxis.add(Taxi(R.drawable.ic_citymobil, getString(R.string.citymobil_title), it.balanceCity))

                taxi_recycler.adapter = WithdrawTaxiAdapter(taxis) { itemView ->
                    val items = mutableListOf(
                        taxi_recycler[0], taxi_recycler[1], taxi_recycler[2]
                    )

                    when (itemView.taxi_name.text) {
                        getString(R.string.yandex_title) -> {
                            sourceId = 1
                            taxi_recycler.scrollToPosition(0)
                        }
                        getString(R.string.citymobil_title) -> {
                            sourceId = 2
                            taxi_recycler.scrollToPosition(2)
                        }
                        getString(R.string.gett_title) -> {
                            sourceId = 3
                        }
                    }

                    items.map { view ->
                        if (view != itemView) {
                            view.background = getDrawable(requireContext(), android.R.color.transparent)
                            view.check_image.visibility = INVISIBLE
                        } else {
                            view.background = getDrawable(requireContext(), R.drawable.bg_outline_green)
                            view.check_image.visibility = VISIBLE
                        }
                    }
                }

                val runnable = Runnable {
                    when (taxiType) {
                        YANDEX -> taxi_recycler[0].performClick()
                        GETT -> taxi_recycler[1].performClick()
                        CITYMOBIL -> {
                            taxi_recycler.scrollToPosition(2)
                            taxi_recycler[2].performClick()
                        }
                        else -> taxi_recycler[0].performClick()
                    }
                }
                Handler().postDelayed(runnable, 500)
            }
        })

        viewModel.notifications.observe(viewLifecycleOwner, Observer {
            val oldPushesSize = PreferenceManager(requireContext()).getInt(PUSH_COUNTER)
            oldPushesSize?.let { oldSize ->
                if (it.size > oldSize) {
                    notification_count.text = (it.size - oldSize).toString()
                    notification_count.visibility = VISIBLE
                    notification_image.visibility = INVISIBLE
                } else {
                    notification_count.visibility = INVISIBLE
                    notification_image.visibility = VISIBLE
                }
            }
        })

        sum_edit_text.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                constraintSet.clone(parent_layout)
                constraintSet.connect(
                    R.id.sum_input_view,
                    ConstraintSet.BOTTOM,
                    R.id.send_request_button,
                    ConstraintSet.TOP
                )
                constraintSet.applyTo(parent_layout)
            }
        }

        sum_edit_text.setOnEditorActionListener { _, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                constraintSet.clear(R.id.sum_input_view, ConstraintSet.BOTTOM)
                constraintSet.applyTo(parent_layout)
            }
            false
        }

        minus_image.setOnClickListener {
            account_block.visibility = GONE
            minus_image.visibility = GONE
            plus_image.visibility = VISIBLE
        }

        plus_image.setOnClickListener {
            account_block.visibility = VISIBLE
            minus_image.visibility = VISIBLE
            plus_image.visibility = GONE
        }

        minus_card_image.setOnClickListener {
            add_card_block.visibility = GONE
            minus_card_image.visibility = GONE
            plus_card_image.visibility = VISIBLE
        }

        plus_card_image.setOnClickListener {
            add_card_block.visibility = VISIBLE
            minus_card_image.visibility = VISIBLE
            plus_card_image.visibility = GONE
        }

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

        card_background.setOnClickListener {
            findNavController(this).navigate(R.id.action_withdrawCreateFragment_to_accountsFragment)
        }

        send_request_button.setOnClickListener {
            val sum = sum_edit_text.text.toString()
            if (sum.isEmpty()) {
                sum_input_view.error = getString(R.string.input_error)
                return@setOnClickListener
            }

            viewModel.createWithdraw(sourceId, sum)
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

        notification_image.setOnClickListener {
            findNavController(this).navigate(R.id.action_withdrawCreateFragment_to_notificationsFragment)
        }

        notification_count.setOnClickListener {
            findNavController(this).navigate(R.id.action_withdrawCreateFragment_to_notificationsFragment)
        }

        back_arrow.setOnClickListener { activity?.onBackPressed() }
    }
}