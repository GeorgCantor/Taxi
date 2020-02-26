package taxi.kassa.view.orders

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_orders.*
import kotlinx.android.synthetic.main.item_taxi_orders.view.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R

class OrdersFragment : Fragment() {

    private lateinit var viewModel: OrdersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel { parametersOf() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_orders, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTaxis()

        viewModel.taxis.observe(viewLifecycleOwner, Observer {
            taxi_recycler.adapter = OrdersTaxiAdapter(it) { view, _ ->
                val items = mutableListOf(
                    taxi_recycler[0], taxi_recycler[1], taxi_recycler[2]
                )

                items.map { item ->
                    if (item != view) {
                        item.space.background = getDrawable(requireContext(), R.color.colorAccent)
                    } else {
                        item.space.background = getDrawable(requireContext(), R.color.withdraws_yellow)
                    }
                }

            }

            val runnable = Runnable {
                taxi_recycler[0].performClick()
            }
            Handler().postDelayed(runnable, 500)
        })

        back_arrow.setOnClickListener { activity?.onBackPressed() }
    }
}