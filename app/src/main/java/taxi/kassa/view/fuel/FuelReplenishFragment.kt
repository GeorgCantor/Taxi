package taxi.kassa.view.fuel

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.constraintlayout.widget.ConstraintSet.BOTTOM
import androidx.constraintlayout.widget.ConstraintSet.TOP
import androidx.core.view.get
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_fuel_replenish.*
import kotlinx.android.synthetic.main.fragment_success.*
import kotlinx.android.synthetic.main.item_taxi.view.*
import kotlinx.android.synthetic.main.keyboard.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import taxi.kassa.R
import taxi.kassa.model.Taxi
import taxi.kassa.util.*
import taxi.kassa.util.Constants.CITY
import taxi.kassa.util.Constants.GETT
import taxi.kassa.util.Constants.NOT_FROM_PUSH
import taxi.kassa.util.Constants.PUSH_COUNTER
import taxi.kassa.util.Constants.YANDEX

class FuelReplenishFragment : Fragment() {

    private lateinit var viewModel: FuelReplenishViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(R.layout.fragment_fuel_replenish)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                back()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)

        with(viewModel) {
            getOwnerData()

            isNetworkAvailable.observe(viewLifecycleOwner) { available ->
                if (!available) context?.longToast(getString(R.string.internet_unavailable))
            }

            isProgressVisible.observe(viewLifecycleOwner) { visible ->
                progress_bar.visibility = if (visible) VISIBLE else GONE
            }

            error.observe(viewLifecycleOwner) { context?.shortToast(it) }

            showSuccessScreen.observe(viewLifecycleOwner) { show ->
                if (show) {
                    success_layout.visible()
                    success_title.text = getString(R.string.fuel_replenish_success)
                    success_message.gone()
                    back_arrow_success.setOnClickListener { activity?.onBackPressed() }
                    back_to_main_button.setOnClickListener {
                        findNavController(this@FuelReplenishFragment).navigate(R.id.action_fuelReplenishFragment_to_profileFragment)
                    }
                    back_button.setOnClickListener { activity?.onBackPressed() }
                }
            }

            responseOwner.observe(viewLifecycleOwner) { response ->
                response?.let {
                    rosneft_amount.text = getString(R.string.withdraw_format, it.balanceFuel)
                    rosneft_amount.setColor(
                        it.balanceFuel,
                        R.color.balance_green,
                        R.color.balance_red
                    )

                    val taxis = mutableListOf<Taxi>()
                    with(taxis){
                        add(Taxi(R.drawable.ic_yandex, getString(R.string.yandex_title), it.balanceYandex))
                        add(Taxi(R.drawable.ic_gett, getString(R.string.gett_title), it.balanceGett))
                        add(Taxi(R.drawable.ic_citymobil, getString(R.string.citymobil_title), it.balanceCity))
                    }

                    taxi_recycler.adapter = FuelTaxiAdapter(taxis) { itemView ->
                        val items = mutableListOf(
                            taxi_recycler[0], taxi_recycler[1], taxi_recycler[2]
                        )

                        when (itemView.taxi_name.text) {
                            getString(R.string.yandex_title) -> {
                                taxi_recycler.scrollToPosition(0)
                                viewModel.selectedTaxi.value = YANDEX
                            }
                            getString(R.string.citymobil_title) -> {
                                taxi_recycler.scrollToPosition(2)
                                viewModel.selectedTaxi.value = CITY
                            }
                            getString(R.string.gett_title) -> viewModel.selectedTaxi.value = GETT
                        }

                        items.map { view ->
                            when (view == itemView) {
                                true -> {
                                    view.background = getDrawable(requireContext(), R.drawable.bg_outline_green)
                                    view.check_image.visible()
                                }
                                false -> {
                                    view.background = getDrawable(requireContext(), android.R.color.transparent)
                                    view.check_image.invisible()
                                }
                            }
                        }
                    }

                    500L.runDelayed { taxi_recycler?.let { if (it.isNotEmpty()) it[0].performClick() } }
                }
            }

            newFuelBalance.observe(viewLifecycleOwner) {
                it?.let { rosneft_amount.text = it }
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

        enter_amount_edit_text.setKeyboard(
            arrayOf(num_0, num_1, num_2, num_3, num_4, num_5, num_6, num_7, num_8, num_9, erase_btn, apply_btn)
        ) { replenish() }

        enter_amount_edit_text.setOnFocusChangeListener { _, hasFocus ->
            when (hasFocus) {
                true -> {
                    keyboard.visible()
                    100L.runDelayed { scroll_view.scrollTo(0, scroll_view.bottom) }
                    root_layout.changeConstraint(
                        R.id.enter_amount_input_view,
                        BOTTOM,
                        R.id.replenish_button,
                        TOP,
                        40
                    )
                }
                false -> {
                    keyboard.gone()
                    enter_amount_input_view.error = null
                    root_layout.removeConstraint(R.id.enter_amount_input_view, BOTTOM)
                }
            }
        }

        enter_amount_edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                enter_amount_input_view.error = null
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        replenish_button.setOnClickListener { replenish() }

        notification_image.setOnClickListener {
            findNavController(this).navigate(
                R.id.action_fuelReplenishFragment_to_notificationsFragment,
                Bundle().apply { putString(NOT_FROM_PUSH, NOT_FROM_PUSH) }
            )
        }

        notification_count.setOnClickListener {
            findNavController(this).navigate(
                R.id.action_fuelReplenishFragment_to_notificationsFragment,
                Bundle().apply { putString(NOT_FROM_PUSH, NOT_FROM_PUSH) }
            )
        }

        back_arrow.setOnClickListener { findNavController(this).popBackStack() }
    }

    private fun replenish() {
        if (enter_amount_edit_text.isEmpty()) enter_amount_input_view.error = getString(R.string.input_error)
        enter_amount_edit_text.value.apply {
            if (this.isNotBlank()) viewModel.refillFuelBalance(toFloat())
        }
        enter_amount_edit_text.setText("")
        enter_amount_edit_text.clearFocus()
    }

    private fun back() {
        when (keyboard.visibility) {
            VISIBLE -> enter_amount_edit_text.clearFocus()
            GONE -> findNavController(this).navigate(R.id.action_fuelReplenishFragment_to_balanceFragment)
        }
    }
}