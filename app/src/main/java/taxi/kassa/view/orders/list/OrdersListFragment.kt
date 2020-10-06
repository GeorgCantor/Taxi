package taxi.kassa.view.orders.list

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_order.*
import kotlinx.android.synthetic.main.fragment_orders.*
import kotlinx.android.synthetic.main.fragment_orders_list.*
import org.koin.android.ext.android.inject
import taxi.kassa.R
import taxi.kassa.model.responses.Order
import taxi.kassa.util.*
import taxi.kassa.view.orders.adapter.OrdersAdapter

class OrdersListFragment : Fragment(R.layout.fragment_orders_list) {

    companion object {
        private const val ARG_TAXI = "taxi"

        fun create(taxiId: Int) = OrdersListFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_TAXI, taxiId)
            }
        }
    }

    private val viewModel by inject<OrdersListViewModel>()
    private lateinit var adapter: OrdersAdapter
    private var nextOffset = ""
    private var firstLoad = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                back()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)

        with(viewModel) {
            getOrders("")

            isProgressVisible.observe(viewLifecycleOwner) { visible ->
                progress_bar.visibility = if (visible) VISIBLE else GONE
            }

            error.observe(viewLifecycleOwner) { context?.shortToast(it) }

            orders.observe(viewLifecycleOwner) {
                nextOffset = it.nextOffset ?: ""

                when (firstLoad) {
                    true -> {
                        adapter = OrdersAdapter(it.orders?.filter {
                            it.sourceId == arguments?.get(ARG_TAXI).toString()
                        } as MutableList<Order>) {
                            openOrderDetails(it)
                        }
                        orders_recycler.adapter = adapter

                        empty_tv.visibility = if (orders_recycler.adapter?.itemCount == 0) VISIBLE else GONE

                        firstLoad = false
                    }
                    false -> {
                        adapter.updateList(it.orders?.filter {
                            it.sourceId == arguments?.get(ARG_TAXI).toString()
                        } as MutableList<Order>)
                    }
                }
            }

            val scrollListener = object : EndlessScrollListener() {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    getOrders(nextOffset)
                }
            }

            orders_recycler.addOnScrollListener(scrollListener)
        }
    }

    private fun openOrderDetails(order: Order) {
        with(requireActivity()) {
            date_time_tv.text = order.getDateWithTime()
            order_address_from.text = order.addressFrom
            order_address_to.text = order.addressTo
            received_amount.text = order.amountClient
            tariff_amount.text = order.amountDriver
            tip_amount.text = order.amountTip
            commission_amount.text = order.amountFeeAgr
            our_commission_amount.text = order.amountFeeOur
            order_total_amount.text = order.amountTotal.toString()
            total_sum_tv.setFormattedText(R.string.order_balance_format, order.amountTotal?.toDouble() ?: 0.0)

            when (order.status) {
                "0" -> {
                    order_circle_image.setImageResource(R.drawable.ic_green_circle)
                    order_status_tv.text = getString(R.string.complete)
                }
                "-1" -> {
                    order_circle_image.setImageResource(R.drawable.ic_red_circle)
                    order_status_tv.text = getString(R.string.canceled)
                }
                else -> order_circle_image.setImageResource(R.drawable.ic_green_circle)
            }
            order_layout.visible()

            order_back_arrow.setOnClickListener { hideOrderDetails() }
            back_button.setOnClickListener { hideOrderDetails() }
        }
    }

    private fun hideOrderDetails() = requireActivity().order_layout.gone()

    private fun back() {
        when (requireActivity().order_layout.visibility) {
            VISIBLE -> hideOrderDetails()
            GONE -> findNavController(this).popBackStack()
        }
    }
}