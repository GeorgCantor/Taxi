package taxi.kassa.view.fuel

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.get
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_fuel_replenish.*
import kotlinx.android.synthetic.main.item_taxi.view.*
import kotlinx.android.synthetic.main.keyboard.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.model.Taxi
import taxi.kassa.util.*
import taxi.kassa.util.Constants.NOT_FROM_PUSH
import taxi.kassa.util.Constants.PUSH_COUNTER

class FuelReplenishFragment : Fragment() {

    private lateinit var viewModel: FuelReplenishViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel { parametersOf() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_fuel_replenish, container, false)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
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
                            getString(R.string.yandex_title) -> taxi_recycler.scrollToPosition(0)
                            getString(R.string.citymobil_title) -> taxi_recycler.scrollToPosition(2)
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

                    Handler().postDelayed({
                        taxi_recycler?.let { if (it.isNotEmpty()) taxi_recycler[0].performClick() }
                    }, 500)
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

        enter_amount_edit_text.showSoftInputOnFocus = false

        enter_amount_edit_text.setOnFocusChangeListener { _, hasFocus ->
            when (hasFocus) {
                true -> keyboard.visibility = VISIBLE
                false -> {
                    keyboard.visibility = GONE
                    enter_amount_input_view.error = null
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
            enter_amount_edit_text.setNumberClickListener(it.first, it.second)
        }

        erase_btn.setOnClickListener {
            val cursorPosition = enter_amount_edit_text.selectionStart
            if (cursorPosition > 0) {
                enter_amount_edit_text.text = enter_amount_edit_text.text?.delete(cursorPosition - 1, cursorPosition)
                enter_amount_edit_text.setSelection(cursorPosition - 1)
            }
        }

        apply_btn.setOnClickListener { replenish() }

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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        Handler().postDelayed({requireView().hideKeyboard() }, 100)
    }

    private fun replenish() {
        val amount = enter_amount_edit_text.text.toString()
        if (amount.isEmpty()) {
            enter_amount_input_view.error = getString(R.string.input_error)
            return
        }
    }

    private fun back() {
        when (keyboard.visibility) {
            VISIBLE -> enter_amount_edit_text.clearFocus()
            GONE -> findNavController(this).popBackStack()
        }
    }
}