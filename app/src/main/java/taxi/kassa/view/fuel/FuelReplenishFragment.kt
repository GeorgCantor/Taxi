package taxi.kassa.view.fuel

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_fuel_replenish.*
import kotlinx.android.synthetic.main.item_taxi.view.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.model.Taxi
import taxi.kassa.util.*
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewModel) {
            isProgressVisible.observe(viewLifecycleOwner, Observer { visible ->
                progress_bar.visibility = if (visible) VISIBLE else GONE
            })

            error.observe(viewLifecycleOwner, Observer {
                activity?.shortToast(it)
            })

            responseOwner.observe(viewLifecycleOwner, Observer { response ->
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

                    Handler().postDelayed({ taxi_recycler?.let { taxi_recycler[0].performClick() } }, 500)
                }
            })

            notifications.observe(viewLifecycleOwner, Observer {
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
            })
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

        replenish_button.setOnClickListener {
            val amount = enter_amount_edit_text.text.toString()
            if (amount.isEmpty()) {
                enter_amount_input_view.error = getString(R.string.input_error)
                return@setOnClickListener
            }
        }

        notification_image.setOnClickListener {
            findNavController(this).navigate(R.id.action_fuelReplenishFragment_to_notificationsFragment)
        }

        notification_count.setOnClickListener {
            findNavController(this).navigate(R.id.action_fuelReplenishFragment_to_notificationsFragment)
        }

        back_arrow.setOnClickListener { activity?.onBackPressed() }
    }
}