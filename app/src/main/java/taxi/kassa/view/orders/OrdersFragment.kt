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
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.fragment_orders.*
import kotlinx.android.synthetic.main.item_taxi_orders.view.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.view.orders.adapter.OrdersPagerAdapter
import taxi.kassa.view.orders.adapter.OrdersTaxiAdapter
import taxi.kassa.view.orders.list.OrdersListFragment

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

        val adapter = OrdersPagerAdapter(childFragmentManager)
        adapter.addFragment(OrdersListFragment.create(1))
        adapter.addFragment(OrdersListFragment.create(4))
        adapter.addFragment(OrdersListFragment.create(3))

        view_pager.adapter = adapter

        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        taxi_recycler[0].performClick()
                        taxi_recycler.scrollToPosition(0)
                    }
                    1 -> taxi_recycler[1].performClick()
                    2 -> {
                        taxi_recycler[2].performClick()
                        taxi_recycler.scrollToPosition(2)
                    }
                }
            }
        })

        viewModel.taxis.observe(viewLifecycleOwner, Observer {
            taxi_recycler.adapter = OrdersTaxiAdapter(it) { view, taxi ->
                val items = mutableListOf(
                    taxi_recycler[0], taxi_recycler[1], taxi_recycler[2]
                )

                items.map { item ->
                    if (item != view) {
                        item.space.background = getDrawable(requireContext(), R.color.colorAccent)
                    } else {
                        item.space.background =
                            getDrawable(requireContext(), R.color.withdraws_yellow)
                    }
                }

                when (taxi.taxiName) {
                    getString(R.string.yandex_title) -> view_pager.currentItem = 0
                    getString(R.string.gett_title) -> view_pager.currentItem = 1
                    getString(R.string.citymobil_title) -> view_pager.currentItem = 2
                }
            }
        })

        val runnable = Runnable {
            taxi_recycler[0]?.performClick()
        }
        Handler().postDelayed(runnable, 500)

        back_arrow.setOnClickListener { activity?.onBackPressed() }
    }
}