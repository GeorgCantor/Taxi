package taxi.kassa.view.orders.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_orders_list.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.model.responses.Order
import taxi.kassa.util.shortToast
import taxi.kassa.view.MainActivity
import taxi.kassa.view.orders.order.OrderFragment
import taxi.kassa.view.orders.adapter.OrdersAdapter

class OrdersListFragment : Fragment() {

    companion object {
        private const val ARG_TAXI = "taxi"

        fun create(taxiId: Int): OrdersListFragment {
            return OrdersListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_TAXI, taxiId)
                }
            }
        }
    }

    private lateinit var viewModel: OrdersListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel { parametersOf() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_orders_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getOrders()

        viewModel.progressIsVisible.observe(viewLifecycleOwner, Observer { visible ->
            progress_bar.visibility = if (visible) VISIBLE else GONE
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            activity?.shortToast(it)
        })

        viewModel.orders.observe(viewLifecycleOwner, Observer {
            orders_recycler.adapter = OrdersAdapter(it.orders?.filter {
                it.sourceId == arguments?.get(ARG_TAXI).toString()
            } as MutableList<Order>) {
                openOrder(it)
            }

            empty_tv.visibility = if (orders_recycler.adapter?.itemCount == 0) VISIBLE else GONE
        })
    }

    private fun openOrder(order: Order) {
        val transaction: FragmentTransaction = (context as MainActivity).supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            R.animator.slide_in_left,
            R.animator.slide_out_right,
            R.animator.slide_in_right,
            R.animator.slide_out_left
        )
        transaction.replace(R.id.navHostFragment, OrderFragment.create(order))
        transaction.commit()
    }
}