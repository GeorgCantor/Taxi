package taxi.kassa.view.withdraws.withdraw_create.instant

import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.get
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_instant_withdraw.*
import kotlinx.android.synthetic.main.item_cardd.view.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import taxi.kassa.R
import taxi.kassa.util.*
import taxi.kassa.util.Constants.CITYMOBIL
import taxi.kassa.util.Constants.GETT
import taxi.kassa.util.Constants.NOT_FROM_PUSH
import taxi.kassa.util.Constants.PUSH_COUNTER
import taxi.kassa.util.Constants.YANDEX

class InstantWithdrawFragment : Fragment() {

    private lateinit var viewModel: InstantWithdrawViewModel

    private val taxiType: String by lazy { arguments?.get(Constants.TAXI) as String }
    private var sourceId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(R.layout.fragment_instant_withdraw)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back_arrow.setOnClickListener { findNavController(this).popBackStack() }

        notification_image.setOnClickListener {
            findNavController(this).navigate(
                R.id.action_instantWithdrawFragment_to_notificationsFragment,
                Bundle().apply { putString(NOT_FROM_PUSH, NOT_FROM_PUSH) }
            )
        }

        notification_count.setOnClickListener {
            findNavController(this).navigate(
                R.id.action_instantWithdrawFragment_to_notificationsFragment,
                Bundle().apply { putString(NOT_FROM_PUSH, NOT_FROM_PUSH) }
            )
        }

        call_background.setOnClickListener { activity?.makeCall(this) }

        next_button.setOnClickListener {
            cards_block.gone()
            withdraw_block.visible()
        }

        with(viewModel) {
            getOwnerData()

            isProgressVisible.observe(viewLifecycleOwner) { visible ->
                progress_bar.visibility = if (visible) VISIBLE else GONE
            }

            error.observe(viewLifecycleOwner) { context?.longToast(it) }

            cards.observe(viewLifecycleOwner) {
                when (it.isNullOrEmpty()) {
                    true -> call_dispatcher_block.visible()
                    false -> {
                        cards_block.visible()
                        cards_recycler.setHasFixedSize(true)
                        cards_recycler.adapter = CardsAdapter(it) { card, view ->
                            val items = mutableListOf<View>()
                            (0 until (cards_recycler.adapter?.itemCount ?: 0)).map {
                                items.add(cards_recycler[it])
                            }

                            items.map {
                                if (it == view) {
                                    it.card_background_outline.visible()
                                    it.card_background.invisible()
                                } else {
                                    it.card_background_outline.invisible()
                                    it.card_background.visible()
                                }
                            }
                        }
                    }
                }
            }

            responseOwner.observe(viewLifecycleOwner) { response ->
                response?.let {
                    when (taxiType) {
                        YANDEX -> {
                            sourceId = 1
                            taxi_icon.setImageResource(R.drawable.ic_yandex_mini)
                            taxi_name.text = getString(R.string.yandex_title)
                            balance_tv.text = getString(R.string.account_balance_format, it.balanceYandex)
                        }
                        GETT -> {
                            sourceId = 4
                            taxi_icon.setImageResource(R.drawable.ic_gett_mini)
                            taxi_name.text = getString(R.string.gett_title)
                            balance_tv.text = getString(R.string.account_balance_format, it.balanceGett)
                        }
                        CITYMOBIL -> {
                            sourceId = 3
                            taxi_icon.setImageResource(R.drawable.ic_citymobil_mini)
                            taxi_name.text = getString(R.string.citymobil_title)
                            balance_tv.text = getString(R.string.account_balance_format, it.balanceCity)
                        }
                    }
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

        runDelayed(500) { cards_recycler?.let { if (it.isNotEmpty()) it[0].performClick() } }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
            activity?.makeCall(this)
        }
    }
}