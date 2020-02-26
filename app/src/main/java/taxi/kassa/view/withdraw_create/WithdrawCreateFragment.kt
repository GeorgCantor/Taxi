package taxi.kassa.view.withdraw_create

import android.os.Bundle
import android.os.Handler
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
import kotlinx.android.synthetic.main.fragment_withdraw_create.*
import kotlinx.android.synthetic.main.item_taxi.view.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.model.Taxi
import taxi.kassa.util.Constants.CITYMOBIL
import taxi.kassa.util.Constants.GETT
import taxi.kassa.util.Constants.TAXI
import taxi.kassa.util.Constants.YANDEX
import taxi.kassa.util.shortToast
import taxi.kassa.util.showOneButtonDialog


class WithdrawCreateFragment : Fragment() {

    private lateinit var viewModel: WithdrawCreateViewModel

    private val taxiType: String by lazy { arguments?.get(TAXI) as String }

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

        val constraintSet = ConstraintSet()

        viewModel.error.observe(viewLifecycleOwner, Observer {
            activity?.shortToast(it)
        })

        viewModel.responseOwner.observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                val taxis = mutableListOf<Taxi>()
                taxis.add(Taxi(R.drawable.ic_yandex, getString(R.string.yandex_title), it.balanceYandex))
                taxis.add(Taxi(R.drawable.ic_gett, getString(R.string.gett_title), it.balanceGett))
                taxis.add(Taxi(R.drawable.ic_citymobil, getString(R.string.citymobil_title), it.balanceCity))

                taxi_recycler.adapter = WithdrawTaxiAdapter(taxis) {
                    val items = mutableListOf(
                        taxi_recycler[0], taxi_recycler[1], taxi_recycler[2]
                    )

                    items.map { view ->
                        if (view != it) {
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

        sum_edit_text.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                constraintSet.clone(parent_layout)
                constraintSet.connect(
                    R.id.sum_input_view,
                    ConstraintSet.BOTTOM,
                    R.id.add_account_button,
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

        back_arrow.setOnClickListener { activity?.onBackPressed() }
    }
}