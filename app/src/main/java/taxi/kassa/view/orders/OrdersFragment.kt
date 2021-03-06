package taxi.kassa.view.orders

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.get
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.fragment_orders.*
import kotlinx.android.synthetic.main.item_taxi_orders.view.*
import org.koin.android.ext.android.inject
import taxi.kassa.R
import taxi.kassa.util.Constants.NOT_FROM_PUSH
import taxi.kassa.util.checkSizes
import taxi.kassa.util.runDelayed
import taxi.kassa.view.orders.adapter.OrdersPagerAdapter
import taxi.kassa.view.orders.adapter.OrdersTaxiAdapter
import taxi.kassa.view.orders.list.OrdersListFragment

class OrdersFragment : Fragment(R.layout.fragment_orders) {

    private val viewModel by inject<OrdersViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = OrdersPagerAdapter(childFragmentManager)
        with(adapter) {
            addFragment(OrdersListFragment.create(1))
            addFragment(OrdersListFragment.create(4))
            addFragment(OrdersListFragment.create(3))
        }

        view_pager.adapter = adapter
        view_pager.offscreenPageLimit = 3

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
                try {
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
                } catch (e: IndexOutOfBoundsException) {
                }
            }
        })

        viewModel.taxis.observe(viewLifecycleOwner) {
            taxi_recycler.adapter = OrdersTaxiAdapter(it) { view, taxi ->
                try {
                    val items = mutableListOf(
                        taxi_recycler[0], taxi_recycler[1], taxi_recycler[2]
                    )

                    items.forEach { item ->
                        item.space.background = getDrawable(
                            requireContext(),
                            if (item != view) R.color.colorAccent else R.color.withdraws_yellow
                        )
                    }

                    when (taxi.taxiName) {
                        getString(R.string.yandex_title) -> view_pager.currentItem = 0
                        getString(R.string.gett_title) -> view_pager.currentItem = 1
                        getString(R.string.citymobil_title) -> view_pager.currentItem = 2
                    }
                } catch (e: IndexOutOfBoundsException) {
                }
            }
        }

        viewModel.notifications.observe(viewLifecycleOwner) {
            context?.checkSizes(it, notification_count, notification_image)
        }

        500L.runDelayed { taxi_recycler?.let { if (it.isNotEmpty()) it[0].performClick() } }

        notification_image.setOnClickListener {
            findNavController(this).navigate(
                R.id.action_ordersFragment_to_notificationsFragment,
                Bundle().apply { putString(NOT_FROM_PUSH, NOT_FROM_PUSH) }
            )
        }

        notification_count.setOnClickListener {
            findNavController(this).navigate(
                R.id.action_ordersFragment_to_notificationsFragment,
                Bundle().apply { putString(NOT_FROM_PUSH, NOT_FROM_PUSH) }
            )
        }

        back_arrow.setOnClickListener { activity?.onBackPressed() }
    }
}