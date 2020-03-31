package taxi.kassa.view.orders.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_order.*
import kotlinx.android.synthetic.main.fragment_orders.*
import kotlinx.android.synthetic.main.fragment_orders_list.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.model.responses.Order
import taxi.kassa.util.EndlessScrollListener
import taxi.kassa.util.gone
import taxi.kassa.util.shortToast
import taxi.kassa.util.visible
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
    private lateinit var adapter: OrdersAdapter
    private var nextOffset = ""
    private var firstLoad = true

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

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                back()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        viewModel.getOrders("")

        viewModel.progressIsVisible.observe(viewLifecycleOwner, Observer { visible ->
            progress_bar.visibility = if (visible) VISIBLE else GONE
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            activity?.shortToast(it)
        })

        viewModel.orders.observe(viewLifecycleOwner, Observer {
            nextOffset = it.nextOffset

            if (firstLoad) {
                adapter = OrdersAdapter(it.orders?.filter {
                    it.sourceId == arguments?.get(ARG_TAXI).toString()
                } as MutableList<Order>) {
                    openOrderDetails(it)
                }
                orders_recycler.adapter = adapter

                empty_tv.visibility = if (orders_recycler.adapter?.itemCount == 0) VISIBLE else GONE

                firstLoad = false
            } else {
                adapter.updateList(it.orders?.filter {
                    it.sourceId == arguments?.get(ARG_TAXI).toString()
                } as MutableList<Order>)
            }
        })

        val scrollListener = object : EndlessScrollListener() {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                viewModel.getOrders(nextOffset)
            }
        }

        orders_recycler.addOnScrollListener(scrollListener)
    }

    private fun openOrderDetails(order: Order) {
        requireActivity().date_time_tv.text = order.getDateWithTime()
        requireActivity().order_address_from.text = order.addressFrom
        requireActivity().order_address_to.text = order.addressTo
        requireActivity().received_amount.text = order.amountClient
        requireActivity().tariff_amount.text = order.amountDriver
        requireActivity().tip_amount.text = order.amountTip
        requireActivity().commission_amount.text = order.amountFeeAgr
        requireActivity().our_commission_amount.text = order.amountFeeOur
        requireActivity().order_total_amount.text = order.amountTotal.toString()
        requireActivity().total_sum_tv.text = getString(R.string.order_balance_format, order.amountTotal.toString())

        when (order.status) {
            "0" -> {
                requireActivity().order_circle_image.setImageResource(R.drawable.ic_green_circle)
                requireActivity().order_status_tv.text = getString(R.string.complete)
            }
            "-1" -> {
                requireActivity().order_circle_image.setImageResource(R.drawable.ic_red_circle)
                requireActivity().order_status_tv.text = getString(R.string.canceled)
            }
            else -> requireActivity().order_circle_image.setImageResource(R.drawable.ic_green_circle)
        }
        requireActivity().order_layout.visible()

        requireActivity().order_back_arrow.setOnClickListener { hideOrderDetails() }
        requireActivity().back_button.setOnClickListener { hideOrderDetails() }
    }

    private fun hideOrderDetails() = requireActivity().order_layout.gone()

    private fun back() {
        when (requireActivity().order_layout.visibility) {
            VISIBLE -> hideOrderDetails()
            GONE -> findNavController(this).popBackStack()
        }
    }
}