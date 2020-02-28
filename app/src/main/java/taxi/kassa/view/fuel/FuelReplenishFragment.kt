package taxi.kassa.view.fuel

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_fuel_replenish.*
import kotlinx.android.synthetic.main.item_taxi.view.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.model.Taxi
import taxi.kassa.util.shortToast

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
        viewModel.getUserInfo()

        viewModel.error.observe(viewLifecycleOwner, Observer {
            activity?.shortToast(it)
        })

        viewModel.responseOwner.observe(viewLifecycleOwner, Observer {response->
            response?.let {
                val taxis = mutableListOf<Taxi>()
                taxis.add(Taxi(R.drawable.ic_yandex, getString(R.string.yandex_title), it.balanceYandex))
                taxis.add(Taxi(R.drawable.ic_gett, getString(R.string.gett_title), it.balanceGett))
                taxis.add(Taxi(R.drawable.ic_citymobil, getString(R.string.citymobil_title), it.balanceCity))

                taxi_recycler.adapter = FuelTaxiAdapter(taxis) { itemView ->
                    val items = mutableListOf(
                        taxi_recycler[0], taxi_recycler[1], taxi_recycler[2]
                    )

                    items.map { view ->
                        if (view != itemView) {
                            view.background = AppCompatResources.getDrawable(requireContext(), android.R.color.transparent)
                            view.check_image.visibility = View.INVISIBLE
                        } else {
                            view.background = AppCompatResources.getDrawable(requireContext(), R.drawable.bg_outline_green)
                            view.check_image.visibility = View.VISIBLE
                        }
                    }
                }

                val runnable = Runnable { taxi_recycler[0].performClick() }
                Handler().postDelayed(runnable, 500)
            }
        })

        back_arrow.setOnClickListener { activity?.onBackPressed() }
    }
}