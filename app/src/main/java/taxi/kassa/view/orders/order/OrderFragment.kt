package taxi.kassa.view.orders.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.fragment_order.*
import taxi.kassa.R
import taxi.kassa.model.responses.Order
import taxi.kassa.view.MainActivity
import taxi.kassa.view.orders.OrdersFragment

class OrderFragment : Fragment() {

    companion object {
        private const val ARG_ORDER = "order"

        fun create(order: Order): OrderFragment {
            return OrderFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_ORDER, order)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_order, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                back()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        val order = arguments?.getParcelable<Order>(ARG_ORDER)

        date_time_tv.text = order?.getDateWithTime()
        address_from.text = order?.addressFrom
        address_to.text = order?.addressTo
        received_amount.text = order?.amountClient
        tariff_amount.text = order?.amountDriver
        tip_amount.text = order?.amountTip
        commission_amount.text = order?.amountFeeAgr
        our_commission_amount.text = order?.amountFeeOur
        total_amount.text = order?.amountTotal.toString()
        total_sum_tv.text = getString(R.string.order_balance_format, order?.amountTotal.toString())

        when (order?.status) {
            "0" -> {
                circle_image.setImageResource(R.drawable.ic_green_circle)
                status_tv.text = getString(R.string.complete)
            }
            "-1" -> {
                circle_image.setImageResource(R.drawable.ic_red_circle)
                status_tv.text = getString(R.string.canceled)
            }
            else -> circle_image.setImageResource(R.drawable.ic_green_circle)
        }

        back_arrow.setOnClickListener { back() }
        back_button.setOnClickListener { back() }
    }

    private fun back() {
        val transaction: FragmentTransaction = (context as MainActivity).supportFragmentManager.beginTransaction()
        transaction.replace(R.id.navHostFragment, OrdersFragment())
        transaction.commit()
    }
}